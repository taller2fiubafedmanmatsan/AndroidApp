package com.taller2.droidclient.model;

import java.util.List;

public class MessagesResponse {
    private String name;
    private boolean isPrivate;
    //private CreatorMsg creator;
    private String _id;
    private List<Page> pages;
    private List<UserMail> users;
    private String description;
    private String welcomeMessage;

    public MessagesResponse(String name, boolean isPrivate, CreatorMsg creator, String _id, List<Page> pages, List<UserMail> users
    , String description, String welcomeMessage) {
        this.name = name;
        this.isPrivate = isPrivate;
        //this.creator = creator;
        this._id = _id;
        this.pages = pages;
        this.users = users;
        this.welcomeMessage = welcomeMessage;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getWelcomeMessage() {
        return welcomeMessage;
    }

    public void setWelcomeMessage(String welcomeMessage) {
        this.welcomeMessage = welcomeMessage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(boolean aPrivate) {
        isPrivate = aPrivate;
    }

    /*public CreatorMsg getCreator() {
        return creator;
    }

    public void setCreator(CreatorMsg creator) {
        this.creator = creator;
    }*/

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public List<Page> getPages() {
        return pages;
    }

    public void setPages(List<Page> pages) {
        this.pages = pages;
    }

    public List<UserMail> getUsers() {
        return users;
    }

    public void setUsers(List<UserMail> users) {
        this.users = users;
    }
}
