package org.myorg.service;

import org.myorg.dto.UserFileDto;
import org.myorg.model.Data;
import org.myorg.model.User;
import org.myorg.persistor.Persistor;
import org.myorg.quickstart.FlinkApp;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

/**
 * Author: s.kiselyov
 * Date: 21.01.2016
 */
public class DataService implements Serializable {

    private static final long serialVersionUID = -6358623569402935056L;

    private Persistor persistor = new Persistor();

    public Data saveData(UserFileDto userFileDto) throws Exception {
        Data data = new Data(userFileDto.getFileName(), userFileDto.getFile(), "",
                1, userFileDto.getDate(), userFileDto.getPlace());
        Data savedData = persistor.insertData(data);
        FlinkApp.sendFileSignature(savedData.getId(), userFileDto.getSignature());
        return savedData;
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
