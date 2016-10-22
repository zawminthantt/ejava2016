/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejava2016.pt08.ca1.rest;

import ejava2016.pt08.ca1.business.AppointmentBean;
import ejava2016.pt08.ca1.business.PeopleBean;
import ejava2016.pt08.ca1.model.Appointment;
import ejava2016.pt08.ca1.model.People;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

/**
 *
 * @author kyawminthu
 */
@RequestScoped
@Path("/people")
public class PeopleResource {

    @EJB
    private PeopleBean peopleBean;

    @GET
    @Path("{pid}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findPeopleByID(@PathParam("pid") String pID) {
        Optional<People> optPeople = peopleBean.find(pID);

        if (optPeople.isPresent()) {
            return (Response.ok(optPeople.get().toJSON())
                    .build());
        }

        return (Response.status(Response.Status.NOT_FOUND)
                .entity("No people with such ID: " + pID)
                .build());
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response findPeopleByEmail(@QueryParam("email") String email) {
        Optional<People> optPeople = peopleBean.findByEmail(email);

        if (!optPeople.isPresent()) {
            return (Response.status(Response.Status.NOT_FOUND)
                    .build());
        }

        People people = optPeople.get();
//        JsonArrayBuilder jsonArrayBuilder=Json.createArrayBuilder();
//        if (people.getAppointmentCollection() != null) {
//            for (Appointment a : people.getAppointmentCollection()) {  
//                jsonArrayBuilder.add(a.toJSON());
//            }
//        }
        
        JsonObject returnValue = Json.createObjectBuilder()
                //.add("appointments", jsonArrayBuilder.build())
                .add("pid", people.getPid())
                .add("name", people.getName())
                .add("email", people.getEmail())
                .build();

        return (Response.ok(returnValue).build());
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
