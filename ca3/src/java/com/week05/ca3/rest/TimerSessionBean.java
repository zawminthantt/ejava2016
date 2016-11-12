/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.week05.ca3.rest;

import com.week05.ca3.business.PodBean;
import com.week05.ca3.entities.Pod;
import com.week05.ca3.entities.PodFacade;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerService;
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

/**
 *
 * @author kyawminthu
 */
@Singleton
public class TimerSessionBean {

    @Resource
    TimerService timerService;

    @EJB
    private PodBean podBean;

    private Logger logger = Logger.getLogger(
            "com.sun.tutorial.javaee.ejb.timersession.TimerSessionBean");

    public void createTimer(int podId) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND, 30);
        timerService.createTimer(calendar.getTime(), podId);
    }

    @Timeout
    public void sendReminder(Timer timer) {
        int podId = (int) timer.getInfo();
        Pod pod = podBean.find(podId);

        if (pod != null && pod.getAckId() == null) {
            sendData(pod);
            createTimer(podId);
        }
    }

    public void sendData(Pod pod) {
        Client client = ClientBuilder.newBuilder()
                .register(MultiPartFeature.class)
                .build();
        try {
            File tempFile = File.createTempFile(String.valueOf(System.currentTimeMillis()), ".png", null);
            FileOutputStream fos = new FileOutputStream(tempFile);
            fos.write(pod.getImage());
            fos.close();

            MultiPart part = new MultiPart();

            FileDataBodyPart imgPart = new FileDataBodyPart("image",
                    //new File("/Users/kyawminthu/ca3.png"),
                    tempFile,
                    MediaType.APPLICATION_OCTET_STREAM_TYPE);
            imgPart.setContentDisposition(
                    FormDataContentDisposition.name("image")
                    .fileName("ca3.png").build());

            MultiPart formData = new FormDataMultiPart()
                    .field("teamId", "7db25b8b", MediaType.TEXT_PLAIN_TYPE)
                    .field("epodId", pod.getPodId(), MediaType.TEXT_PLAIN_TYPE)
                    .field("note", pod.getNote(), MediaType.TEXT_PLAIN_TYPE)
                    .field("callback", "http://localhost:8080/ca3/callback", MediaType.TEXT_PLAIN_TYPE)
                    .field("time", pod.getDeliveryDate().getTime(), MediaType.TEXT_PLAIN_TYPE)
                    .bodyPart(imgPart);
            formData.setMediaType(MediaType.MULTIPART_FORM_DATA_TYPE);

            //part.bodyPart(new FileDataBodyPart("image", 
            //new File("/home/cmlee/Pictures/ca3.png")));
            WebTarget target = client.target("http://localhost:8080/epod/upload");
            Invocation.Builder inv = target.request();

            System.out.println(">>> part: " + formData);

            Response callResp = inv.post(Entity.entity(formData, formData.getMediaType()));

            System.out.println(">> call resp:" + callResp.getStatus());
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
