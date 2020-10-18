package com.example.feander;

import com.google.firebase.firestore.GeoPoint;

public class LocationModel {
    private String name;
    private GeoPoint latLng;
    private String location;
    private String desc;

    private LocationModel(){
    }

    private LocationModel(String name, GeoPoint latLng,  String location, String desc)
    {
        this.name = name;
        this.latLng = latLng;
        this.location = location;
        this.desc = desc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public GeoPoint getLatLng() {
        return latLng;
    }

    public void setLatLng(GeoPoint latLng) {
        this.latLng = latLng;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
