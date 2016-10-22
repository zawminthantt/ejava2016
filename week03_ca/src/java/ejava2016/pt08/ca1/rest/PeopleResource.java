/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejava2016.pt08.ca1.rest;

import ejava2016.pt08.ca1.business.PeopleBean;
import ejava2016.pt08.ca1.model.People;
import java.util.Optional;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author kyawminthu
 */
@RequestScoped
@Path("/people")
public class PeopleResource {
    @EJB private PeopleBean peopleBean;
    
    @GET
    @Path("{pid}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findPeopleByID(@PathParam("pid") String pID) {
        Optional<People> optPeople = peopleBean.find(pID);
        System.out.println(">>> findPeopleByID");
        if (optPeople.isPresent()) {
            return (Response.ok(optPeople.get().toJSON())
            .build());
        }
        
        return (Response.status(Response.Status.NOT_FOUND)
				.entity("Customer id not found:" + pID)
				.build());
    }
}
