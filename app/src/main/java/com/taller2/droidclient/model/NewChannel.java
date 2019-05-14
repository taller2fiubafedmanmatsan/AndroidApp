package com.taller2.droidclient.model;

import java.util.List;

public class NewChannel {
    private String workspaceName;
    private String name;
    private List<String> users;
    private boolean isPrivate;
    private String description;
    private String welcomeMessage;
    private String creator;


    public NewChannel(String workspaceName, String name, List<String> users) {
        this.workspaceName = workspaceName;
        this.name = name;
        this.users = users;
        this.creator = users.get(0);
        this.isPrivate = false;
        this.description = "Channel " + name;
        this.welcomeMessage = "Welcome to " + name;
    }
    public NewChannel(String workspaceName, String name,String welcome, String description, List<String> users) {
        this.workspaceName = workspaceName;
        this.name = name;
        this.users = users;
        this.creator = users.get(0);
        this.isPrivate = false;
        this.description = description;
        this.welcomeMessage = welcome;
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

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }
}
