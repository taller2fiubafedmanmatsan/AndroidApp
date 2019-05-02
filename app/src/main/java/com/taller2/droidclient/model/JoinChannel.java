package com.taller2.droidclient.model;

import java.util.List;

public class JoinChannel {

    private List<String> users;

    public JoinChannel(List<String> users) {
        this.users = users;
    }


    public List<String> getUsers() {
        return users;
    }

    public void setUsers(List<String> users) {
        this.users = users;
    }
}
