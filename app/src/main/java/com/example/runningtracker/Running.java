package com.example.runningtracker;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.example.runningtracker.model.Run;
import com.example.runningtracker.model.RunDetails;

import java.sql.Timestamp;
import java.util.ArrayList;

public class Running implements LocationListener {

    private String TAG = "RUNNING";

    public enum RunningState {
        RUNNING,
        PAUSED,
        STOPPED
    }

    private RunningState runningState;

    private Context mContext;
    private LocationManager locationManager;
    private Handler runHandler = new Handler();

    private Run run;
    private ArrayList<RunDetails> rdList;

    private int duration;
    private int distance;
    private int pace;

    private Location currLocation;

    public Running(Context context) {
        mContext = context;

        run = new Run();
        runningState = RunningState.STOPPED;

        duration = distance = 0;
        pace = 0;

        setLocationListener();
    }

    // user starts run
    public void startRun() {
        // instantiate new Run object, will later be used to store into DB
        Timestamp now = new Timestamp(System.currentTimeMillis());
        run.setStart(now);

        // new arraylist to store every second
        rdList = new ArrayList<RunDetails>();

        // change running state and start updating duration
        runningState = RunningState.RUNNING;
        updateDuration();
    }

    // user stops run
    public void stopRun() {
        locationManager.removeUpdates(this);
        runningState = RunningState.STOPPED;

        if (!rdList.isEmpty()) {
            Timestamp now = new Timestamp(System.currentTimeMillis());
            run.setEnd(now);
            run.setPace(pace);
            run.setDuration(duration);
            run.setDistance(distance);

            // save Run and RunDetails ArrayList to database
            DBHandler dbHandler = new DBHandler(mContext, null, null,
                    MyContract.DATABASE_VERSION);

            int id = dbHandler.addRun(run);
            dbHandler.addRunDetails(rdList, id);
        }
    }

    // user pauses run
    public void pauseRun() {
        runningState = RunningState.PAUSED;
    }

    // user continues run
    public void continueRun() {
        runningState = RunningState.RUNNING;
        updateDuration();
    }

    public RunningState getRunningState() {
        return this.runningState;
    }

    @Override
    public void onLocationChanged(Location location) {
        double newLatitude = location.getLatitude();
        double newLongitude = location.getLongitude();

        Log.i(TAG, "Latitude = " + newLatitude);
        Log.i(TAG, "Longitude = " + newLongitude);

        // if user is currently running and it is not the first coordinate
        // calculate the distance and pace
        if (runningState == RunningState.RUNNING && (currLocation != null)) {
            // Haversine Formula
            distance += Config.calculateDistance(currLocation.getLatitude(), currLocation.getLongitude()
                    , newLatitude, newLongitude);

            // average pace in seconds/km
            pace = (int)((duration * 1.0) / (distance * 1.0/1000));

            // add run details
            RunDetails rd = new RunDetails(newLatitude, newLongitude, duration);
            rdList.add(rd);
        }

        // replace the previous coordinate with current coordinate
        currLocation = location;
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    private boolean setLocationListener() {
        locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);

        try {
            // retrieve update every determined time and distance interval
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    Config.timeInterval,
                    Config.distInterval,
                    this);
        } catch (SecurityException e) {
            Log.d(TAG, e.toString());
            return false;
        }

        return true;
    }

    // runs to update duration
    private Runnable durationHandler = new Runnable() {
        @Override
        public void run() {
            updateDuration();
        }
    };

    // logic to update duration
    private void updateDuration() {
        if (runningState == RunningState.RUNNING) {
            // send broadcast to RunningActivity
            broadcastProgress();
            runHandler.postDelayed(durationHandler, 1000);

            duration++;
        }
    }

    // send progress to RunningActivity
    private void broadcastProgress() {
        Intent intent = new Intent();
        intent.setAction(Config.progressFilter);

        Log.i(TAG, Config.duration + " = " + duration);
        Log.i(TAG, Config.distance + " = " + distance);
        Log.i(TAG, Config.pace + " = " + pace);

        // put data
        intent.putExtra(Config.duration, duration);
        intent.putExtra(Config.distance, distance);
        intent.putExtra(Config.pace, pace);

        mContext.sendBroadcast(intent);
    }

    public boolean hasMoved() {
        return (!rdList.isEmpty());
    }
}
