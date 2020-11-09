package com.example.feander.Location;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.GeoPoint;

public class LocationModel implements Parcelable {
    private String name;
    private GeoPoint latLng;
    private String location;
    private String desc;
    private double distance;

    public LocationModel(){
    }

    public LocationModel(String name, GeoPoint latLng, String location, String desc, double distance)
    {
        this.name = name;
        this.latLng = latLng;
        this.location = location;
        this.desc = desc;
        this.distance =distance;
    }

    public LocationModel(Parcel in) {
        name = in.readString();
        double lat = in.readDouble();
        double lng = in.readDouble();
        latLng = new GeoPoint(lat,lng);
        location = in.readString();
        desc = in.readString();
        distance = in.readDouble();
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

    public static final Creator<LocationModel> CREATOR = new Creator<LocationModel>() {
        @Override
        public LocationModel createFromParcel(Parcel in) {
            return new LocationModel(in);
        }
        @Override
        public LocationModel[] newArray(int size) {
            return new LocationModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeDouble(latLng.getLatitude());
        dest.writeDouble(latLng.getLongitude());
        dest.writeString(location);
        dest.writeString(desc);
        dest.writeDouble(distance);
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(LatLng curr_latLng) {
        //private double distance(double lat1, double lon1, double lat2, double lon2) {
        double lat1 = curr_latLng.latitude;
        double lon1 = curr_latLng.longitude;
        double lat2 = this.getLatLng().getLatitude();
        double lon2 = this.getLatLng().getLongitude();
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                    * Math.sin(deg2rad(lat2))
                    + Math.cos(deg2rad(lat1))
                    * Math.cos(deg2rad(lat2))
                    * Math.cos(deg2rad(theta));
            dist = Math.acos(dist);
            dist = rad2deg(dist);
            dist = dist * 60 * 1.1515 * 1.609344 *1000;
        this.distance = dist;
        }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }
}
