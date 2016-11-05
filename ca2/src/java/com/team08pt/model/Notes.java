/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.team08pt.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author kyawminthu
 */
@Entity
@Table(name = "notes")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Notes.findAll", query = "SELECT n FROM Notes n"),
    @NamedQuery(name = "Notes.findByNoteid", query = "SELECT n FROM Notes n WHERE n.noteid = :noteid"),
    @NamedQuery(name = "Notes.findByTitle", query = "SELECT n FROM Notes n WHERE n.title = :title"),
    @NamedQuery(name = "Notes.findByCategory", query = "SELECT n FROM Notes n WHERE n.category = :category"),
    @NamedQuery(name = "Notes.findByContent", query = "SELECT n FROM Notes n WHERE n.content = :content"),
    @NamedQuery(name = "Notes.findByCreateTime", query = "SELECT n FROM Notes n WHERE n.createTime = :createTime")})
public class Notes implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "noteid")
    private Integer noteid;
    @Size(max = 100)
    @Column(name = "title")
    private String title;
    @Size(max = 10)
    @Column(name = "category")
    private String category;
    @Size(max = 255)
    @Column(name = "content")
    private String content;
    @Column(name = "create_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;
    @JoinColumn(name = "userid", referencedColumnName = "userid")
    @ManyToOne
    private Users userid;

    public Notes() {
    }

    public Notes(Integer noteid) {
        this.noteid = noteid;
    }

    public Integer getNoteid() {
        return noteid;
    }

    public void setNoteid(Integer noteid) {
        this.noteid = noteid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Users getUserid() {
        return userid;
    }

    public void setUserid(Users userid) {
        this.userid = userid;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (noteid != null ? noteid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Notes)) {
            return false;
        }
        Notes other = (Notes) object;
        if ((this.noteid == null && other.noteid != null) || (this.noteid != null && !this.noteid.equals(other.noteid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.team08pt.model.Notes[ noteid=" + noteid + " ]";
    }
    
}
