package com.taller2.droidclient.model;

import java.util.Date;

public class BaseMessage {

    private String text;
    private String creator;
    private Date dateTime;
    private String _id;

    public BaseMessage(String _id, String text, String creator, Date dateTime) {
        this._id = _id;
        this.text = text;
        this.creator = creator;
        this.dateTime = dateTime;
    }

    public String getText() {
        return text;
    }

    public void setText(String message) {
        this.text = message;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
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
}

