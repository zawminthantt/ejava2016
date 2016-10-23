/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejava2016.pt08.ca1.model;

import java.io.Serializable;
import java.util.Date;
import javax.json.Json;
import javax.json.JsonObject;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

/**
 *
 * @author kyawminthu
 */
@Entity
@NamedQueries({
    @NamedQuery(name = "Appointment.findAll", query = "SELECT a FROM Appointment a"),
    @NamedQuery(name = "Appointment.findByApptId", query = "SELECT a FROM Appointment a WHERE a.apptId = :apptId"),
    @NamedQuery(name = "Appointment.findByApptDate", query = "SELECT a FROM Appointment a WHERE a.apptDate = :apptDate"), 
    @NamedQuery(name = "Appointment.findByEmail", query = 
            "SELECT a FROM People p join p.appointmentCollection a where p.email = :email")
})
public class Appointment implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @Column(name = "appt_id")
    private Integer apptId;
    
    @Column(name = "description")
    private String description;
    
    @Column(name = "appt_date")
    private Date apptDate;
    
    @JoinColumn(name = "pid", referencedColumnName = "pid")
    @ManyToOne
    private People people;

    public Appointment() {
    }

    public Appointment(Integer apptId) {
        this.apptId = apptId;
    }

    public Appointment(Integer apptId, String description, Date apptDate) {
        this.apptId = apptId;
        this.description = description;
        this.apptDate = apptDate;
    }

    public Integer getApptId() {
        return apptId;
    }

    public void setApptId(Integer apptId) {
        this.apptId = apptId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getApptDate() {
        return apptDate;
    }

    public void setApptDate(Date apptDate) {
        this.apptDate = apptDate;
    }

    public People getPeople() {
        return people;
    }

    public void setPeople(People people) {
        this.people = people;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (apptId != null ? apptId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Appointment)) {
            return false;
        }
        Appointment other = (Appointment) object;

        return !((this.apptId == null && other.apptId != null) || (this.apptId != null && !this.apptId.equals(other.apptId)));
    }

    @Override
    public String toString() {
        return "com.javaee.se24pt08.model.Appointment[ apptId=" + apptId + " ]";
    }
    
    public JsonObject toJSON() {
        return (Json.createObjectBuilder()
                .add("apptId", apptId)
                .add("description", description)
                .add("dateTime", apptDate.toString())
                .build());
    }
}
