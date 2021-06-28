package com.example.go4lunch.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class User {

    @SerializedName("uid")
    @Expose
    private String uid;

    @SerializedName("username")
    @Expose
    private String username;

    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("picture")
    @Expose
    private String picture;

    @SerializedName("restaurant")
    @Expose
    private String restaurantId;

    @SerializedName("restaurantsLiked")
    @Expose
    private List<String> restaurantsLiked;

    @SerializedName("restaurantName")
    @Expose
    private String restaurantName;

    @SerializedName("restaurantAddress")
    @Expose
    private String restaurantAddress;

    public User() { }

    public User(String uid, String username, String email, String picture, String restaurantId, List<String> restaurantsLiked, String restaurantName, String restaurantAddress) {
        this.uid = uid;
        this.username = username;
        this.email = email;
        this.picture = picture;
        this.restaurantId = restaurantId;
        this.restaurantsLiked = restaurantsLiked;
        this.restaurantName = restaurantName;
        this.restaurantAddress = restaurantAddress;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getRestaurant() {
        return restaurantId;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }

    public List<String> getRestaurantsLiked() {
        return restaurantsLiked;
    }

    public void addRestaurantLike(String restaurantToLike) {
        restaurantsLiked.add(restaurantToLike);
    }

    public void deleteRestaurantLike(String restaurantToDislike) {
        restaurantsLiked.remove(restaurantToDislike);
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public String getRestaurantAddress() {
        return restaurantAddress;
    }

    public void setRestaurantAddress(String restaurantAddress) {
        this.restaurantAddress = restaurantAddress;
    }
}
