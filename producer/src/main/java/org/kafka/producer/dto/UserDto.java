package org.kafka.producer.dto;

import java.io.Serializable;

/**
 * author: sereja
 * date: 4.2.16.
 */
public class UserDto implements Serializable {

    public UserDto(Integer id, byte[] publicKey, String loginhash) {
        this.id = id;
        this.publicKey = publicKey;
        this.loginhash = loginhash;
    }

    public UserDto() {
    }

    private Integer id;

    private byte[] publicKey;

    private String loginhash;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public byte[] getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(byte[] publicKey) {
        this.publicKey = publicKey;
    }

    public String getLoginhash() {
        return loginhash;
    }

    public void setLoginhash(String loginhash) {
        this.loginhash = loginhash;
    }
}
