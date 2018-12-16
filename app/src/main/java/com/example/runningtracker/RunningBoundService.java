package com.example.runningtracker;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

public class RunningBoundService extends Service {

    private final IBinder myBinder = new MyLocalBinder();

    private NotificationCompat.Builder mBuilder;
    private NotificationManagerCompat notificationManager;

    private String TAG = "RunningBoundService";

    private Running running;

    public RunningBoundService() {
        Log.i(TAG, "Constructor Called");
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG, "onBind called");
        running = new Running(RunningBoundService.this);
        return myBinder;
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

    // ---------------------------- Functionality Specific Methods ------------------------------

    public Running.RunningState getRunningState() {
        return running.getRunningState();
    }

    public void startRun() {
        running.startRun();
    }

    public void stopRun() {
        running.stopRun();
        stopSelf();
    }

    public void pauseRun() {
        running.pauseRun();
    }

    public void continueRun() {
        running.pauseRun();
    }

    public boolean hasMoved() {
        return running.hasMoved();
    }
}
