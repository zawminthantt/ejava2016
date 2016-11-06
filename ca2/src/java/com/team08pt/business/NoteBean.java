/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.team08pt.business;

import com.team08pt.model.Note;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

/**
 *
 * @author kyawminthu
 */
@Named
@Stateless
public class NoteBean {

    @PersistenceContext
    private EntityManager entityManager;
    
    public List<Note> findAll() {
        TypedQuery<Note> query = entityManager.createNamedQuery("Note.findAll",
                Note.class);

        return query.getResultList();
    }
    
    public void createNote(Note note) {
        System.out.println(">>> Creating Note....");
        System.out.println(">>>> Note: " + note);
    }

}
