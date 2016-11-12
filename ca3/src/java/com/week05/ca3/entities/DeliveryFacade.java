/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.week05.ca3.entities;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Family-PC
 */
@Stateless
public class DeliveryFacade extends AbstractFacade<Delivery> {

    @PersistenceContext(unitName = "ca3PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public DeliveryFacade() {
        super(Delivery.class);
    }

    @Override
    public void create(Delivery entity) {
        em.persist(entity);
    }
}
