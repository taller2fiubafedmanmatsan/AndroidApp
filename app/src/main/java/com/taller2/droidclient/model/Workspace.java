package com.taller2.droidclient.model;

import java.util.ArrayList;
import java.util.List;

public class Workspace {

    private String name;
    private String imageUrl;
    private String location;
    private String creator;
    private String description;
    private String welcomeMessage;
    private List<String> channels;
    private List<String> users;
    private List<String> admins;

    public Workspace(String name) {
        this.name = name;
    }

    public Workspace(String name, String creator) {
        this.name = name;
        this.creator = creator;

        //this.imageUrl = "https://upload.wikimedia.org/wikipedia/commons/9/93/Logo-fiuba.gif";
        //this.location = "";
        this.description = "Workspace " + name;
        this.welcomeMessage = "Welcome to " + name;
        this.channels = new ArrayList<String>();
        this.users = new ArrayList<String>();
        this.admins = new ArrayList<String>();
        this.users.add(creator);
        this.admins.add(creator);
    }
    public Workspace(String name,String welcome, String description, String creator) {
        this.name = name;
        this.creator = creator;
        //this.imageUrl = "https://upload.wikimedia.org/wikipedia/commons/9/93/Logo-fiuba.gif";
        //this.location = "";
        this.description = description;
        this.welcomeMessage = welcome;
        this.channels = new ArrayList<String>();
        this.users = new ArrayList<String>();
        this.admins = new ArrayList<String>();
        this.users.add(creator);
        this.admins.add(creator);
    }

    public Workspace(String name, String imageUrl, String location, String creator, String description, String welcomeMessage, List<String> channels, List<String> users, List<String> admins) {
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

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
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

    public List<String> getUsers() {
        return users;
    }

    public void setUsers(List<String> users) {
        this.users = users;
    }

    public List<String> getAdmins() {
        return admins;
    }

    public void setAdmins(List<String> admins) {
        this.admins = admins;
    }

    @Override
    public boolean equals(Object obj) {
        //My eyes are bleeding...
        if (!(obj instanceof Workspace))
            return false;

        return ((Workspace) obj).getName().equals(this.name);
    }
}
