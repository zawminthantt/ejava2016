/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ejava2016.pt08.ca1.business;

import ejava2016.pt08.ca1.model.People;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Myo Wai Yan Kyaw
 */
@Stateless
public class PeopleBean {
    @PersistenceContext private EntityManager em;
    
    public void register(People people) {
        em.persist(people);
    }
}
