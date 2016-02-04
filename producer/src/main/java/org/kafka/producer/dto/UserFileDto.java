package org.kafka.producer.dto;

import java.io.Serializable;

/**
 * author: sereja
 * date: 4.2.16.
 */
public class UserFileDto implements Serializable {

    public UserFileDto(Long id, String name, byte[] file, byte[] hash, String extension, Integer ownerId) {
        this.id = id;
        this.name = name;
        this.file = file;
        this.hash = hash;
        this.extension = extension;
        this.ownerId = ownerId;
    }

    public UserFileDto() {}

    private Long id;

    private String name;

    private byte[] file;

    private byte[] hash;

    private String extension;

    private Integer ownerId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getHash() {
        return hash;
    }

    public void setHash(byte[] hash) {
        this.hash = hash;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public Integer getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Integer ownerId) {
        this.ownerId = ownerId;
    }

    public byte[] getFile() {
        return file;
    }

    public void setFile(byte[] file) {
        this.file = file;
    }
}