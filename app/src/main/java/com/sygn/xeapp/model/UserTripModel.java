package com.sygn.xeapp.model;

public class UserTripModel {

    private String tripLoc;
    private int tripStatusIcon;
    private String tripDesLoc;
    private String tripTime;
    private String tripPrice;
    private String tripStatus;

    public UserTripModel(String tripLoc, int tripStatusIcon, String tripDesLoc, String tripTime, String tripPrice, String tripStatus) {
        this.tripLoc = tripLoc;
        this.tripStatusIcon = tripStatusIcon;
        this.tripDesLoc = tripDesLoc;
        this.tripTime = tripTime;
        this.tripPrice = tripPrice;
        this.tripStatus = tripStatus;
    }

    public String getTripLoc() {
        return tripLoc;
    }

    public void setTripLoc(String tripLoc) {
        this.tripLoc = tripLoc;
    }

    public int getTripStatusIcon() {
        return tripStatusIcon;
    }

    public void setTripStatusIcon(int tripStatusIcon) {
        this.tripStatusIcon = tripStatusIcon;
    }

    public String getTripDesLoc() {
        return tripDesLoc;
    }

    public void setTripDesLoc(String tripDesLoc) {
        this.tripDesLoc = tripDesLoc;
    }

    public String getTripTime() {
        return tripTime;
    }

    public void setTripTime(String tripTime) {
        this.tripTime = tripTime;
    }

    public String getTripPrice() {
        return tripPrice;
    }

    public void setTripPrice(String tripPrice) {
        this.tripPrice = tripPrice;
    }

    public String getTripStatus() {
        return tripStatus;
    }

    public void setTripStatus(String tripStatus) {
        this.tripStatus = tripStatus;
    }
}
