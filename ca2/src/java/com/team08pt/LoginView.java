/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.team08pt;

import com.team08pt.business.GroupsBean;
import com.team08pt.business.UsersBean;
import com.team08pt.model.Groups;
import com.team08pt.model.GroupsPK;
import com.team08pt.model.Users;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.codec.digest.DigestUtils;

@ViewScoped
@Named
public class LoginView implements Serializable {

    private static final long serialVersionUID = 1L;

    private String userid;
    private String password;
    private String cpassword;
    private int count;
    
    private static final String AUTHENTICATED = "authenticated";
    
    @EJB
    private UsersBean userBean;
    @EJB
    private GroupsBean groupBean;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCpassword() {
        return cpassword;
    }

    public void setCpassword(String cpassword) {
        this.cpassword = cpassword;
    }

    public String login() {

        HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance()
                .getExternalContext()
                .getRequest();

        try {
            req.login(userid, password);
        } catch (ServletException ex) {
            FacesMessage msg = new FacesMessage("Incorrect login");
            count++;
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return ("");
        }
        System.out.println(">>> LoginView user: " + req.getRemoteUser());
        return ("/secure/menu");

    }
    
    public String register() {
        
        Users user = new Users();
        user.setUserid(userid);
        
        if (!password.equals(cpassword)) {
            FacesMessage msg = new FacesMessage("Password and confirm password do not match.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return ("");
        }
        
        String encodedPassword = DigestUtils.sha256Hex(password);
        user.setPassword(encodedPassword);
        
        try {
            userBean.register(user);
            
            Groups group = new Groups();
            group.setGroupsPK(new GroupsPK(AUTHENTICATED, userid));
            
            groupBean.addUser(group);
        } catch(Exception e) {
            e.printStackTrace();
            FacesMessage msg = new FacesMessage("Username already in used.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return ("");
        }
        FacesMessage msg = new FacesMessage("User created succesfully.");
        FacesContext.getCurrentInstance().addMessage(null, msg);
        return "/register";
    }

}
