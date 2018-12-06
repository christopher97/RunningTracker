package com.example.runningtracker;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class RunningActivity extends AppCompatActivity {

    private ImageButton pauseButton;
    private ImageButton playButton;
    private ImageButton stopButton;

    private AlertDialog.Builder alertDialogBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_running);

        createAlertDialog();

        pauseButton = findViewById(R.id.pauseButton);
        playButton = findViewById(R.id.playButton);
        stopButton = findViewById(R.id.stopButton);
    }

    public void onPauseButtonPressed(View view) {
        playButton.setVisibility(View.VISIBLE);
        stopButton.setVisibility(View.VISIBLE);
        pauseButton.setVisibility(View.GONE);
    }

    public void onPlayButtonPressed(View view) {
        playButton.setVisibility(View.GONE);
        stopButton.setVisibility(View.GONE);
        pauseButton.setVisibility(View.VISIBLE);
    }

    public void onStopButtonPressed(View view) {
        alertDialogBuilder.show();
    }

    private void createAlertDialog() {
        alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder.setTitle(Config.discardConfirmation);
        alertDialogBuilder.setMessage(Config.discardMessage);

        // finish activity and unbind service
        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
    }
}
