package com.example.skiselyov.myapplication.com.example.notepadby.myapplication.util;

import android.util.Log;

import com.example.skiselyov.myapplication.com.example.notepadby.myapplication.model.UserSession;
import com.example.skiselyov.myapplication.com.example.notepadby.myapplication.util.multipart.SendFileHelper;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyPair;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Author: sereja
 * Date: 27.4.15.
 */
public class RemoteUtil {

    private static final Logger logger = Logger.getLogger(RemoteUtil.class.getName());
    private static String serverIP = "http://10.0.2.2";
    //private static String serverIP = "http://192.168.1.4";

    private static String getResponse(String url) {
        try {
            URL urlObject = new URL(url);
            HttpURLConnection con = (HttpURLConnection) urlObject.openConnection();
            con.setRequestMethod("GET");
            con.connect();
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            return response.toString();
        } catch (MalformedURLException e) {
            logger.log(Level.WARNING, "bad url: " + e.getLocalizedMessage());
        } catch (IOException e) {
            logger.log(Level.WARNING, "connectionException: " + e.getLocalizedMessage());
        }
        return "";
    }

    public static String sendPost(String url, String params, UserSession userSession) {
        try {
            URL urlObject = new URL(url);
            HttpURLConnection con = (HttpURLConnection) urlObject.openConnection();
            con.setRequestMethod("POST");
            con.addRequestProperty("Authorization", SendFileHelper.getBasicAuth(userSession.getLogin(), userSession.getPassword()));
            con.connect();
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(params);
            wr.flush();
            wr.close();
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            return response.toString();
        } catch (MalformedURLException e) {
            logger.log(Level.WARNING, "bad url: " + e.getLocalizedMessage());
        } catch (IOException e) {
            logger.log(Level.WARNING, "connectionException: " + e.getLocalizedMessage());
        }
        return "";
    }

    public static String uploadFile(String sourceFileUri, KeyPair keyPair, UserSession userSession) {
        int bytesAvailable;
        byte[] buffer;
        File sourceFile = new File(sourceFileUri);
        if (!sourceFile.isFile()) {
            Log.e("", "Something wrong");
            return null;
        } else {
            try {
                FileInputStream fileInputStream = new FileInputStream(sourceFile);
                bytesAvailable = fileInputStream.available();
                buffer = new byte[bytesAvailable];
                int read = fileInputStream.read(buffer, 0, bytesAvailable);
                byte[] signed = RSA.sign(buffer, keyPair.getPrivate());
                StringBuilder urlBuilder = new StringBuilder();
                urlBuilder.append(serverIP)
                        .append(Constants.CONST_SERVLET_PATH)
                        .append(Constants.QUESTION).append("action").append(Constants.EQUALS).append(Actions.SEND_FILE)
                        .append(Constants.AMPERSAND).append("name").append(Constants.EQUALS).append(sourceFile.getName())
                        .append(Constants.AMPERSAND).append("extension").append(Constants.EQUALS).append(URLConnection.guessContentTypeFromName(sourceFile.getName()));
                String response = SendFileHelper
                        .createMultipartRequest(userSession, urlBuilder.toString())
                        .writeFile(sourceFile)
                        .finish();
                if ("false".equals(response)) {
                    return "Error during file send";
                }
                Integer id = Integer.valueOf(response);
                urlBuilder = new StringBuilder();
                urlBuilder.append(serverIP)
                        .append(Constants.CONST_SERVLET_PATH)
                        .append(Constants.QUESTION).append("action").append(Constants.EQUALS).append(Actions.SEND_SIGN)
                        .append(Constants.AMPERSAND).append(Constants.FILE_ID).append(Constants.EQUALS).append(id);
                response = SendFileHelper
                        .createMultipartRequest(userSession, urlBuilder.toString())
                        .writeBytes(signed)
                        .finish();
                return response;
            } catch (Exception e) {
                Log.e("Upload file", "Exception : " + e.getMessage(), e);
                return null;
            }
        }
    }

    public static boolean remoteLogin(UserSession userSession) {
        StringBuilder urlBuilder = new StringBuilder();
        urlBuilder.append(serverIP)
                .append(Constants.CONST_SERVLET_PATH)
                .append(Constants.QUESTION).append("action").append(Constants.EQUALS).append(Actions.LOGIN);
        String response = sendPost(urlBuilder.toString(), "", userSession);
        return Boolean.TRUE.equals(Boolean.parseBoolean(response));
    }

    public static String getFilesList(UserSession userSession) {
        StringBuilder urlBuilder = new StringBuilder();
        urlBuilder.append(serverIP)
                .append(Constants.CONST_SERVLET_PATH)
                .append(Constants.QUESTION).append("action").append(Constants.EQUALS).append(Actions.FILES_LIST);
        return sendPost(urlBuilder.toString(), "", userSession);
    }

    public static Void checkSignature(String sourceFileUri, UserSession userSession, KeyPair keyPair) throws Exception {
        StringBuilder urlBuilder = new StringBuilder();
        urlBuilder.append(serverIP)
                .append(Constants.CONST_SERVLET_PATH)
                .append(Constants.QUESTION).append("action").append(Constants.EQUALS).append(Actions.UPDATE)
                .append(Constants.AMPERSAND).append("fileName").append(Constants.EQUALS).append(sourceFileUri);
        int bytesAvailable;
        byte[] buffer;
        File sourceFile = new File(Constants.CONST_TEMP_DIR + sourceFileUri);
        FileInputStream fileInputStream = new FileInputStream(sourceFile);
        bytesAvailable = fileInputStream.available();
        buffer = new byte[bytesAvailable];
        int read = fileInputStream.read(buffer, 0, bytesAvailable);
        byte[] signed = RSA.sign(buffer, keyPair.getPrivate());
        String response = SendFileHelper
                .createMultipartRequest(userSession, urlBuilder.toString())
                .writeBytes(signed)
                .finish();
        return null;
    }

    enum Actions {
        LOGIN,
        SEND_FILE,
        SEND_SIGN,
        UPDATE,
        FILES_LIST
    }
}
