package com.week05.ca3.rest;

import com.week05.ca3.business.PodBean;
import com.week05.ca3.entities.Pod;
import java.io.IOException;
import java.util.Date;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/upload")
@MultipartConfig
public class UploadProxy extends HttpServlet {

    @EJB
    private PodBean podBean;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int podId = new Integer(req.getParameter("podId"));
        String note = req.getParameter("note");
        byte[] image = (byte[]) req.getAttribute("image");
        podBean.updatePOD(podId, image, note);
    }

}
