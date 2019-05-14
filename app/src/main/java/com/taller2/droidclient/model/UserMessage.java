package com.taller2.droidclient.model;

import android.net.Uri;

import java.util.Date;

public class UserMessage {

    private String message;
    private User creator;
    private Date dateTime;
    private String _id;
    private int type;
    private String photoUrl;

    public UserMessage(String _id, String message, User creator, Date dateTime, int type, String photoUrl) {
        this._id = _id;
        this.message = message;
        this.creator = creator;
        this.dateTime = dateTime;
        this.type = type;
        this.photoUrl = photoUrl;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
}
