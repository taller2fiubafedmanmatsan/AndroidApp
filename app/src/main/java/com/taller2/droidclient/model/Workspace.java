package com.taller2.droidclient.model;

import java.util.ArrayList;

public class Workspace {
    private String id;
    private String name;
    //private ArrayList<Channel> channels;

    public Workspace(String id, String name) {
        this.id = id;
        this.name = name;
        //channels = new ArrayList<Channel>();
    }

    /*public void addChannel(Channel channel) {
        channels.add(channel);
    }

    public ArrayList<Channel> getChannels() {
        return channels;
    }*/

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

    @Override
    public boolean equals(Object obj) {
        //My eyes are bleeding...
        if (!(obj instanceof Workspace))
            return false;

        return ((Workspace) obj).getId().equals(this.id);
    }
}
