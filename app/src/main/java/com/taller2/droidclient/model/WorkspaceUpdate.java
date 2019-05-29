package com.taller2.droidclient.model;

public class WorkspaceUpdate {

    private String name;
    private String imageUrl;
    private String location;
    private String description;
    private String welcomeMessage;

    public WorkspaceUpdate(String name, String imageUrl, String location, String description, String welcomeMessage) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.location = location;
        this.description = description;
        this.welcomeMessage = welcomeMessage;
    }

    public WorkspaceUpdate(String name, String description, String welcomeMessage) {
        this.name = name;
        this.description = description;
        this.welcomeMessage = welcomeMessage;
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
}
