/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.team08pt.business;

import com.team08pt.model.Groups;
import com.team08pt.model.Users;
import java.util.Optional;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Named;
import javax.enterprise.context.Dependent;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author kyawminthu
 */
@Named
@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class GroupsBean {

    @PersistenceContext
    private EntityManager entityManager;
    
    public void addUser(Groups group) {
        entityManager.persist(group);
    }

}
