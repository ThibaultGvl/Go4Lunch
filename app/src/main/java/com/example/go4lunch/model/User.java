package com.example.go4lunch.model;

public class User {

    private String user;

    private long picture;

    private Restaurant restaurant;

    public User(String user, long picture, Restaurant restaurant) {
        this.user = user;
        this.picture = picture;
        this.restaurant = restaurant;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public long getPicture() {
        return picture;
    }

    public void setPicture(long picture) {
        this.picture = picture;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }
}
