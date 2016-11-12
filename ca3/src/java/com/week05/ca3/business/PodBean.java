/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.week05.ca3.business;

import com.week05.ca3.entities.Pod;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

/**
 *
 * @author zawminthant
 */
@Stateless
public class PodBean {
    
    @PersistenceContext private EntityManager entityManager;
    
    public Pod find(int podId) {
        return entityManager.find(Pod.class, podId);
    }
    
    public List<Pod> findAll() {
        TypedQuery<Pod> query = entityManager.createNamedQuery("Pod.findAll", Pod.class);
        
        return (query.getResultList());
    }
    
    public void createPOD(Pod pod) {
        entityManager.persist(pod);
    }
    
    public void updatePOD(int podId, byte[] image, String note) {
        Pod p = entityManager.find(Pod.class, podId);
        p.setImage(image);
        p.setNote(note);
        p.setDeliveryDate(new Date());
        
        entityManager.persist(p);
    }
    
    public void update(Pod pod) {
        entityManager.persist(pod);
    }
    
}
