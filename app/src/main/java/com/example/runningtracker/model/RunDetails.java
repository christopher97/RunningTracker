package com.example.runningtracker.model;

public class RunDetails {

    private int id;
    private int runID;
    private double lat; // latitude
    private double lon; // longitude
    private int time;   // seconds elapsed since start of run

    public RunDetails() {
    }

    public RunDetails(double lat, double lon, int time) {
        this.lat = lat;
        this.lon = lon;
        this.time = time;
    }

    public RunDetails(int runID, double lat, double lon, int time) {
        this.runID = runID;
        this.lat = lat;
        this.lon = lon;
        this.time = time;
    }

    public RunDetails(int id, int runID, double lat, double lon, int time) {
        this.id = id;
        this.runID = runID;
        this.lat = lat;
        this.lon = lon;
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRunID() {
        return runID;
    }

    public void setRunID(int runID) {
        this.runID = runID;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }
}
