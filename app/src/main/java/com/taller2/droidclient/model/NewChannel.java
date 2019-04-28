package com.taller2.droidclient.model;

import java.util.List;

public class NewChannel {
    private String workspaceName;
    private String name;
    private List<String> users;
    private boolean isPrivate;
    private String description;
    private String welcomeMessage;


    public NewChannel(String workspaceName, String name, List<String> users) {
        this.workspaceName = workspaceName;
        this.name = name;
        this.users = users;
        this.isPrivate = false;
        this.description = "Channel" + name;
        this.welcomeMessage = "Welcome to " + name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWorkspaceName() {
        return workspaceName;
    }

    public void setWorkspaceName(String workspaceName) {
        this.workspaceName = workspaceName;
    }

    public List<String> getUsers() {
        return users;
    }

    public void setUsers(List<String> users) {
        this.users = users;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(boolean aPrivate) {
        isPrivate = aPrivate;
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
