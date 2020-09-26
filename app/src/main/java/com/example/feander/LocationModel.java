package com.example.feander;


public class LocationModel {
    private String name;

    private LocationModel(){}

    private LocationModel(String name)
    {
        this.name = name;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
