package com.example.feander;

import com.google.android.gms.maps.model.LatLng;

public class LocationModel {
    private String name;
    private double Latitude;
    private double Longtitude;
    private com.google.android.gms.maps.model.LatLng latLng;
    private boolean isTeaShop;

    private LocationModel(){}

    private LocationModel(String name, LatLng latLng, boolean isTeaShop )
    {
        this.name = name;
        this.latLng = latLng;
        this.isTeaShop = isTeaShop;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isTeaShop() {
        return isTeaShop;
    }

    public void setTeaShop(boolean teaShop) {
        isTeaShop = teaShop;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }
}
