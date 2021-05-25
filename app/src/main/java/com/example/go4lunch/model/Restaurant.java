package com.example.go4lunch.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.sql.Time;

public class Restaurant {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("address")
    @Expose
    private String address;

    @SerializedName("distance")
    @Expose
    private int distance;

    @SerializedName("rank")
    @Expose
    private int rank;

    @SerializedName("mate")
    @Expose
    private int mate;

    @SerializedName("picture")
    @Expose
    private long picture;

    @SerializedName("schedules")
    @Expose
    private String schedules;


    public Restaurant(String name, String address, int distance, int rank,int mate, long picture, String schedules) {
        this.name = name;
        this.address = address;
        this.distance = distance;
        this.rank = rank;
        this.mate = mate;
        this.picture = picture;
        this.schedules = schedules;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public int getMate() {
        return mate;
    }

    public void setMate(int mate) {
        this.mate = mate;
    }

    public long getPicture() {
        return picture;
    }

    public void setPicture(long picture) {
        this.picture = picture;
    }

    public String getSchedules() {
        return schedules;
    }

    public void setSchedules(String schedules) {
        this.schedules = schedules;
    }
}
