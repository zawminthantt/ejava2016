/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejava2016.pt08.ca1.business;

import ejava2016.pt08.ca1.model.Appointment;
import java.util.Optional;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author kyawminthu
 */
@Stateless
public class AppointmentBean {
    
    @PersistenceContext private EntityManager entityManager;
    
    public Optional<Appointment> findAppointmentByPeopleID(final Integer pid) {
        return (Optional.ofNullable(entityManager.find(Appointment.class, pid)));
    }
    
}
