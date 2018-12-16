package com.example.runningtracker;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class RunningActivity extends AppCompatActivity {

    private ImageButton pauseButton;
    private ImageButton playButton;
    private ImageButton stopButton;

    private TextView durationText;
    private TextView distanceText;
    private TextView paceText;

    private AlertDialog.Builder discardAlertBuilder;
    private String TAG = "RunningActivity";

    private RunningBoundService runningService;
    private boolean isBound = false;

    private Intent serviceIntent;
    private BroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_running);

        createAlertDialog();

        pauseButton = findViewById(R.id.pauseButton);
        playButton = findViewById(R.id.playButton);
        stopButton = findViewById(R.id.stopButton);

        durationText = findViewById(R.id.durationText);
        distanceText = findViewById(R.id.distanceText);
        paceText = findViewById(R.id.paceText);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG,"On Resume");

        // start service if not started yet
        if (serviceIntent == null) {
            serviceIntent = new Intent(this, RunningBoundService.class);
            startService(serviceIntent);
        }

        // if no broadcast receiver, create new one
        if (broadcastReceiver == null) {
            Log.i(TAG, "Broadcast was null");
            setBroadcastReceiver();
        }

        // bind service
        bindService(serviceIntent, myConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "On Pause");

        isBound = false;

        // unbind service to prevent leak
        if (myConnection != null)
            unbindService(myConnection);
        if (broadcastReceiver != null) {
            unregisterReceiver(broadcastReceiver);
            broadcastReceiver = null;
        }
    }

    @Override
    public void onBackPressed() {
        discardAlertBuilder.show();
    }

    // set the filter and register the broadcast receiver
    private void setBroadcastReceiver() {
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Bundle bundle = intent.getExtras();
                updateUI(bundle);
            }
        };

        IntentFilter filter = new IntentFilter();
        filter.addAction(Config.progressFilter);
        registerReceiver(broadcastReceiver, filter);
    }

    // updates the duration, distance, and pace textView values
    private void updateUI(Bundle bundle) {
        int duration = bundle.getInt(Config.duration);
        int distance = bundle.getInt(Config.distance);
        int pace = bundle.getInt(Config.pace);

        String durationString = Config.getDurationString(duration);
        String distanceString = Config.getDistanceString(distance);
        String paceString = Config.getPaceString(pace);

        durationText.setText(durationString);
        distanceText.setText(distanceString);
        paceText.setText(paceString);
    }

    private ServiceConnection myConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            RunningBoundService.MyLocalBinder binder = (RunningBoundService.MyLocalBinder) service;
            runningService = binder.getService();
            isBound = true;

            // if there is a no run in progress, start a new one
            if (runningService.getRunningState() == Running.RunningState.STOPPED) {
                runningService.startRun();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.i(TAG, "Service Disconnected");
            isBound = false;
        }
    };

    // handle pause button click
    public void onPauseButtonPressed(View view) {
        runningService.pauseRun();

        playButton.setVisibility(View.VISIBLE);
        stopButton.setVisibility(View.VISIBLE);
        pauseButton.setVisibility(View.GONE);
    }

    // handle play button click
    public void onPlayButtonPressed(View view) {
        runningService.continueRun();

        playButton.setVisibility(View.GONE);
        stopButton.setVisibility(View.GONE);
        pauseButton.setVisibility(View.VISIBLE);
    }

    // handles stop button click
    public void onStopButtonPressed(View view) {
        if (!runningService.hasMoved()) {
            discardAlertBuilder.show();
        } else {
            runningService.stopRun();
            finish();
        }
    }

    // dialog when user is about to discard run
    private void createAlertDialog() {
        discardAlertBuilder = new AlertDialog.Builder(this);

        discardAlertBuilder.setTitle(Config.discardConfirmation);
        discardAlertBuilder.setMessage(Config.discardMessage);

        // finish activity and unbind service
        discardAlertBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.i(TAG, "clicked Yes");
                runningService.stopRun();
                RunningActivity.super.onBackPressed();
            }
        });

        discardAlertBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
    }
}
