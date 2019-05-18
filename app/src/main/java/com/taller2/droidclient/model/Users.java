package com.taller2.droidclient.model;

import java.util.ArrayList;
import java.util.List;

public class Users {

    private List<String> users;

    public Users(String user){
        users = new ArrayList<>();
        users.add(user);
    }

    public Users(List<String> users) {
        this.users = users;
    }

    public List<String> getUsers() {
        return users;
    }

    public void setUsers(List<String> users) {
        this.users = users;
    }
}
