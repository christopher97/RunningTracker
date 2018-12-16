package com.example.runningtracker.model;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import java.sql.Timestamp;

public class Run implements Parcelable {

    private int id;
    private Timestamp start;
    private Timestamp end;
    private int distance;
    private int duration;
    private int pace;

    public Run() {
    }

    public Run(Parcel parcel) {
        Bundle bundle = parcel.readBundle();

        this.id = bundle.getInt("id");
        this.start = new Timestamp(bundle.getLong("start"));
        this.end = new Timestamp(bundle.getLong("end"));
        this.distance = bundle.getInt("distance");
        this.duration = bundle.getInt("duration");
        this.pace = bundle.getInt("pace");
    }

    public Run(Timestamp start) {
        this.start = start;
        this.distance = 0;
        this.duration = 0;
        this.pace = 0;
    }

    public Run(int id, int duration, int pace, Timestamp start, Timestamp end, int distance) {
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

    public int getPace() {
        return pace;
    }

    public void setPace(int pace) {
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        Bundle bundle = new Bundle();
        bundle.putInt("id", id);
        bundle.putLong("start", start.getTime());
        bundle.putLong("end", end.getTime());
        bundle.putInt("distance", distance);
        bundle.putInt("duration", duration);
        bundle.putInt("pace", pace);

        parcel.writeBundle(bundle);
    }

    public static final Creator<Run> CREATOR = new Creator<Run>() {
        @Override
        public Run[] newArray(int i) { return new Run[i]; }

        @Override
        public Run createFromParcel(Parcel parcel) { return new Run(parcel); }
    };
}
