package com.week05.ca3.rest;

import com.week05.ca3.business.PackageDetailBean;
import com.week05.ca3.entities.Delivery;
import java.util.List;
import java.util.concurrent.ExecutorService;
import javax.ejb.EJB;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author zawminthant
 */
@Path("/package-detail")
public class PackageDetail {

    @EJB
    private PackageDetailBean packageDetailBean;
    private final ExecutorService executorService = java.util.concurrent.Executors.newCachedThreadPool();

    @GET
    @Path("/all")
    @Produces(MediaType.APPLICATION_JSON)
    public void retrieveAllPackageDetails(@Suspended
            final AsyncResponse asyncResponse) {
        executorService.submit(() -> {
            asyncResponse.resume(doRetrieveAllPackageDetails());
        });

    }

    private Response doRetrieveAllPackageDetails() {
        JsonArrayBuilder arrBuilder = Json.createArrayBuilder();

        List<Delivery> result = packageDetailBean.findAll();

        if (result == null) {
            return (Response.noContent().build());
        } else if (result.size() <= 0) {
            return (Response.noContent().build());
        }

        result.forEach((delivery) -> {
            arrBuilder.add(delivery.toJSON());
        });

        return (Response.ok(arrBuilder.build())
                .build());
    }
}
