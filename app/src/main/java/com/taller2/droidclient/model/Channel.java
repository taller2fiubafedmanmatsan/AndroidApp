package com.taller2.droidclient.model;

import java.util.List;

public class Channel {
    private String _id;
    private String name;
    private String description;
    private String welcomeMessage;
    private String fakeName;
    private User creator;
    private List<User> users;
    private String channelType;
    private final String GROUP_CHANNEL = "group";
    private final String DIRECT_CHANNEL = "users";

    public Channel(String _id, String name, String description, String welcomeMessage, User creator, List<User> users, String channelType) {
        this._id = _id;
        this.name = name;
        this.description = description;
        this.welcomeMessage = welcomeMessage;
        this.creator = creator;
        this.users = users;
        this.channelType = channelType;
    }

    public Channel(String name, String description, String welcomeMessage) {
        this.name = name;
        this.description = description;
        this.welcomeMessage = welcomeMessage;
    }

    public Channel(String name, String description, String welcomeMessage, String channelType) {
        this.name = name;
        this.description = description;
        this.welcomeMessage = welcomeMessage;
        this.channelType = channelType;
    }

    public Channel(String name) {
        this.name = name;
    }

    public Channel(String name, String channelType) {
        this.name = name;
        this.channelType = channelType;
    }

    public Channel(String _id, String name, String channelType, List<User> users) {
        this._id = _id;
        this.name = name;
        this.channelType = channelType;
        this.users = users;
    }

    public String getFakeName() {
        return fakeName;
    }

    public void setFakeName(String fakeName) {
        this.fakeName = fakeName;
    }

    public boolean is_direct_channel() {
        return this.channelType.equals(DIRECT_CHANNEL);
    }

    public String getChannelType() {
        return channelType;
    }

    public void setChannelType(String channelType) {
        this.channelType = channelType;
    }

    public String getId() {
        return _id;
    }

    public void setId(String _id) {
        this._id = _id;
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

    @Override
    public boolean equals(Object obj) {
        //My eyes are bleeding...
        if (!(obj instanceof Channel))
            return false;

        return ((Channel) obj).getName().equals(this.name);
    }
}
