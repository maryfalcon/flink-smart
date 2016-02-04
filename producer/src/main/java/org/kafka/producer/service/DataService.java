package org.kafka.producer.service;

import org.kafka.producer.dto.UserFileDto;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;

/**
 * author: sereja
 * date: 4.2.16.
 */
public class DataService {
    public UserFileDto saveData(HttpServletRequest request, InputStream input) {
        return null;
    }

    public String saveSign(HttpServletRequest request, InputStream input) {
        return null;
    }

    public String getFilesNames() {
        return null;
    }

    public boolean checkSignature(HttpServletRequest request, InputStream input) {
        return false;
    }
}
