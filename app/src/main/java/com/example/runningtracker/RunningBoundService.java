package com.example.runningtracker;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.widget.Toast;

import com.example.runningtracker.model.Run;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.sql.Date;

public class RunningBoundService extends Service {

    private final IBinder myBinder = new MyLocalBinder();

    private NotificationCompat.Builder mBuilder;
    private NotificationManagerCompat notificationManager;

    private String TAG = "RunningBoundService";

    private Run run;
    private LocationManager locationManager;
    private LocationListener locationListener;

    private boolean isRunning = false;

    public RunningBoundService() {
    }

    public boolean startRun() {
        // current time (when running starts)
        Timestamp now = new Timestamp(System.currentTimeMillis());
        run = new Run(now);

        boolean isListenerSet = setLocationListener();
        if (isListenerSet) {
            isRunning = true;
            return true;
        } else {
            isRunning = false;
            return false;
        }
    }

    public void stopRun() {
        // time when the run ends
        Timestamp now = new Timestamp(System.currentTimeMillis());
        run.setEnd(now);

        isRunning = false;
    }

    public void pauseRun() {

    }

    private boolean setLocationListener() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = new MyLocationListener();

        try {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                Toast.makeText(RunningBoundService.this,
                        "No Location Permission", Toast.LENGTH_SHORT).show();
                return false;
            }
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    Config.timeInterval,
                    Config.distInterval,
                    locationListener);
        } catch (SecurityException e) {
            Log.d("g53mdp", e.toString());
            return false;
        }

        return true;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
    }

    public class MyLocalBinder extends Binder {
        RunningBoundService getService() {
            return RunningBoundService.this;
        }
    }
}
