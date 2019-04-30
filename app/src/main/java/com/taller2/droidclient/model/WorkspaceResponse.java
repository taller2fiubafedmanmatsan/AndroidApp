package com.taller2.droidclient.model;

import java.util.ArrayList;
import java.util.List;

public class WorkspaceResponse {

    private String name;
    private String imageUrl;
    private String location;
    private User creator;
    private String description;
    private String welcomeMessage;
    private List<String> channels;
    private List<User> users;
    private List<User> admins;

    public WorkspaceResponse(String name) {
        this.name = name;
    }

    public WorkspaceResponse(String name, User creator) {
        this.name = name;
        this.creator = creator;

        //this.imageUrl = "https://upload.wikimedia.org/wikipedia/commons/9/93/Logo-fiuba.gif";
        //this.location = "";
        this.description = "Workspace " + name;
        this.welcomeMessage = "Welcome to " + name;
        this.channels = new ArrayList<String>();
        this.users = new ArrayList<User>();
        this.admins = new ArrayList<User>();
        this.users.add(creator);
        this.admins.add(creator);
    }

    public WorkspaceResponse(String name, String imageUrl, String location, User creator, String description, String welcomeMessage, List<String> channels, List<User> users, List<User> admins) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.location = location;
        this.creator = creator;
        this.description = description;
        this.welcomeMessage = welcomeMessage;
        this.channels = channels;
        this.users = users;
        this.admins = admins;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
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

    public List<String> getChannels() {
        return channels;
    }

    public void setChannels(List<String> channels) {
        this.channels = channels;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public List<User> getAdmins() {
        return admins;
    }

    public void setAdmins(List<User> admins) {
        this.admins = admins;
    }

    @Override
    public boolean equals(Object obj) {
        //My eyes are bleeding...
        if (!(obj instanceof WorkspaceResponse))
            return false;

        return ((WorkspaceResponse) obj).getName().equals(this.name);
    }
}
