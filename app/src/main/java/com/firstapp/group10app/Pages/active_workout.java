package com.firstapp.group10app.Pages;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.firstapp.group10app.R;

import java.util.Timer;
import java.util.TimerTask;

public class active_workout extends AppCompatActivity {
    TextView timerText;
    Timer duration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_active_workout);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        startTimer();
    }

    public void startTimer() {
        timerText = findViewById(R.id.timer);
        duration = new Timer();

        TimerTask updateTimer = new TimerTask() {
            @Override
            public void run() {
                timerText.setText(duration.toString());
            }
        };

        duration.schedule(updateTimer, 0, 1000/40);
    }
}