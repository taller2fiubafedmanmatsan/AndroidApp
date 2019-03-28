package com.taller2.droidclient.model;

public class LoginUser {
    private String email;
    private String password;

    //Temporary
    private Boolean facebook_log;

    public LoginUser(String email, String password, Boolean facebook_log) {
        this.email = email;
        this.password = password;
        this.facebook_log = facebook_log;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Boolean getFacebook_log() {
        return facebook_log;
    }
}
