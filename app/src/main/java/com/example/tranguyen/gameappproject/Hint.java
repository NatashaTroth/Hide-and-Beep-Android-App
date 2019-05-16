package com.example.tranguyen.gameappproject;

public class Hint {
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
