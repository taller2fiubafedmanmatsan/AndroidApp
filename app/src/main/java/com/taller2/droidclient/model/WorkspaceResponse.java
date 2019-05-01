package com.taller2.droidclient.model;

import java.util.ArrayList;
import java.util.List;

public class WorkspaceResponse {

    private String name;
    private String imageUrl;
    private String location;
    private String description;
    private String welcomeMessage;
    private List<Channel> channels;

    public WorkspaceResponse(String name) {
        this.name = name;
    }

    public WorkspaceResponse(String name, User creator) {
        this.name = name;
        //this.imageUrl = "https://upload.wikimedia.org/wikipedia/commons/9/93/Logo-fiuba.gif";
        //this.location = "";
        this.description = "Workspace " + name;
        this.welcomeMessage = "Welcome to " + name;
        this.channels = new ArrayList<Channel>();
    }

    public WorkspaceResponse(String name, String imageUrl, String location, User creator, String description, String welcomeMessage, List<Channel> channels, List<User> users, List<User> admins) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.location = location;
        this.description = description;
        this.welcomeMessage = welcomeMessage;
        this.channels = channels;

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

    public List<Channel> getChannels() {
        return channels;
    }

    public void setChannels(List<Channel> channels) {
        this.channels = channels;
    }


    @Override
    public boolean equals(Object obj) {
        //My eyes are bleeding...
        if (!(obj instanceof WorkspaceResponse))
            return false;

        return ((WorkspaceResponse) obj).getName().equals(this.name);
    }
}
