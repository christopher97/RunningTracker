package com.example.runningtracker;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class CountdownActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_countdown);

        countdown();
    }

    private void countdown() {
        final TextView number = findViewById(R.id.numberText);
        int counter = 3;
        new CountDownTimer(counter * 1000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                long seconds = 1 + millisUntilFinished/1000;
                number.setText(String.valueOf(seconds));
            }

            @Override
            public void onFinish() {
                Intent i = new Intent(CountdownActivity.this, RunningActivity.class);
                startActivity(i);
                finish();
            }
        }.start();
    }
}
