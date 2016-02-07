package org.myorg.kafka;

import java.io.Serializable;
import java.util.Date;

/**
 * author: sereja
 * date: 4.2.16.
 */
public class UserFileDto implements Serializable {

    private String fileName;
    private byte[] file;
    private byte[] signature;
    private String extension;
    private Date date;
    private String place;
    private byte[] dateHash;
    private byte[] placeHash;

    public UserFileDto(String fileName, byte[] buffer, byte[] signed, String extension,Date date,String place) {
        this.fileName = fileName;
        this.file = buffer;
        this.signature = signed;
        this.extension = extension;
        this.date = date;
        this.place = place;
    }
    
    public UserFileDto(String fileName, byte[] buffer, byte[] signed, String extension,Date date,String place, byte[] dateHash, byte[] placeHash ) {
        this.fileName = fileName;
        this.file = buffer;
        this.signature = signed;
        this.extension = extension;
        this.date = date;
        this.place = place;
        this.dateHash = dateHash;
        this.placeHash = placeHash;
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public byte[] getDateHash() {
        return dateHash;
    }

    public void setDateHash(byte[] dateHash) {
        this.dateHash = dateHash;
    }

    public byte[] getPlaceHash() {
        return placeHash;
    }

    public void setPlaceHash(byte[] placeHash) {
        this.placeHash = placeHash;
    }
}
