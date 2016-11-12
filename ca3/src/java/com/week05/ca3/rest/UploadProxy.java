package com.week05.ca3.rest;

import java.io.File;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.MultiPart;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.media.multipart.file.FileDataBodyPart;

@WebServlet("/upload-proxy")
public class UploadProxy extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {
		Client client = ClientBuilder.newBuilder()
				.register(MultiPartFeature.class)
				.build();

		MultiPart part = new MultiPart();

		FileDataBodyPart imgPart = new FileDataBodyPart("image", 
				new File("/home/cmlee/Pictures/ca3.png"),
				MediaType.APPLICATION_OCTET_STREAM_TYPE);
		imgPart.setContentDisposition(
				FormDataContentDisposition.name("image")
				.fileName("ca3.png").build());

		MultiPart formData = new FormDataMultiPart()
				.field("epodId", "abc123", MediaType.TEXT_PLAIN_TYPE)
				.field("note", "a message", MediaType.TEXT_PLAIN_TYPE)
				.field("time", Long.toString(System.currentTimeMillis()), MediaType.TEXT_PLAIN_TYPE)
				.bodyPart(imgPart);
		formData.setMediaType(MediaType.MULTIPART_FORM_DATA_TYPE);


		//part.bodyPart(new FileDataBodyPart("image", 
				//new File("/home/cmlee/Pictures/ca3.png")));
				
		WebTarget target = client.target("http://localhost:8080/epod/upload");
		Invocation.Builder inv = target.request();

		System.out.println(">>> part: " + formData);

		Response callResp = inv.post(Entity.entity(formData, formData.getMediaType()));

		System.out.println(">> call resp:" + callResp.getStatus());
	}

	
	
}
