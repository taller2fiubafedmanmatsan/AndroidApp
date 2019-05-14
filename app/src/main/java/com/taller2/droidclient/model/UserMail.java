package com.taller2.droidclient.model;

public class UserMail {
    /*private String email;

    public UserMail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }*/

    private String _id;
    private String name;
    private String email;
    private String nickname;
    private String photoUrl;

    public UserMail(String _id, String name, String email, String nickname, String photoUrl) {
        this._id = _id;
        this.name = name;
        this.email = email;
        this.nickname = nickname;
        this.photoUrl = photoUrl;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
}
