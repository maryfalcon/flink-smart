/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myorg.flinkDB;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Arrays;
import java.util.List;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.tuple.Tuple2;

import org.myorg.persistor.Persistor;

public class FlinkApp {
    
    public static PrivateKey pk;
    
    public static void workWithFile(String filename)  throws Exception{
        
        KeyPair kp = RSA.generateKeyPair();//on client

        final ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();

        String initString = "osdjflsdjf dlfjskldf dkfjskldfj"; 
        DataSet<String> text = env.fromElements(initString);
        byte[] signature = RSA.sign(initString.getBytes(), kp.getPrivate()); //on client
        
        /*Data dataToInsert = new Data();
        dataToInsert.setFile(initString.getBytes());
        dataToInsert.setHash(signature);
        dataToInsert.setName(filename);
        new Persistor().insertData(dataToInsert);
        pk = kp.getPrivate();*/
        
    }
    
    public static void getAccessToFile(String fileName/*,byte[] signature*/){
        /*Data data = new Persistor().getDataByName(fileName);
        byte[] signature = RSA.sign(data.getFile(), pk);
        if(Arrays.equals(signature, data.getHash()))
            System.out.println(new String(data.getFile()));
        else
            System.out.println("no");*/
        
    }

    
    public static void main(String[] args) throws Exception {
        workWithFile("fn");
        getAccessToFile("fn");
        

    }
}
