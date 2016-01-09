/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myorg.quickstart;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Arrays;
import java.util.List;
import javax.net.ssl.HttpsURLConnection;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.tuple.Tuple2;
import org.myorg.model.Data;
import org.myorg.persistor.Persistor;

public class FlinkApp {

    public static PrivateKey pk;

    public static void workWithFile(String filename, String  fileContent) throws Exception {

        KeyPair kp = RSA.generateKeyPair();//on client

        final ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();

        String initString = fileContent;
        DataSet<String> text = env.fromElements(initString);
        byte[] signature = RSA.sign(initString.getBytes(), kp.getPrivate()); //on client

        Data dataToInsert = new Data();
        dataToInsert.setFile(initString.getBytes());
        dataToInsert.setHash(signature);
        dataToInsert.setName(filename);
        dataToInsert = new Persistor().insertData(dataToInsert);
        String resp = sendHttpRequest(signature, "datahash", "&fileid="+dataToInsert.getId());
        pk = kp.getPrivate();

    }
    
    public static void getAccessToEdit(String filename, byte[]  filehash) throws Exception{
        Data data = new Persistor().getDataByName(filename);
        sendHttpRequest(filehash, "hashcheck", "&fileid="+data.getId());
        
    }

    public static void getAccessToFile(String fileName/*,byte[] signature*/) {
        Data data = new Persistor().getDataByName(fileName);
        byte[] signature = RSA.sign(data.getFile(), pk);
        if (Arrays.equals(signature, data.getHash())) {
            System.out.println(new String(data.getFile()));
        } else {
            System.out.println("no");
        }

    }

    public static String sendHttpRequest(byte[] bytesToSend, String param, String otherParams) throws Exception{
        String url = "http://localhost:8080/flinkDB/download";
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        //add reuqest header
        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", "Mozilla/5.0");
        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
        

        String urlParameters = "bytes="+new String(bytesToSend)+"&param="+param+otherParams;

        // Send post request
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(urlParameters);
        wr.flush();
        wr.close();

        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'POST' request to URL : " + url);
        System.out.println("Post parameters : " + urlParameters);
        System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        //print result
        System.out.println(response.toString());
        return response.toString();
    }

    public static void main(String[] args) throws Exception {
        //workWithFile("fn","fkslkdfjslkdfj lkfjsdklf kdfjskd fjkldfjsdlfjl lksdjfklsdjfklsdj flksdjfklsdjf fsdjflksdjfksfksd f");
        //getAccessToFile("fn");
        //sendHttpRequest("fsdflksjdf dksjfsk kfjdsk fkjf sfjkfj sdkjf".getBytes(),"datahash","");
        Data d = new Persistor().getDataByName("fn");
        getAccessToEdit(d.getName(), d.getHash());

    }
}
