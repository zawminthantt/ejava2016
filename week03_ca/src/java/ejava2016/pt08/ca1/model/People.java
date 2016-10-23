/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejava2016.pt08.ca1.model;

import java.io.Serializable;
import java.util.List;
import javax.json.Json;
import javax.json.JsonObject;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author kyawminthu
 */
@Entity
@NamedQueries({
    @NamedQuery(name = "People.findAll", query = "SELECT p FROM People p")
    ,
    @NamedQuery(name = "People.findByPid", query = "SELECT p FROM People p WHERE p.pid = :pid")
    ,
    @NamedQuery(name = "People.findByName", query = "SELECT p FROM People p WHERE p.name = :name")
    ,
    @NamedQuery(name = "People.findByEmail", query = "SELECT p FROM People p WHERE p.email = :email")})
public class People implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    private String pid;

    private String name;

    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    private String email;

    @OneToMany(mappedBy = "people")
    private List<Appointment> appointmentCollection;

    public People() {
    }

    public People(String pid) {
        this.pid = pid;
    }

    public People(String pid, String name, String email) {
        this.pid = pid;
        this.name = name;
        this.email = email;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @XmlTransient
    public List<Appointment> getAppointmentCollection() {
        return appointmentCollection;
    }

    public void setAppointmentCollection(List<Appointment> appointmentCollection) {
        this.appointmentCollection = appointmentCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (pid != null ? pid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof People)) {
            return false;
        }
        People other = (People) object;

        return !((this.pid == null && other.pid != null) || (this.pid != null && !this.pid.equals(other.pid)));
    }

    @Override
    public String toString() {
        return "com.javaee.se24pt08.model.People[ pid=" + pid + " ]";
    }

    public JsonObject toJSON() {
        return (Json.createObjectBuilder()
                .add("pid", pid)
                .add("name", name)
                .add("email", email)
                .build());
    }

}
