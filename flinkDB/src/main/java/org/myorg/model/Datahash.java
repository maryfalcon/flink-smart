/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myorg.model;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * @author maryfalcon
 */
@Entity
@Table(name = "datahash")
@XmlRootElement
@NamedQueries({
        @NamedQuery(name = "Datahash.findAll", query = "SELECT d FROM Datahash d"),
        @NamedQuery(name = "Datahash.findById", query = "SELECT d FROM Datahash d WHERE d.id = :id"),
        @NamedQuery(name = "Datahash.findByName", query = "SELECT d FROM Datahash d WHERE d.name = :name"),
        @NamedQuery(name = "Datahash.findByFlinkdbuuid", query = "SELECT d FROM Datahash d WHERE d.flinkdbuuid = :flinkdbuuid"),
        @NamedQuery(name = "Datahash.findByFlinkdbid", query = "SELECT d FROM Datahash d WHERE d.flinkdbid = :flinkdbid")})
public class Datahash implements Serializable {

    @Basic(optional = false)
    @Lob
    @Column(name = "hash")
    private byte[] hash;
    @Lob
    @Column(name = "datehash")
    private byte[] datehash;
    @Lob
    @Column(name = "placehash")
    private byte[] placehash;
    @Column(name = "flinkdbuuid")
    private String flinkdbuuid;
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "name")
    private String name;
    @Column(name = "flinkdbid")
    private Integer flinkdbid;

    public Datahash() {
    }

    public Datahash(Integer id) {
        this.id = id;
    }

    public Datahash(Integer id, byte[] hash) {
        this.id = id;
        this.hash = hash;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getFlinkdbid() {
        return flinkdbid;
    }

    public void setFlinkdbid(Integer flinkdbid) {
        this.flinkdbid = flinkdbid;
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
        if (!(object instanceof Datahash)) {
            return false;
        }
        Datahash other = (Datahash) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.myorg.model.Datahash[ id=" + id + " ]";
    }

    public byte[] getHash() {
        return hash;
    }

    public void setHash(byte[] hash) {
        this.hash = hash;
    }

    public byte[] getDatehash() {
        return datehash;
    }

    public void setDatehash(byte[] datehash) {
        this.datehash = datehash;
    }

    public byte[] getPlacehash() {
        return placehash;
    }

    public void setPlacehash(byte[] placehash) {
        this.placehash = placehash;
    }

    public String getFlinkdbuuid() {
        return flinkdbuuid;
    }

    public void setFlinkdbuuid(String flinkdbuuid) {
        this.flinkdbuuid = flinkdbuuid;
    }

}
