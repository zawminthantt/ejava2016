/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.team08pt;

import com.team08pt.business.NoteBean;
import com.team08pt.business.UsersBean;
import com.team08pt.model.Note;
import com.team08pt.model.Users;
import java.io.Serializable;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author myo
 */
@ViewScoped
@Named
public class NoteView implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private int noteid;
    private String category;
    private String title;
    private String content;
    private Date created_date;
    private String userid;
    private static List<Note> notes;
    
    //@EJB private NoteBean noteBean;
    @EJB private UsersBean userBean;
    @Inject private UserSession userSession;

    public int getNoteid() {
        return noteid;
    }

    public void setNoteid(int noteid) {
        this.noteid = noteid;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreated_date() {
        return created_date;
    }

    public void setCreated_date(Date created_date) {
        this.created_date = created_date;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }
    
    public List<Note> getNotes() {
        Optional<Users> user = userBean.find(userSession.getUsername());
        if (user.isPresent()) {
            notes =  (List<Note>) user.get().getNotesCollection();
            if (notes != null && !notes.isEmpty())
                Collections.sort(notes, (n1, n2) -> n2.getCreateTime().compareTo(n1.getCreateTime()));
        }
        return notes;
    }
    
}
