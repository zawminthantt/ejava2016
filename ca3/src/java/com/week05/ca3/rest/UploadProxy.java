package com.week05.ca3.rest;

import com.week05.ca3.business.PodBean;
import com.week05.ca3.entities.Pod;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Date;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import sun.nio.ch.IOUtil;

@WebServlet("/upload")
@MultipartConfig
public class UploadProxy extends HttpServlet {

    @EJB
    private PodBean podBean;
    
    @Inject
    private TimerSessionBean timerSessionBean;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int podId = new Integer(req.getParameter("podId"));
        String note = req.getParameter("note");
        //byte[] image = (byte[]) req.getAttribute("image");
        Part image = req.getPart("image");
        OutputStream out = null;
        InputStream filecontent = null;
        String filename = image.getSubmittedFileName();
        byte[] bytes = new byte[1024];

        try {
            out = new FileOutputStream(new File(image.getSubmittedFileName()));
            filecontent = image.getInputStream();

            int read = 0;

            while ((read = filecontent.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }
        } catch (FileNotFoundException fne) {
            fne.printStackTrace();
        } finally {
            if (out != null) {
                out.close();
            }
            if (filecontent != null) {
                filecontent.close();
            }
        }
        podBean.updatePOD(podId, bytes, note);
        
        Pod pod = podBean.find(podId);
        timerSessionBean.sendData(pod);
        timerSessionBean.createTimer(pod.getPodId());
    }

}
