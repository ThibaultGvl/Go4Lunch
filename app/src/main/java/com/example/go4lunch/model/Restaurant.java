package com.example.go4lunch.model;

import java.sql.Time;

public class Restaurant {

    private String name;

    private String address;

    private int distance;

    private int rank;

    private int mate;

    private long picture;

    private Time schedules;


    public Restaurant(String name, String type, String address, int distance, int rank,int mate, long picture, Time schedules) {

        this.name = name;
        this.address = address;
        this.distance = distance;
        this.rank = rank;
        this.mate = mate;
        this.picture = picture;
        this.schedules =schedules;
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

    public Time getSchedules() {
        return schedules;
    }

    public void setSchedules(Time schedules) {
        this.schedules = schedules;
    }
}
