/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejava2016.pt08.ca1.rest;

import ejava2016.pt08.ca1.business.PeopleBean;
import ejava2016.pt08.ca1.model.People;
import java.util.Optional;
import java.util.UUID;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
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
    
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public Response registerPeople(MultivaluedMap<String, String> formData) {
        String name = formData.getFirst("name");
        String email = formData.getFirst("email");
        
        People p = new People();
        p.setName(name);
        p.setEmail(email);
        p.setPid(UUID.randomUUID().toString().substring(0, 8));
        
        peopleBean.register(p);
        
        JsonObject json = Json.createObjectBuilder()
                .add("name", name)
                .add("email", email)
                .build();
        
        System.out.println(String.format(">>> Name: %s, Email: %s", name, email));
        
        return (Response.status(Response.Status.CREATED)
                .entity(json.toString())
                .build());
    }
    
}
