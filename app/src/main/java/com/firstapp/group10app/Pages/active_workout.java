package com.firstapp.group10app.Pages;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import androidx.activity.EdgeToEdge;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.firstapp.group10app.Other.ExerciseAdapter;
import com.firstapp.group10app.Other.Session;
import com.firstapp.group10app.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class active_workout extends AppCompatActivity implements View.OnClickListener {
    TextView timerText;
    int time = 0;
    boolean paused, complete;
    Button pause, resume, finish;
    Runnable timerRunnable;
    LinearLayout activeView;
    JSONArray exerciseArray;


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

        pause = findViewById(R.id.pauseWorkout);
        pause.setOnClickListener(this);

        resume = findViewById(R.id.resumeWorkout);
        resume.setOnClickListener(this);

        finish = findViewById(R.id.finishWorkout);
        finish.setOnClickListener(this);

        try {
            showWorkouts(Session.getSelectedWorkout());
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        startTimer();

        ViewPager2 viewPager = findViewById(R.id.viewPager);
        ExerciseAdapter pagerAdapter = new ExerciseAdapter(this, exerciseArray);
        viewPager.setAdapter(pagerAdapter);

    }

    public void showWorkouts(JSONObject currentWorkout) throws JSONException {
        String exerciseString = currentWorkout.optString("Exercises");

        try {
            exerciseArray = new JSONArray(exerciseString);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public void startTimer() {
        timerText = findViewById(R.id.timer);
        timerRunnable = new Runnable() {
            @Override
            public void run() {
                int hours = time / 3600;
                int minutes = (time % 3600) / 60;
                int secs = time % 60;

                timerText.setText(String.format(Locale.ENGLISH, "%d:%02d:%02d", hours, minutes, secs));

                if (!paused) {
                    time++;
                }

                timerText.postDelayed(this, 1000);
            }
        };

        timerText.post(timerRunnable);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.pauseWorkout) {
            paused = true;
            flashPaused();
            findViewById(R.id.pauseWorkout).setVisibility(View.GONE);

            findViewById(R.id.resumeWorkout).setVisibility(View.VISIBLE);
            findViewById(R.id.finishWorkout).setVisibility(View.VISIBLE);
        } else if (id == R.id.resumeWorkout) {
            paused = false;
            findViewById(R.id.pauseWorkout).setVisibility(View.VISIBLE);

            findViewById(R.id.resumeWorkout).setVisibility(View.GONE);
            findViewById(R.id.finishWorkout).setVisibility(View.GONE);
        } else if (id == R.id.finishWorkout) {
            if (complete) {
                // Do code when workout is finished
                Toast.makeText(this, "FINISH", Toast.LENGTH_LONG).show();
            } else {
                /// Do code when workout isn't (inform user, ask for double click etc).
                Toast.makeText(this, "NOT FINISH", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void flashPaused() {
        Timer timer = new Timer();
        TimerTask flashTask = new TimerTask() {
            boolean pausedBoolean = true;

            @Override
            public void run() {
                runOnUiThread(() -> {
                    timerText = findViewById(R.id.timer);
                    if (pausedBoolean) {
                        timerText.setText("Paused");
                    } else {
                        int hours = time / 3600;
                        int minutes = (time % 3600) / 60;
                        int secs = time % 60;
                        timerText.setText(String.format(Locale.ENGLISH, "%d:%02d:%02d", hours, minutes, secs));
                    }
                    pausedBoolean = !pausedBoolean;
                });
            }
        };

        timer.schedule(flashTask, 1000, 2000);
    }

}
