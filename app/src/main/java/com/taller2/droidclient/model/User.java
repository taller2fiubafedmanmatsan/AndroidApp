package com.taller2.droidclient.model;

public class User {
    private String id;
    private String username;
    private String imageURL;

    public User() {
        //Empty for firebase DataSnapshot.getValue(User.class)
    }

    public User(String id, String username, String imageURL) {
        this.id = id;
        this.username = username;
        this.imageURL = imageURL;
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getImageURL() {
        return imageURL;
    }
}
