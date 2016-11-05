/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.team08pt;

import java.io.Serializable;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

@ViewScoped
@Named
public class LoginView implements Serializable {

	private static final long serialVersionUID = 1L;

	private String username;
	private String password;
	private int count;

	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}


	public String login() {

		HttpServletRequest req = (HttpServletRequest)FacesContext.getCurrentInstance()
				.getExternalContext()
				.getRequest();

		try {
			req.login(username, password);
		} catch (ServletException ex) {
			FacesMessage msg = new FacesMessage("Incorrect login");
			count++;
			FacesContext.getCurrentInstance().addMessage(null, msg);
			return ("");
		}

		return ("/secure/topsecret");

	}

	
}
