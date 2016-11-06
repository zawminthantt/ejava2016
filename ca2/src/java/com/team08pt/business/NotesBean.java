/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.team08pt.business;

import com.team08pt.model.Notes;
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
public class NotesBean {

    @PersistenceContext
    private EntityManager entityManager;
    
    public List<Notes> findAll() {
        TypedQuery<Notes> query = entityManager.createNamedQuery("Notes.findAll",
                Notes.class);

        return query.getResultList();
    }

}
