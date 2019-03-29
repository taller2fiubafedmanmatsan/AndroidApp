package com.taller2.droidclient.model;

public class User {

    private String _id;
    private String name;
    private String email;
    private String nickname;
    private Boolean isAdmin;
    private Boolean facebook_log;
    //private String imageURL;

    public User(String _id, String name, String email, String nickname, Boolean isAdmin, Boolean facebook_log/*String imageURL*/) {
        this._id = _id;
        this.name = name;
        this.email = email;
        this.nickname = nickname;
        this.isAdmin = isAdmin;
        this.facebook_log = facebook_log;
        //this.imageURL = imageURL;
    }

    public String getId() {
        return _id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getNickname() {
        return nickname;
    }

    public Boolean getAdmin() {
        return isAdmin;
    }

    public Boolean getFacebook_log() {
        return facebook_log;
    }

    public void setId(String _id) {
        this._id = _id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setAdmin(Boolean admin) {
        isAdmin = admin;
    }

    public void setFacebook_log(Boolean facebook_log) {
        this.facebook_log = facebook_log;
    }
    /*public String getImageURL() {
        return imageURL;
    }*/
}
