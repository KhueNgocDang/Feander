package com.example.feander;


import com.google.android.gms.maps.model.LatLng;

public class LocationModel {
    private String name;
    private LatLng latLng;
    private String Latitude;
    private String Longitude;

    private LocationModel(){}

    private LocationModel(String name, LatLng latLng, String Latitude, String Longitude)
    {
        this.name = name;
        this.latLng = latLng;
        this.Latitude = Latitude;
        this.Longitude = Longitude;
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
        return Latitude;
    }

    public void setLatitude(String latitude) {
        this.Latitude = latitude;
    }

    public String getLongitude() {
        return Longitude;
    }

    public void setLongitude(String longitude) {
        this.Longitude = longitude;
    }
}
