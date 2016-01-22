/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myorg.quickstart;

import org.myorg.model.Data;
import org.myorg.persistor.Persistor;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class FlinkApp {

    public static void sendFileSignature(int fileId, byte[] signature) throws Exception {
        //KeyPair kp = RSA.generateKeyPair();//on client
        //final ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
        //DataSet<String> text = env.fromElements(initString);
        //signature = RSA.sign(initString.getBytes(), kp.getPrivate()); //on client
        String resp = sendHttpRequest(signature, "datahash", "&fileid=" + fileId);
    }

    public static boolean checkFileSignature(String filename, byte[] signature) throws Exception {
        Data data = new Persistor().getDataByName(filename);
        String resp = sendHttpRequest(signature, "hashcheck", "&fileid=" + data.getId());
        return resp.equals("true");

    }

    public static String sendHttpRequest(byte[] bytesToSend, String param, String otherParams) throws Exception {
        String url = "http://localhost:8080/flinkDB/download";
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", "Mozilla/5.0");
        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

        String urlParameters = "bytes=" + new String(bytesToSend) + "&param=" + param + otherParams;

        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(urlParameters);
        wr.flush();
        wr.close();

        int responseCode = con.getResponseCode();

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        return response.toString();
    }
}
