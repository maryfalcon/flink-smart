package org.myorg.service;

import org.myorg.model.Data;
import org.myorg.model.User;
import org.myorg.persistor.Persistor;
import org.myorg.quickstart.FlinkApp;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: s.kiselyov
 * Date: 21.01.2016
 */
public class DataService {

    private Persistor persistor = new Persistor();

    public Data saveData(HttpServletRequest request, InputStream input) throws IOException {
        String name = request.getParameter("name");
        String extension = request.getParameter("extension");
        String userLoginHash = request.getHeader("Authorization");
        if (userLoginHash != null) {
            userLoginHash = userLoginHash.substring(6);
        }
        User user = persistor.checkUserByHash(userLoginHash);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        for (int length; (length = input.read(buffer)) > -1;) {
            output.write(buffer, 0, length);
        }
        byte[] bytes = output.toByteArray();
        Data data = new Data(name, bytes, extension, user.getId());
        return persistor.insertData(data);
    }

    public String saveSign(HttpServletRequest request, InputStream input) throws Exception {
        String idString = request.getParameter("fileId");
        if (idString == null) {
            return null;
        }
        int id = Integer.valueOf(idString);
        Data data = persistor.getDataId(id);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        for (int length; (length = input.read(buffer)) > -1;) {
            output.write(buffer, 0, length);
        }
        byte[] bytes = output.toByteArray();
        FlinkApp.sendFileSignature(data.getId(), bytes);
        return "Success";
    }

    public String getFilesNames() {
        String names = "";
        for (Data data : persistor.getAllData()) {
            names += data.getName() + ":";
        }
        return names;
    }

    public boolean checkSignature(HttpServletRequest request, InputStream input) throws Exception {
        String fileName = request.getParameter("fileName");
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        for (int length; (length = input.read(buffer)) > -1;) {
            output.write(buffer, 0, length);
        }
        byte[] bytes = output.toByteArray();
        //return FlinkApp.checkFileSignature(fileName, bytes);
        //TODO: Change request on client side 
        String dateHash = request.getParameter("dateHash");
        String placeHash = request.getParameter("placeHash");
        return FlinkApp.checkFileSignature(fileName,bytes, dateHash.getBytes(), placeHash.getBytes());
        
    }
}
