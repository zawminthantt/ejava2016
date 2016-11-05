package com.team08pt;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(urlPatterns = {"/logout", "/secure/logout", "/faces/logout", "/faces/secure/logout"})
public class LogoutServlet extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {
		HttpSession session = req.getSession();
		System.out.println(">>> user: " + req.getRemoteUser());
		session.invalidate();
		getServletContext()
				.getRequestDispatcher("/faces/login.xhtml")
				.forward(req, resp);
	}

	
	
}
