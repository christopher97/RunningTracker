package com.example.runningtracker.model;

import java.sql.Timestamp;

public class Run {

    private int id;
    private Timestamp start;
    private Timestamp end;
    private int distance;
    private int duration;
    private double pace;

    public Run() {
    }

    public Run(Timestamp start) {
        this.start = start;
        this.distance = 0;
        this.duration = 0;
        this.pace = 0;
    }

    public Run(int id, int duration, double pace, Timestamp start, Timestamp end, int distance) {
        this.id = id;
        this.duration = duration;
        this.pace = pace;
        this.start = start;
        this.end = end;
        this.distance = distance;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public double getPace() {
        return pace;
    }

    public void setPace(double pace) {
        this.pace = pace;
    }

    public Timestamp getStart() {
        return start;
    }

    public void setStart(Timestamp start) {
        this.start = start;
    }

    public Timestamp getEnd() {
        return end;
    }

    public void setEnd(Timestamp end) {
        this.end = end;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }
}
