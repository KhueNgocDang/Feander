package com.example.feander;


import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.GeoPoint;

public class LocationModel {
    private String name;
    private GeoPoint latLng;
    private String Latitude;
    private String Longitude;
    private String location;

    private LocationModel(){
    }

    private LocationModel(String name, GeoPoint latLng, String Latitude, String Longitude, String location)
    {
        this.name = name;
        this.latLng = latLng;
        this.Latitude = Latitude;
        this.Longitude = Longitude;
        this.location = location;
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
