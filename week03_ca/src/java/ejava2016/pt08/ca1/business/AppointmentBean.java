/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejava2016.pt08.ca1.business;

import ejava2016.pt08.ca1.model.Appointment;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

/**
 *
 * @author kyawminthu
 */
@Stateless
public class AppointmentBean {

    @PersistenceContext
    private EntityManager entityManager;

    public List<Appointment> findAppointmentByPeopleID(final String pid) {
        TypedQuery<Appointment> query = entityManager.createNamedQuery("Appointment.findByPID", Appointment.class);
        query.setParameter("pid", pid);
        
        return query.getResultList();
    }

}
