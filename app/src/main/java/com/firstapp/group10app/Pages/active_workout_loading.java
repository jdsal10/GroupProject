package com.firstapp.group10app.Pages;

import android.content.Intent;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.firstapp.group10app.R;

import java.util.Timer;
import java.util.TimerTask;

public class active_workout_loading extends AppCompatActivity {
    Timer countdownClock;
    TextView countdownText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        countdownText = findViewById(R.id.workoutCountdown);

        setContentView(R.layout.activity_active_workout_loading);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.workoutCountdown), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        startCountdown();
    }

    public void startCountdown() {
        countdownText = findViewById(R.id.workoutCountdown);
        countdownClock = new Timer();
        TimerTask setTwo = new TimerTask() {
            @Override
            public void run() {
                countdownText.setText("2");
                vibrate(100);
            }
        };

        TimerTask setOne = new TimerTask() {
            @Override
            public void run() {
                countdownText.setText("1");
                vibrate(100);
            }
        };

        TimerTask go = new TimerTask() {
            @Override
            public void run() {
                countdownText.setText("Go!");
                vibrate(1000);
            }
        };


        TimerTask startWorkout = new TimerTask() {
            @Override
            public void run() {
                startActivity(new Intent(getBaseContext(), active_workout.class));
            }
        };

        countdownClock.schedule(setTwo, 1000);
        countdownClock.schedule(setOne, 2000);
        countdownClock.schedule(go, 3000);
        countdownClock.schedule(startWorkout, 3500);
    }

    private void vibrate(int duration) {
        ((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(VibrationEffect.createOneShot(duration, VibrationEffect.DEFAULT_AMPLITUDE));
    }
}