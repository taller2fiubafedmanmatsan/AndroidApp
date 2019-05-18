package com.taller2.droidclient.model;

import java.util.ArrayList;
import java.util.List;

public class Admins {

    private List<String> admins;

    public Admins(String user){
        this.admins = new ArrayList<>();
        admins.add(user);
    }

    public Admins(List<String> admins) {
        this.admins = admins;
    }

    public List<String> getAdmins() {
        return admins;
    }

    public void setAdmins(List<String> admins) {
        this.admins = admins;
    }
}
