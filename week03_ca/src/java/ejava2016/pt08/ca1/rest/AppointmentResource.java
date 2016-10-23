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
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
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
@Path("/appointment")
public class AppointmentResource {

    @EJB
    private AppointmentBean appointmentBean;
    @EJB
    private PeopleBean peopleBean;

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createAppointment(MultivaluedMap<String, String> formData) {
        //http://localhost:8080/week03ca/api/appointment
        String email = formData.getFirst("email");
        String timestamp = formData.getFirst("date");
        String description = formData.getFirst("description");

        Calendar apptDate = Calendar.getInstance();
        apptDate.setTimeInMillis(Long.valueOf(timestamp));

        Optional<People> people = peopleBean.findByEmail(email);
        if (people.isPresent()) {
            Appointment appointment = new Appointment();
            appointment.setPeople(people.get());
            appointment.setDescription(description);
            appointment.setApptDate(apptDate.getTime());
            appointmentBean.addAppointment(appointment);

            System.out.println(String.format(">>> description: %s, Email: %s", description, email));
            JsonObject json = Json.createObjectBuilder()
                    .add("email", email)
                    .add("appt_date", timestamp)
                    .add("description", description)
                    .build();

            return (Response.status(Response.Status.CREATED)
                    .entity(json.toString())
                    .build());
        }
        return (Response.status(Response.Status.NOT_FOUND)
                .build());
    }
    private final ExecutorService executorService = java.util.concurrent.Executors.newCachedThreadPool();

    @GET
    @Path(value = "{email}")
    @Produces(value = MediaType.APPLICATION_JSON)
    public void getAllAppointmentByEmail(@Suspended final AsyncResponse asyncResponse, @PathParam(value = "email") final String email) {
        executorService.submit(() -> {
            asyncResponse.resume(doGetAllAppointmentByEmail(email));
        });
    }

    private Response doGetAllAppointmentByEmail(@PathParam("email") String email) {
        List<Appointment> appointments = appointmentBean.findAppointmentByEmail(email);
        JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
        if (appointments != null) {
            appointments.forEach((a) -> {
                jsonArrayBuilder.add(a.toJSON());
            });
        }
        return (Response.ok(jsonArrayBuilder.build()).build());
    }
}
