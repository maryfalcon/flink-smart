package com.example.skiselyov.myapplication.com.example.notepadby.myapplication.model;

import java.io.Serializable;

/**
 * Created by s.kiselyov on 20.01.2016.
 */
public class UserSession implements Serializable {

    private String login;
    private String password;
    private boolean isAuthenticated;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isAuthenticated() {
        return isAuthenticated;
    }

    public void setIsAuthenticated(boolean isAuthenticated) {
        this.isAuthenticated = isAuthenticated;
    }
}
