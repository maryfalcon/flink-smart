package org.myorg;

import org.junit.Test;
import org.myorg.quickstart.RSA;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyPair;

/**
 * Test time of hashing of different sizes and types of files.
 *
 * @author Sergey Kiselev
 */
public class HashingTest {

    private static final String TEST_GIF_NAME = "test.gif";
    private static final String TEST_JPG_NAME = "test.jpg";
    private static final String TEST_PNG_NAME = "test.png";
    private static final String TEST_TEXT_FILE = "file.txt";
    private static final String TEST_BIG_TEXT_FILE = "bigfile.txt";
    private static final String TEST_VERY_BIG_TEXT_FILE = "verybigfile.txt";
    private static final String TEST_MP4_NAME = "test.mp4";
    private static final String TEST_3GP_NAME = "test.3gp";

    // 300Mb file here
    private static final String TEST_AVI_NAME = "test.avi";

    private static final int COUNT = 100;

    @Test
    public void testTextFile() throws IOException {
    //    testFile(TEST_TEXT_FILE, COUNT);
        testFile("bigfile1.txt", COUNT);
        testFile("bigfile2.txt", COUNT);
        testFile("bigfile3.txt", COUNT);
        testFile("bigfile4.txt", COUNT);
        testFile("bigfile5.txt", COUNT);
//        testFile(TEST_VERY_BIG_TEXT_FILE, COUNT);
//        testFile(TEST_GIF_NAME, COUNT);
//        testFile(TEST_JPG_NAME, COUNT);
//        testFile(TEST_PNG_NAME, COUNT);
//        testFile(TEST_MP4_NAME, COUNT);
//        testFile(TEST_3GP_NAME, COUNT);
//        testFile(TEST_AVI_NAME, COUNT);
    }

    private void testFile(String fileName, int count) throws IOException {
        int bytesAvailable;
        ClassLoader classLoader = getClass().getClassLoader();
        byte[] buffer;
        long startTime;
        long endTime;
        long hashStartTime;
        long averageHashTime = 0;
        long averageTime = 0;
        KeyPair keyPair = RSA.generateKeyPair();
        File file = new File(classLoader.getResource(fileName).getFile());
        for (int i = 0; i < count; i++) {
            startTime = System.currentTimeMillis();
            FileInputStream fileInputStream = new FileInputStream(file);
            bytesAvailable = fileInputStream.available();
            buffer = new byte[bytesAvailable];
            int read = fileInputStream.read(buffer, 0, bytesAvailable);
            hashStartTime = System.currentTimeMillis();
            byte[] signed = RSA.sign(buffer, keyPair.getPrivate());
            endTime = System.currentTimeMillis();
            fileInputStream.close();
            averageHashTime += (endTime - hashStartTime);
            averageTime += (endTime - startTime);
        }
        averageHashTime /= count;
        averageTime /= count;
        System.out.println("Hashing of " + fileName +
                ", number of iterations: " + count +
                ", average time: " + averageTime + "ms" +
                ", average time of hashing: " + averageHashTime + "ms" +
                ", file size: " + file.length()/1024 + "kb");
    }

}
