package com.taller2.droidclient.model;

public class CreatorMsg {
    private String _id;
    private String name;
    private String email;
    private String nickname;

    public CreatorMsg(String _id, String name, String email, String nickname) {
        this._id = _id;
        this.name = name;
        this.email = email;
        this.nickname = nickname;
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
}
