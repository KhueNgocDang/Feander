package com.example.feander.Location;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.GeoPoint;

public class LocationModel implements Parcelable {
    private String name;
    private GeoPoint latLng;
    private String location;
    private String desc;

    public LocationModel(){
    }

    public LocationModel(String name, GeoPoint latLng, String location, String desc)
    {
        this.name = name;
        this.latLng = latLng;
        this.location = location;
        this.desc = desc;
    }

    public LocationModel(Parcel in) {
        name = in.readString();
        double lat = in.readDouble();
        double lng = in.readDouble();
        latLng = new GeoPoint(lat,lng);
        location = in.readString();
        desc = in.readString();
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
        dest.writeString(location);
        dest.writeString(desc);
        dest.writeDouble(latLng.getLatitude());
        dest.writeDouble(latLng.getLongitude());
    }
}
