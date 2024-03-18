package com.firstapp.group10app.Pages;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
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
import androidx.fragment.app.FragmentContainerView;

import com.firstapp.group10app.Other.ActiveExerciseUpdate;
import com.firstapp.group10app.Other.Session;
import com.firstapp.group10app.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class active_workout extends AppCompatActivity implements View.OnClickListener {
    TextView timerText;
    int time = 0;
    boolean paused;
    boolean complete;
    Button pause, resume, finish;
    Runnable timerRunnable;
    int currentExerciseIndex = 0;
    FragmentContainerView activeView;

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

        activeView = findViewById(R.id.activeView);
        activeView.setOnTouchListener(new OnSwipeTouchLisqtener(MainActivity.this) {
            @Override
            public void onSwipeLeft(View v, MotionEvent event) {
                return false;
            }
        });
    }

    public void showWorkouts(JSONObject currentWorkout) throws JSONException {
        String exerciseString = currentWorkout.optString("Exercises");

        JSONArray exerciseArray;
        try {
            exerciseArray = new JSONArray(exerciseString);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        showExerciseDetails(exerciseArray.getJSONObject(currentExerciseIndex));
    }

    public void showExerciseDetails(JSONObject exercise) throws JSONException {
        String description = exercise.getString("Description");
        String sets = exercise.getString("Sets");
        String reps = exercise.getString("Reps");
        String time = exercise.getString("Time");

        ActiveExerciseUpdate fragment = ActiveExerciseUpdate.newInstance(description, sets, reps, time);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.activeView, fragment)
                .commit();
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
        TimerTask setPaused = new TimerTask() {
            @Override
            public void run() {
                timerText = findViewById(R.id.timer);
                timerText.setText("Paused");
            }
        };

        TimerTask setTime = new TimerTask() {
            @Override
            public void run() {
                timerText = findViewById(R.id.timer);
                int hours = time / 3600;
                int minutes = (time % 3600) / 60;
                int secs = time % 60;

                timerText.setText(String.format(Locale.ENGLISH, "%d:%02d:%02d", hours, minutes, secs));
            }
        };

        timer.schedule(setPaused, 1000, 1000);
        timer.schedule(setTime, 2000, 2000);
    }
}
