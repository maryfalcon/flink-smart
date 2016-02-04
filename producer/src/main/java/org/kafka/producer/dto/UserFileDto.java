package org.kafka.producer.dto;

import java.io.Serializable;

/**
 * author: sereja
 * date: 4.2.16.
 */
public class UserFileDto implements Serializable {

    private String fileName;
    private byte[] file;
    private byte[] signature;
    private String extension;

    public UserFileDto(String fileName, byte[] buffer, byte[] signed, String extension) {
        this.fileName = fileName;
        this.file = buffer;
        this.signature = signed;
        this.extension = extension;
    }

    public UserFileDto() {}

    public void setFile(byte[] file) {
        this.file = file;
    }

    public void setSignature(byte[] signature) {
        this.signature = signature;
    }

    public byte[] getSignature() {
        return signature;
    }

    public byte[] getFile() {
        return file;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }
}
