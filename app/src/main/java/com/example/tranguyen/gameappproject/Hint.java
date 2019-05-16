package com.example.tranguyen.gameappproject;

import java.io.Serializable;

public class Hint implements Serializable {
    private int id;
    //private int position;
    private double latitude;
    private double longitude;
    private String text;

    public Hint(int id, double latitude, double longitude, String text){
        this.id = id;
       // this.position = position;
        this.latitude = latitude;
        this.longitude = longitude;
        this.id = id;
        this.text = text;

    }

    public int getId() {
        return id;
    }
    //public int getPosition(){return position;}
    public double getLatitude(){return latitude;}
    public double getLongitude(){return longitude;}
    public String getText() {
        return text;
    }
}
