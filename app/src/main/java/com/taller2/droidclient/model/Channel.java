package com.taller2.droidclient.model;

public class Channel {
    private String id;
    private String name;
    private String description;
    private String welcomeMessage;

    public Channel(String id, String name, String description, String welcomeMessage) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.welcomeMessage = welcomeMessage;
    }

    public Channel(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    @Override
    public boolean equals(Object obj) {
        //My eyes are bleeding...
        if (!(obj instanceof Channel))
            return false;

        return ((Channel) obj).getName().equals(this.name);
    }
}