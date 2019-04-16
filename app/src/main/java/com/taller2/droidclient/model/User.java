package com.taller2.droidclient.model;

import android.net.Uri;
import android.util.Log;

public class User {

    private String _id;
    private String name;
    private String email;
    private String nickname;
    private Boolean isAdmin;
    private String photo_url;

    public User(String _id, String name, String email, String nickname, Boolean isAdmin/*, Uri photo_url*/) {
        this._id = _id;
        this.name = name;
        this.email = email;
        this.nickname = nickname;
        this.isAdmin = isAdmin;

        //Hasta que el server devuelva la url
        //this.photo_url = photo_url;
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

    public String getPhotoUrl() { return photo_url; }
    public void setPhotoUrl(String photo_url) { this.photo_url = photo_url; }
}
