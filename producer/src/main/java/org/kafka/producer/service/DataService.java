package org.kafka.producer.service;

import org.codehaus.jackson.map.ObjectMapper;
import org.kafka.producer.dto.UserFileDto;
import org.kafka.producer.remote.KafkaRemoteSender;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * author: sereja
 * date: 4.2.16.
 */
public class DataService {

    private KafkaRemoteSender remoteSender = new KafkaRemoteSender();

    public UserFileDto saveData(HttpServletRequest request, InputStream input) throws Exception {
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
        final ObjectMapper objectMapper = new ObjectMapper();
        UserFileDto userFileDto = objectMapper.readValue(bytes, UserFileDto.class);
        remoteSender.sendFileData(userFileDto);
        return null;
    }
}
