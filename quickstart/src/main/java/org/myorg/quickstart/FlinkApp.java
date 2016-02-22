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
import java.util.Date;
import java.util.List;
import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.aggregation.Aggregations;
import org.apache.flink.api.java.operators.DataSource;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class FlinkApp {
    
    public static void sendFileSignature(int fileId, byte[] signature) throws Exception {
        //KeyPair kp = RSA.generateKeyPair();//on client
        //final ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
        //DataSet<String> text = env.fromElements(initString);
        //signature = RSA.sign(initString.getBytes(), kp.getPrivate()); //on client
        String resp = sendHttpRequest(signature, "datahash", "&fileid=" + fileId);
    }
    
    public static void sendFileSignature(int fileId, byte[] signature,byte[] dateHash,byte[] placeHash) throws Exception {
        
        String resp = sendHttpRequest(signature, "datahash", "&fileid=" + fileId+"&date="+new String(dateHash)+"&place="+new String(placeHash));
    }
    
    public static void sendFileSignature(String fileId, byte[] signature,byte[] dateHash,byte[] placeHash) throws Exception {
        
        String resp = sendHttpRequest(signature, "datahash", "&uuid=" + fileId+"&date="+new String(dateHash)+"&place="+new String(placeHash));
    }

    public static boolean checkFileSignature(String filename, byte[] signature) throws Exception {
        Data data = new Persistor().getDataByName(filename);
        String resp = sendHttpRequest(signature, "hashcheck", "&fileid=" + data.getId());
        return resp.equals("true");
    }
    
    public static boolean checkFileSignature(int id, byte[] signature,byte[] dateHash,byte[] placeHash) throws Exception {
        //Data data = new Persistor().getDataByName(filename);
        String resp = sendHttpRequest(signature, "hashcheck", "&fileid=" + id+"&date="+new String(dateHash)+"&place="+new String(placeHash));
        return resp.equals("true");
    }
    
    public static boolean checkFileSignature(String uuid, byte[] signature,byte[] dateHash,byte[] placeHash) throws Exception {
        
        String resp = sendHttpRequest(signature, "hashcheck", "&fileid=" + uuid+"&date="+new String(dateHash)+"&place="+new String(placeHash));
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
    
    public List<Data> getDataWithDate(final Date date) throws Exception{
       final ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
       DataSet<Data> dataSet = env.fromCollection(new Persistor().getAllData());
       DataSet<Data> newDataSet = dataSet.filter(new FilterFunction<Data>() {public boolean filter(Data data) { return data.getDate().equals(date); }});
       return newDataSet.collect();
    }
    
    public List<Data> getDataWithPlace(final String place) throws Exception{
       final ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
       DataSet<Data> dataSet = env.fromCollection(new Persistor().getAllData());
       DataSet<Data> newDataSet = dataSet.filter(new FilterFunction<Data>() {public boolean filter(Data data) { return data.getPlace().equals(place); }});
       return newDataSet.collect();
    }
    
    public List<Data> getDataWithPlaceAndDate(final String place, final Date date) throws Exception{
       final ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
       DataSet<Data> dataSet = env.fromCollection(new Persistor().getAllData());
       DataSet<Data> newDataSet = dataSet.filter(new FilterFunction<Data>() {public boolean filter(Data data) { return 
               data.getPlace().equals(place) && data.getDate().equals(date); }});
       return newDataSet.collect();
    }
}
