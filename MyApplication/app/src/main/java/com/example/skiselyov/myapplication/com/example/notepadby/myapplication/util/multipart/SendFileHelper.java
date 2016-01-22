package com.example.skiselyov.myapplication.com.example.notepadby.myapplication.util.multipart;

import android.util.Base64;

import com.example.skiselyov.myapplication.com.example.notepadby.myapplication.model.UserSession;

import org.json.JSONObject;

import java.io.BufferedReader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public final class SendFileHelper {
    public static final int BUFFER_SIZE = 4096;
    private final String boundary;
    private static final String LINE_FEED = "\r\n";
    private HttpURLConnection httpConnection;
    private String charset;
    private OutputStream outputStream;
    private PrintWriter writer;
    private String serverUrl;

    private SendFileHelper(String serverUrl, String charset) throws Exception {
        this.charset = charset;
        this.serverUrl = serverUrl;

        boundary = "===" + System.currentTimeMillis() + "===";

        URL url = new URL(this.serverUrl);
        httpConnection = (HttpURLConnection) url.openConnection();
        httpConnection.setUseCaches(false);
        httpConnection.setDoOutput(true);
        httpConnection.setDoInput(true);
        httpConnection.setRequestMethod("POST");
        httpConnection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
    }

    public SendFileHelper writeFile(File uploadFile)
            throws IOException {
        FileInputStream inputStream = new FileInputStream(uploadFile);
        byte[] buffer = new byte[BUFFER_SIZE];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }
        outputStream.flush();
        inputStream.close();
        return this;
    }

    public SendFileHelper writeBytes(byte[] bytes) throws IOException {
        outputStream.write(bytes);
        outputStream.flush();
        return this;
    }

    private void addHeaderField(String name, String value) {
        httpConnection.setRequestProperty(name, value);
    }

    public String finish() throws Exception {
        int status = httpConnection.getResponseCode();
        if (status != HttpURLConnection.HTTP_OK) {
            throw new IOException("Server returned non-OK status: " + status);
        }
        String response = readResponse();
        httpConnection.disconnect();
        return response;
    }

    private String readResponse() throws Exception {
        String response = "";
        BufferedReader reader = new BufferedReader(new InputStreamReader(
                httpConnection.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            response += line;
        }
        reader.close();
        return response;
    }

    private void connect() throws IOException {
        outputStream = httpConnection.getOutputStream();
        writer = new PrintWriter(new OutputStreamWriter(outputStream, charset), true);
    }

    public static SendFileHelper createMultipartRequest(UserSession currentSession, String serverUrl) throws Exception {
        SendFileHelper multipart = new SendFileHelper(serverUrl, "UTF-8");
        if (currentSession.isAuthenticated()) {
            multipart.addHeaderField("Authorization", getBasicAuth(currentSession.getLogin(), currentSession.getPassword()));
        }
        multipart.connect();
        return multipart;
    }

    public static String getBasicAuth(String username, String password) {
        byte[] encoded = Base64.encode((username + ":" + password).getBytes(), 0);
        return "Basic " + new String(encoded);
    }
}
