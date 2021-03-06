package com.team08pt;

import com.team08pt.model.Users;
import java.io.Serializable;
import java.security.Principal;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@SessionScoped
@Named
public class UserSession implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private Principal user;

    private Users userModel;

    public Users getUserModel() {
        return userModel;
    }

    public void setUserModel(Users userModel) {
        this.userModel = userModel;
    }

    public String getUsername() {
        return (user.getName());
    }

    public void setUserName(String n) {
    }

    public String logout() {

        HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance()
                .getExternalContext()
                .getRequest();
        HttpSession session = req.getSession();
        System.out.println(">>> UserSession user: " + req.getRemoteUser());
        session.invalidate();
        return ("/login");

    }

}
