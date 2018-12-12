package com.example.runningtracker;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.example.runningtracker.model.Run;
import com.example.runningtracker.model.RunDetails;

import java.sql.Timestamp;
import java.util.ArrayList;

import static java.lang.Math.*;

public class Running implements LocationListener {

    private String TAG = "RUNNING";

    public enum RunningState {
        RUNNING,
        PAUSED,
        STOPPED
    }

    private Context mContext;

    private LocationManager locationManager;

    private Handler runHandler = new Handler();

    private RunningState runningState;

    private Run run;
    private ArrayList<RunDetails> rdList;

    private int duration;
    private int distance;
    private int pace;

    private double currLatitude;
    private double currLongitude;

    private boolean firstCall;

    public Running(Context context) {
        mContext = context;

        run = new Run();
        runningState = RunningState.STOPPED;
        firstCall = true;

        duration = distance = 0;
        pace = 0;

        setLocationListener();
    }

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

    public void stopRun() {
        locationManager.removeUpdates(this);
        runningState = RunningState.STOPPED;

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

    public void pauseRun() {
        runningState = RunningState.PAUSED;
    }

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

        if (runningState == RunningState.RUNNING && !firstCall) {
            // manhattan distance
            distance += Config.calculateDistance(currLatitude, currLongitude, newLatitude, newLongitude);

            // average pace in seconds/km
            pace = (int)((duration * 1.0) / (distance * 1.0/1000));

            // add run details
            RunDetails rd = new RunDetails(currLatitude, currLongitude, duration, pace);
            rdList.add(rd);
        }

        firstCall = false;
        currLatitude = newLatitude;
        currLongitude = newLongitude;
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
            if (ActivityCompat.checkSelfPermission(mContext,
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return false;
            }

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

    private Runnable durationHandler = new Runnable() {
        @Override
        public void run() {
            updateDuration();
        }
    };

    private void updateDuration() {
        if (runningState == RunningState.RUNNING) {
            // send broadcast to RunningActivity
            broadcastProgress();
            runHandler.postDelayed(durationHandler, 1000);

            duration++;
        }
    }

    private void broadcastProgress() {
        Intent intent = new Intent();
        intent.setAction(Config.progressFilter);

        Log.i(TAG, Config.duration + " = " + duration);
        Log.i(TAG, Config.distance + " = " + distance);
        Log.i(TAG, Config.pace + " = " + pace);

        intent.putExtra(Config.duration, duration);
        intent.putExtra(Config.distance, distance);
        intent.putExtra(Config.pace, pace);

        mContext.sendBroadcast(intent);
    }
}
