package org.kafka.producer.service;

import org.eclipse.jetty.server.Authentication;
import org.kafka.producer.dto.UserDto;
import org.kafka.producer.dto.UserFileDto;
import org.kafka.producer.remote.KafkaRemoteReceiver;
import org.kafka.producer.remote.KafkaRemoteSender;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * author: sereja
 * date: 4.2.16.
 */
public class DataService {

    private KafkaRemoteSender remoteSender = new KafkaRemoteSender();

    private KafkaRemoteReceiver remoteReceiver = new KafkaRemoteReceiver();

    public UserFileDto saveData(HttpServletRequest request, InputStream input) throws Exception {
        String name = request.getParameter("name");
        String extension = request.getParameter("extension");
        String userLoginHash = request.getHeader("Authorization");
        if (userLoginHash != null) {
            userLoginHash = userLoginHash.substring(6);
        }
        // TODO THink about authentication
        //User user = persistor.checkUserByHash(userLoginHash);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        for (int length; (length = input.read(buffer)) > -1;) {
            output.write(buffer, 0, length);
        }
        byte[] bytes = output.toByteArray();
        UserFileDto userFileDto = new UserFileDto(name, bytes, extension, 1);
        remoteSender.sendFileData(userFileDto);
        return null;
    }

    public String saveSign(HttpServletRequest request, InputStream input) {
        String idString = request.getParameter("fileId");
        if (idString == null) {
            return null;
        }
        int id = Integer.valueOf(idString);
//        Data data = persistor.getDataId(id);
//        ByteArrayOutputStream output = new ByteArrayOutputStream();
//        byte[] buffer = new byte[1024];
//        for (int length; (length = input.read(buffer)) > -1;) {
//            output.write(buffer, 0, length);
//        }
//        byte[] bytes = output.toByteArray();
//        FlinkApp.sendFileSignature(data.getId(), bytes);
        return "Success";
    }

    public String getFilesNames() {
        return null;
    }

    public boolean checkSignature(HttpServletRequest request, InputStream input) {
        return false;
    }
}
