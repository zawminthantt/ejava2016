
package com.week05.ca3.business;

import com.week05.ca3.entities.Delivery;
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
public class PackageDetailBean {
    
    @PersistenceContext private EntityManager entityManager;
    
    public List<Delivery> findAll() {
        TypedQuery<Delivery> query = entityManager.createNamedQuery("Delivery.findAll", Delivery.class);
        
        return (query.getResultList());
    }
    
}
