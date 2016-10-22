/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejava2016.pt08.ca1.business;

import ejava2016.pt08.ca1.model.People;
import java.util.List;
import java.util.Optional;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

/**
 *
 * @author Myo Wai Yan Kyaw
 */
@Stateless
public class PeopleBean {

    @PersistenceContext
    private EntityManager entityManager;

    public Optional<People> find(final String pId) {
        return (Optional.ofNullable(entityManager.find(People.class, pId)));
    }
    
    public Optional<People> findByEmail(final String email) {
        TypedQuery<People> query = entityManager.createNamedQuery("People.findByEmail", People.class);
        query.setParameter("email", email);
        
        List <People> result = query.getResultList();
        
        if (result.size() > 0) {
            return Optional.of(result.get(0));
        }
        
        return Optional.empty();
    }

    public List<People> findAll() {
        TypedQuery<People> query = entityManager.createNamedQuery("People.findAll",
                People.class);

        return query.getResultList();
    }
    
    public void register(People people) {
        entityManager.persist(people);
    }
}
