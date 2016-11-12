package com.week05.ca3.web;

import com.week05.ca3.business.PodBean;
import com.week05.ca3.entities.Pod;
import java.io.File;
import java.io.IOException;
import javax.ejb.EJB;
import javax.inject.Inject;
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
import org.glassfish.jersey.media.multipart.BodyPart;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.MultiPart;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.media.multipart.file.FileDataBodyPart;

@WebServlet("/callback")
public class CallbackProxy extends HttpServlet {

    @EJB
    private PodBean podBean;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String podId = req.getParameter("pod_id");
        String ackId = req.getParameter("ack_id");
        Pod pod = podBean.find(Integer.parseInt(podId));
        pod.setAckId(ackId);
        podBean.update(pod);

        resp.setStatus(HttpServletResponse.SC_OK);
    }

}
