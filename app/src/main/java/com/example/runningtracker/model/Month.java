package com.example.runningtracker.model;

import android.util.Log;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.List;

public class Month {

    private int month;
    private int year;
    private List<Run> runs;

    private int totalRun;
    private long totalDistance;
    private long totalDuration;

    public Month(int month, int year) {
        this.month = month;
        this.year = year;
        this.runs = new ArrayList<Run>();

        totalRun = 0;
        totalDuration = totalDistance = 0;
    }

    public void addRun(Run run) {
        runs.add(run);

        totalRun++;
        totalDistance += run.getDistance();
        totalDuration += run.getDuration();

        Log.i("MONTH", "Runs = " + totalRun);
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }

    public int getTotalRun() {
        return totalRun;
    }

    public List<Run> getRuns() {
        return this.runs;
    }

    public String getTotalDistance() {
        Log.i("MONTH", "distance = " + totalDistance);
        return (totalDistance/1000) + "." + (totalDistance%1000/100) + (totalDistance%100/10);
    }

    public String getPace() {
        long avgPace = (long) (1.0 * totalDuration / (totalDistance/1000.0));
        return (avgPace/60) + "\'" + (avgPace%60) + "\"";
    }

    public String getYearMonth() {
        return new DateFormatSymbols().getMonths()[month] + " " + year;
    }
}
