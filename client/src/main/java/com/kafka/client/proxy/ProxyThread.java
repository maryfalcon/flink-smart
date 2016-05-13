package com.kafka.client.proxy;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * User: NotePad.by
 * Date: 5/13/2016.
 */
public class ProxyThread extends Thread {
    private Socket socket = null;
    private static final int BUFFER_SIZE = 32768;

    private static final String REQUEST_AUTH_KEY = "auth";
    private static final String REQUEST_URL_KEY = "url";

    public static final String CONST_QUICKSTART_SERVLET = ":8086/download";

    public ProxyThread(Socket socket) {
        super("ProxyThread");
        this.socket = socket;
    }

    /**
     * get input from user
     * send request to server
     * get response from server
     * send response to user
     */
    public void run() {

        try {
            DataOutputStream out =
                    new DataOutputStream(socket.getOutputStream());
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));

            String inputLine, outputLine;
            int cnt = 0;
            Map<String, String> requestMap = getRequestMap(in, cnt);
            BufferedReader rd = null;
            try {
                //System.out.println("sending request
                //to real server for url: "
                //        + urlToCall);
                ///////////////////////////////////
                rd = sendRequestToServer(out, requestMap.get(REQUEST_URL_KEY), rd);

            } catch (Exception e) {
                //can redirect this to error log
                System.err.println("Encountered exception: " + e);
                //encountered error - just send nothing back, so
                //processing can continue
                out.writeBytes("");
            }

            //close out all resources
            if (rd != null) {
                rd.close();
            }
            if (out != null) {
                out.close();
            }
            if (in != null) {
                in.close();
            }
            if (socket != null) {
                socket.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Map<String, String> getRequestMap(BufferedReader in, int cnt) throws IOException {
        String inputLine;
        Map<String, String> requestMap = new HashMap<>();
        //begin get request from client
        while ((inputLine = in.readLine()) != null) {
            try {
                StringTokenizer tok = new StringTokenizer(inputLine);
                tok.nextToken();
            } catch (Exception e) {
                break;

            }
            //parse the first line of the request to find the url
            if (cnt == 0) {
                String[] tokens = inputLine.split(" ");
                String urlToCall = tokens[1];
                //can redirect this to output log
                System.out.println("Request for : " + urlToCall);
                requestMap.put(REQUEST_URL_KEY, urlToCall);
            }

            if (cnt == 1) {
                requestMap.put(REQUEST_AUTH_KEY, inputLine);
            }

            cnt++;
        }
        //end get request from client
        ///////////////////////////////////
        return requestMap;
    }

    private BufferedReader sendRequestToServer(DataOutputStream out, String urlToCall, BufferedReader rd) throws IOException {
        //begin send request to server, get response from server
        URL url = new URL("http://localhost:8086" + urlToCall);
        URLConnection conn = url.openConnection();
        conn.setDoInput(true);
        //not doing HTTP posts
        conn.setDoOutput(false);
        //System.out.println("Type is: "
        //+ conn.getContentType());
        //System.out.println("content length: "
        //+ conn.getContentLength());
        //System.out.println("allowed user interaction: "
        //+ conn.getAllowUserInteraction());
        //System.out.println("content encoding: "
        //+ conn.getContentEncoding());
        //System.out.println("content type: "
        //+ conn.getContentType());

        // Get the response
        InputStream is = null;
        HttpURLConnection huc = (HttpURLConnection) conn;
        if (conn.getContentLength() > 0) {
            try {
                is = conn.getInputStream();
                rd = new BufferedReader(new InputStreamReader(is));
            } catch (IOException ioe) {
                System.out.println(
                        "********* IO EXCEPTION **********: " + ioe);
            }
        }
        //end send request to server, get response from server
        ///////////////////////////////////

        ///////////////////////////////////
        //begin send response to client
        byte by[] = new byte[BUFFER_SIZE];
        int index = is.read(by, 0, BUFFER_SIZE);
        while (index != -1) {
            out.write(by, 0, index);
            index = is.read(by, 0, BUFFER_SIZE);
        }
        out.flush();

        //end send response to client
        ///////////////////////////////////
        return rd;
    }


}