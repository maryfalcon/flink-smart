package com.example.skiselyov.myapplication.com.example.notepadby.myapplication.util;

import android.os.Environment;

public class Constants {
    /**
     * Servlet urls
     */
    public static final String CONST_QUICKSTART_SERVLET = ":8082/quickstart/download";
    public static final String CONST_PRODUCER_SERVLET = ":8083/producer/download";

    /**
     * Application constants
     */
    public static final String CONST_TEMP_DIR =
            Environment.getExternalStorageDirectory() + "/flink/";
    public static final String PUBLIC_KEY_FILE_NAME = "pub";
    public static final String PRIVATE_KEY_FILE_NAME = "pri";

    public static final String CONST_USER_EXTRA = "user";

    public static final String EQUALS = "=";
    public static final String QUESTION = "?";
    public static final String AMPERSAND = "&";
    public static final String FILE_ID = "fileId";
}
