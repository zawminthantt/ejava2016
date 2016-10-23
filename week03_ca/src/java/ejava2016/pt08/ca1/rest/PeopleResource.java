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
import java.util.concurrent.ExecutorService;
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
import javax.ws.rs.QueryParam;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

/**
 *
 * @author kyawminthu, Zaw Min Thant
 */
@RequestScoped
@Path("/people")
public class PeopleResource {

    @EJB
    private PeopleBean peopleBean;

    private final ExecutorService executorService = java.util.concurrent.Executors.newCachedThreadPool();

    @GET
    @Path(value = "{pid}")
    @Produces(value = MediaType.APPLICATION_JSON)
    public void findPeopleByID(@Suspended final AsyncResponse asyncResponse, @PathParam(value = "pid") final String pID) {
        executorService.submit(() -> {
            asyncResponse.resume(doFindPeopleByID(pID));
        });
    }

    private Response doFindPeopleByID(@PathParam("pid") String pID) {
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
    @Produces(value = MediaType.APPLICATION_JSON)
    public void findPeopleByEmail(@Suspended final AsyncResponse asyncResponse, @QueryParam(value = "email") final String email) {
        executorService.submit(() -> {
            asyncResponse.resume(doFindPeopleByEmail(email));
        });
    }

    private Response doFindPeopleByEmail(@QueryParam("email") String email) {
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
    @Consumes(value = MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(value = MediaType.APPLICATION_JSON)
    public void registerPeople(@Suspended final AsyncResponse asyncResponse, final MultivaluedMap<String, String> formData) {
        executorService.submit(() -> {
            asyncResponse.resume(doRegisterPeople(formData));
        });
    }

    private Response doRegisterPeople(MultivaluedMap<String, String> formData) {
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
