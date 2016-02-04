package com.example.skiselyov.myapplication.com.example.notepadby.myapplication.util;

import android.os.Environment;

public class Constants {
    /**
     * Servlet urls
     */
    public static final String CONST_SERVLET_PATH = ":8083/producer/download";
    public static final String CONST_LOGIN_ACTION = "login";
    public static final String CONST_FILES_LIST_ACTION = "files";
    public static final String CONST_UPLOAD_FILE = "upload";

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
