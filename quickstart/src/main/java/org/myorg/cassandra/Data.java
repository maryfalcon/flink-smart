/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myorg.cassandra;

import java.io.Serializable;
import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;


public class Data implements Serializable {

    private static final long serialVersionUID = 1L;
    private String id;
    private byte[] file;
    private String name;
    private String extension;
    private Date date;
    private String place;
    private Integer ownerId;

    public Data() {
    }

    public Data(String id) {
        this.id = id;
    }

    public Data(String id, byte[] file) {
        this.id = id;
        this.file = file;
    }
    
    public Data(String name, byte[] file, String extension, Integer ownerId) {
        this.name = name;
        this.file = file;
        this.extension = extension;
        this.ownerId = ownerId;
    }
    
    public Data(String name, byte[] file, String extension, Integer ownerId, Date date, String place) {
        this.name = name;
        this.file = file;
        this.extension = extension;
        this.ownerId = ownerId;
        this.date = date;
        this.place = place;
    }
    
    public Data(String id,String name, byte[] file, String extension, Integer ownerId, Date date, String place) {
        this.id = id;
        this.name = name;
        this.file = file;
        this.extension = extension;
        this.ownerId = ownerId;
        this.date = date;
        this.place = place;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public byte[] getFile() {
        return file;
    }

    public void setFile(byte[] file) {
        this.file = file;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
    
    public Integer getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Integer ownerId) {
        this.ownerId = ownerId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Data)) {
            return false;
        }
        Data other = (Data) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.myorg.model.Data[ id=" + id + " ]";
    }
    
}
