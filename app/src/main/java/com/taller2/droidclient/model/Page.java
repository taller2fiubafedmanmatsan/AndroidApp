package com.taller2.droidclient.model;

import java.util.List;

public class Page {
    private String _id;
    private List<BaseMessage> messages;
    private int number;

    public Page(String _id, List<BaseMessage> messages, int number) {
        this._id = _id;
        this.messages = messages;
        this.number = number;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public List<BaseMessage> getMessages() {
        return messages;
    }

    public void setMessages(List<BaseMessage> messages) {
        this.messages = messages;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
