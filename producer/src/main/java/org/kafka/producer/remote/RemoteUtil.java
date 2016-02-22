package org.kafka.producer.remote;

import com.sun.jersey.core.util.Base64;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Author: s.kiselyov
 * Date: 08.02.2016
 */
public class RemoteUtil {

    public static String sendHttpRequest(HttpServletRequest request) throws Exception {
        URL urlObject = new URL("http://localhost:8081/quickstart/download?action=LOGIN");
        HttpURLConnection con = (HttpURLConnection) urlObject.openConnection();
        con.setRequestMethod("POST");
        con.addRequestProperty("Authorization", request.getHeader("Authorization"));
        con.setDoOutput(true);
        con.connect();
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes("");
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
