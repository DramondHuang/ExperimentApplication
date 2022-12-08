package com.jnu.myapplication;

import java.io.Serializable;

public class Coordinate implements Serializable {
    public String name;
    public double latitude;
    public double longitude;
    public Coordinate(String n, double lat,double longi){
        name=n;
        latitude=lat;
        longitude=longi;
    }
}
