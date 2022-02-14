package com.example.openskyproject;


/**
 * The class obtains three arguments from the API — icso24, latitude and longitude.
 */
public class Flights {

    private String icao24;
    protected double longitude;
    protected double latitude;


    public Flights(double longitude, double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;

    }

    public Flights(String icao24, double longitude, double latitude) {
        this.icao24 = icao24;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public Flights() {
    }
    public String getIcao24() {
        return icao24;
    }

    public void setIcao24(String icao24) {
        this.icao24 = icao24;
    }
    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    @Override
    public String toString() {
        return "states{" +
                "longitude=" + longitude +
                ", latitude=" + latitude +
                '}';
    }
}
