package org.myorg.service;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import org.myorg.dto.UserFileDto;
import org.myorg.model.User;
import org.myorg.persistor.Persistor;
import org.myorg.quickstart.FlinkApp;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Date;
import java.util.logging.Logger;
import org.myorg.cassandra.CassandraPersistor;
import org.myorg.cassandra.Data;

/**
 * Author: s.kiselyov
 * Date: 21.01.2016
 */
public class DataService implements Serializable {

    private static final long serialVersionUID = -6358623569402935056L;

    //private Persistor persistor = new Persistor();
    
    Logger logger= Logger.getLogger("ds");

    public org.myorg.cassandra.Data saveData(UserFileDto userFileDto) throws Exception {
//        Data data = new Data(userFileDto.getFileName(), userFileDto.getFile(), "",
//                1, userFileDto.getDate(), userFileDto.getPlace());
//        Data savedData = persistor.insertData(data);
//         FlinkApp.sendFileSignature(savedData.getId(), userFileDto.getSignature(),userFileDto.getDateHash(), userFileDto.getPlaceHash());
        CassandraPersistor cp = new CassandraPersistor();
        Data data2 = new Data(userFileDto.getFileName(), userFileDto.getFile(), "io",
                1, userFileDto.getDate(), userFileDto.getPlace());
        data2 = cp.insertData(data2);
        cp.close();
        FlinkApp.sendFileSignature(data2.getId(), userFileDto.getSignature(),userFileDto.getDateHash(), userFileDto.getPlaceHash());
       
        return data2;
    }

    public String getFilesNames() {
        CassandraPersistor cp = new CassandraPersistor();
        String names = "";
        for (Data data : cp.getAllData()) {
            names += data.getName() + ":";
        }
        return names;
    }

    public boolean checkSignature(HttpServletRequest request, InputStream input) throws Exception {
        String uuid = request.getParameter("fileName");
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
        return FlinkApp.checkFileSignature(uuid,bytes, dateHash.getBytes(), placeHash.getBytes());
        
    }
}
