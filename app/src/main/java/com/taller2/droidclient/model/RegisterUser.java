package com.taller2.droidclient.model;

public class RegisterUser {

    private String name;
    private String nickname;
    private String email;
    private String password;
    private boolean isAdmin;
    //private boolean facebook_log;
    //private String photo_url;

    public RegisterUser(String name, String nickname, String email, String password/*, Boolean facebook_log, String photo_url*/) {
        this.name = name;
        this.nickname = nickname;
        this.email = email;
        this.password = password;
        this.isAdmin = true;
        //this.facebook_log = facebook_log;
        //this.photo_url = photo_url;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    /*public void setUsingFacebook(boolean facebook) {this.facebook_log = facebook; }

    public boolean getUsingFacebook() { return this.facebook_log; }*/

   /* public String getPhotoUrl() { return photo_url; }

    public void setPhotoUrl(String photo_url) { this.photo_url = photo_url; }*/
}
