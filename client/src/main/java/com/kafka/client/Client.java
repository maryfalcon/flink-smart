package com.kafka.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kafka.client.crypto.RSA;
import com.kafka.client.dto.SendFileHelper;
import com.kafka.client.dto.UserFileDto;
import com.kafka.client.dto.UserSession;
import com.sun.javafx.fxml.builder.URLBuilder;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyPair;
import java.util.Date;
import java.util.zip.Deflater;

/**
 * User: NotePad.by
 * Date: 3/3/2016.
 */
public class Client {

    public static final String CONST_PRODUCER_SERVLET = ":8083/download";
    public static final String CONST_QUICKSTART_SERVLET = ":8086/download";

    private static final String TEST_PNG_NAME = "test.jpg";
    public static final int DATA_TO_SIGN_SIZE = 10000;

    public static void main(String[] args) throws Exception {
        new Client().getFilesList();
    }

    public void getFilesList() throws IOException {
        UserSession userSession = loginUser();
        StringBuilder urlBuilder = new StringBuilder().append("http://127.0.0.1")
                .append(CONST_QUICKSTART_SERVLET).append("?").append("action").append("=").append("FILES_LIST");
        long start = System.currentTimeMillis();
        String result = sendPost(urlBuilder.toString(), userSession);
        long end = System.currentTimeMillis();
        System.out.println(result);
        System.out.println(end - start);
    }

    public void sendFile() throws Exception {
        UserSession userSession = loginUser();
        ObjectMapper mapper = new ObjectMapper();
        ClassLoader classLoader = getClass().getClassLoader();
        KeyPair keyPair = RSA.generateKeyPair();
        long start = System.currentTimeMillis();
        File file = new File(classLoader.getResource(TEST_PNG_NAME).getFile());
        FileInputStream fileInputStream = new FileInputStream(file);
        int bytesAvailable = fileInputStream.available();
        byte[] buffer = new byte[bytesAvailable];
        int read = fileInputStream.read(buffer, 0, bytesAvailable);
        long compressionStart = System.currentTimeMillis();
        byte[] compressedData = compressFile(buffer);
        long signingStart = System.currentTimeMillis();
        byte[] dataToSign = getDataToSign(compressedData);
        byte[] signedData = RSA.sign(dataToSign, keyPair.getPrivate());
        long signingFinish = System.currentTimeMillis();
        StringBuilder urlBuilder = new StringBuilder();
        UserFileDto userFileDto = getUserFileDto(keyPair, file, signedData, compressedData);
        urlBuilder.append("http://127.0.0.1")
                .append(CONST_PRODUCER_SERVLET).append("?").append("action").append("=").append("SEND_FILE");
        String response1 = SendFileHelper
                .createMultipartRequest(userSession, urlBuilder.toString())
                .writeBytes(mapper.writeValueAsBytes(userFileDto))
                .finish();
        long end = System.currentTimeMillis();
        System.out.println(buffer.length);
        System.out.println(compressedData.length);
        System.out.println("Compression time " + (signingStart - compressionStart));
        System.out.println("Signing time " + (signingFinish - signingStart));
        System.out.println(end - start);
    }

    private UserSession loginUser() throws IOException {
        UserSession userSession = new UserSession();
        userSession.setLogin("login");
        userSession.setPassword("password");
        String response = sendPost("http://127.0.0.1" + CONST_QUICKSTART_SERVLET + "?action=LOGIN", userSession);
        userSession.setIsAuthenticated(true);
        return userSession;
    }

    private static byte[] getDataToSign(byte[] compressedData) {
        byte[] dataToSign;
        if (compressedData.length < DATA_TO_SIGN_SIZE) {
            dataToSign = new byte[compressedData.length];
        } else {
            dataToSign = new byte[DATA_TO_SIGN_SIZE];
        }
        System.arraycopy(compressedData, 0, dataToSign, 0, dataToSign.length);
        return dataToSign;
    }

    private static UserFileDto getUserFileDto(KeyPair keyPair, File file, byte[] signed, byte[] compressedData) {
        Date date = new Date();
        String place = "Minsk";
        UserFileDto userFileDto = new UserFileDto();
        userFileDto.setFileName(file.getName());
        userFileDto.setFile(compressedData);
        userFileDto.setSignature(signed);
        userFileDto.setExtension(URLConnection.guessContentTypeFromName(file.getName()));
        userFileDto.setDate(date);
        userFileDto.setDateHash(RSA.sign(date.toString().getBytes(), keyPair.getPrivate()));
        userFileDto.setPlace(place);
        userFileDto.setPlaceHash(RSA.sign(place.getBytes(), keyPair.getPrivate()));
        return userFileDto;
    }

    private static byte[] compressFile(byte[] buffer) throws IOException {
        Deflater deflater = new Deflater();
        deflater.setInput(buffer);
        deflater.setLevel(Deflater.BEST_SPEED);
        deflater.finish();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(buffer.length);
        byte[] buffer11 = new byte[1024];
        while (!deflater.finished()) {
            int count = deflater.deflate(buffer11);
            outputStream.write(buffer11, 0, count);
        }
        outputStream.close();
        return outputStream.toByteArray();
    }

    private static String sendPost(String url, UserSession userSession) throws IOException {
        URL urlObject = new URL(url);
        HttpURLConnection con = (HttpURLConnection) urlObject.openConnection();
        con.setRequestMethod("POST");
        con.setDoOutput(true);
        con.addRequestProperty("Authorization", SendFileHelper.getBasicAuth(userSession.getLogin(), userSession.getPassword()));
        con.connect();
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.flush();
        wr.close();
        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        return response.toString();
    }
}
