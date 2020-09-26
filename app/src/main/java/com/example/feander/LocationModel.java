package com.example.feander;


import com.google.android.gms.maps.model.LatLng;

public class LocationModel {
    private String name;
    private LatLng latLng;
    private String latitude;
    private String longitude;

    private LocationModel(){}

    private LocationModel(String name, LatLng latLng, String latitude, String longitude)
    {
        this.name = name;
        this.latLng = latLng;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}
