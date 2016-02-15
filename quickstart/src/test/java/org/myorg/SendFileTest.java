package org.myorg;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.myorg.dto.UserFileDto;
import org.myorg.quickstart.RSA;
import org.myorg.util.SendFileHelper;
import org.myorg.util.UserSession;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyPair;
import java.util.Date;
import java.util.logging.Level;

/**
 * User: NotePad.by
 * Date: 2/14/2016.
 */
public class SendFileTest {

    public static final String CONST_PRODUCER_SERVLET = ":8083/dwn/download";
    public static final String CONST_QUICKSTART_SERVLET = ":8086/download";

    private static final String TEST_PNG_NAME = "test.png";

    @Test
    public void testSendFile() throws Exception {
        UserSession userSession = new UserSession();
        userSession.setLogin("login");
        userSession.setPassword("password");
        String response = sendPost("http://127.0.0.1" + CONST_QUICKSTART_SERVLET + "?action=LOGIN", userSession);
        userSession.setIsAuthenticated(true);
        ObjectMapper mapper = new ObjectMapper();
        ClassLoader classLoader = getClass().getClassLoader();
        KeyPair keyPair = RSA.generateKeyPair();
        long start = System.currentTimeMillis();
        File file = new File(classLoader.getResource(TEST_PNG_NAME).getFile());
        FileInputStream fileInputStream = new FileInputStream(file);
        int bytesAvailable = 0;
        byte[] buffer;
        bytesAvailable = fileInputStream.available();
        buffer = new byte[bytesAvailable];
        int read = fileInputStream.read(buffer, 0, bytesAvailable);
        byte[] signed = RSA.sign(buffer, keyPair.getPrivate());
        StringBuilder urlBuilder = new StringBuilder();
        Date date = new Date();
        String place = "Minsk";
        UserFileDto userFileDto = new UserFileDto();
        userFileDto.setFileName(file.getName());
        userFileDto.setFile(buffer);
        userFileDto.setSignature(signed);
        userFileDto.setExtension(URLConnection.guessContentTypeFromName(file.getName()));
        userFileDto.setDate(date);
        userFileDto.setDateHash(RSA.sign(date.toString().getBytes(), keyPair.getPrivate()));
        userFileDto.setPlace(place);
        userFileDto.setPlaceHash(RSA.sign(place.getBytes(), keyPair.getPrivate()));
        urlBuilder.append("http://127.0.0.1")
                .append(CONST_PRODUCER_SERVLET).append("?").append("action").append("=").append("SEND_FILE");
        String response1 = SendFileHelper
                .createMultipartRequest(userSession, urlBuilder.toString())
                .writeBytes(mapper.writeValueAsBytes(userFileDto))
                .finish();
        long end = System.currentTimeMillis();
        System.out.println(end - start);
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
