/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejava2016.pt08.ca1.business;

import ejava2016.pt08.ca1.model.Appointment;
import java.util.List;
import java.util.Optional;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

/**
 *
 * @author kyawminthu, zawminthant
 */
@Stateless
public class AppointmentBean {

    @PersistenceContext
    private EntityManager entityManager;

    public List<Appointment> findAppointmentByEmail(final String email) {
        TypedQuery<Appointment> query = entityManager.createNamedQuery("Appointment.findByEmail", Appointment.class);
        query.setParameter("email", email);
        
        return query.getResultList();
    }
    
    public Optional<Appointment> find(final Integer apptId) {
        return (Optional.ofNullable(entityManager.find(Appointment.class, apptId)));
    }

    public void addAppointment(Appointment appointment) {
        entityManager.persist(appointment);
    }
}
