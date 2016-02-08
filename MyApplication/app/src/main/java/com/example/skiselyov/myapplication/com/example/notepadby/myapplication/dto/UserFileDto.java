package com.example.skiselyov.myapplication.com.example.notepadby.myapplication.dto;

import java.io.Serializable;

/**
 * Created by s.kiselyov on 04.02.2016.
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
