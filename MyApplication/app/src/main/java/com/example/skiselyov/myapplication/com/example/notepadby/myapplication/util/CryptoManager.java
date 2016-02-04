package com.example.skiselyov.myapplication.com.example.notepadby.myapplication.util;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Base64;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * Created by s.kiselyov on 20.01.2016.
 */
public class CryptoManager {

    public static void genKeyPair(String path, String privateName, String publicName)
            throws IOException, NoSuchAlgorithmException, NoSuchProviderException {
        String pathpri, pathpub;
        if (path == null) {
            pathpri = privateName;
            pathpub = publicName;
        }
        else {
            pathpri = path + File.separator + privateName;
            pathpub = path + File.separator + publicName;
        }
        FileOutputStream fos = new FileOutputStream(pathpri);
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(1024, new SecureRandom());
        KeyPair kp = keyGen.genKeyPair();
        fos.write(Base64.encode(kp.getPrivate().getEncoded(), Base64.DEFAULT));
        fos.close();
        fos = new FileOutputStream(pathpub);
        fos.write(Base64.encode(kp.getPublic().getEncoded(), Base64.DEFAULT));
        fos.close();
    }

    public static KeyPair loadPair(Context context) {

        /*String pathpri, pathpub;
        if (path == null) {
            pathpri = privateName;
            pathpub = publicName;
        }
        else {
            pathpri = path + File.separator + privateName;
            pathpub = path + File.separator + publicName;
        }*/
        AssetManager assetManager = context.getAssets();
        InputStream inputStream = null;
        PublicKey pubk = null;
        PrivateKey prvk = null;
        try {
            inputStream = assetManager.open("pri");
            int size = inputStream.available(); // size of the file in bytes
            byte [] buffer = new byte[size]; // declare the size of the byte array with
            // size of the file
            inputStream.read(buffer); // read file
            String a = new String(buffer);
            prvk = KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec
                    (Base64.decode(a, Base64.DEFAULT)));

            inputStream.close();

            inputStream = assetManager.open("pub");
            size = inputStream.available(); // size of the file in bytes
            buffer = new byte[size]; // declare the size of the byte array with
            // size of the file
            inputStream.read(buffer); // read file
            a = new String(buffer);
            pubk = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec
                    (Base64.decode(a, Base64.DEFAULT)));
            inputStream.close();
        } catch (IOException | NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
        }

        return new KeyPair(pubk, prvk);
    }

}
