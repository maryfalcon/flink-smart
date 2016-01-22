package com.example.skiselyov.myapplication.integration;

import com.example.skiselyov.myapplication.com.example.notepadby.myapplication.util.Constants;
import com.example.skiselyov.myapplication.com.example.notepadby.myapplication.util.CryptoManager;

import junit.framework.TestCase;

import java.io.File;
import java.io.FileInputStream;
import java.io.PrintWriter;

/**
 * Created by s.kiselyov on 20.01.2016.
 */
public class GenericIntegrationTest extends TestCase
{
    @Override
    public void setUp() throws Exception {
        super.setUp();
    }

    public void testName() throws Exception {
        File file = new File("dadasda");
        file.createNewFile();
        PrintWriter printWriter = new PrintWriter(file);
        printWriter.write("daweafafafafadasdasdasd");
        printWriter.close();
        FileInputStream fileInputStream = new FileInputStream(file);
        byte[] fileBytes = new byte[(int)file.length()];
        int size = fileInputStream.read(fileBytes);
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
    }
}
