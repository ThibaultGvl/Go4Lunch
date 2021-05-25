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

    @SerializedName("restaurant_id")
    @Expose
    private String restaurantId;

    @SerializedName("restaurants_liked")
    @Expose
    private List<String> restaurantsLiked;

    public User() { }

    public User(String uid, String username, String email, String picture, String restaurantId, List<String> restaurantsLiked) {
        this.uid = uid;
        this.username = username;
        this.email = email;
        this.picture = picture;
        this.restaurantId = restaurantId;
        this.restaurantsLiked = restaurantsLiked;
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
}
