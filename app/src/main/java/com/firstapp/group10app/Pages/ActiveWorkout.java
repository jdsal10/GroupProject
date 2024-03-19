package com.firstapp.group10app.Pages;

import android.app.FragmentManagerNonConfig;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.*;

import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import androidx.activity.EdgeToEdge;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.widget.ViewPager2;

import com.firstapp.group10app.DB.DatabaseManager;
import com.firstapp.group10app.DB.OnlineDb.OnlineDbHelper;
import com.firstapp.group10app.Other.ActiveExerciseUpdate;
import com.firstapp.group10app.Other.ExerciseAdapter;
import com.firstapp.group10app.Other.Session;
import com.firstapp.group10app.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

public class ActiveWorkout extends AppCompatActivity implements View.OnClickListener {
    TextView timerText;
    int time, clicks, currentPage = 0;
    boolean paused, complete;
    Button pause, resume, finish, next, previous;
    Runnable timerRunnable;
    JSONArray exerciseArray;
    List<Fragment> fragmentList = new ArrayList<>();

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

        // Nav buttons
        next = findViewById(R.id.nextExercise);
        next.setOnClickListener(this);

        previous = findViewById(R.id.previousExercise);
        previous.setOnClickListener(this);

        try {
            showWorkouts(Session.getSelectedWorkout());
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        // Starts the timer.
        startTimer();

        // Creates the initial exercise.
        generateExercisePages(exerciseArray);
        changePage(0);

        // Since its the first exercise, set visibility to GONE.
        previous.setVisibility(View.GONE);
    }


    public void generateExercisePages(JSONArray list) {
        for (int i = 0; i < list.length(); i++) {
            try {
                Fragment f1;

                JSONObject exercise = exerciseArray.getJSONObject(i);
                System.out.println("DATA: " + exercise);

                String sets;
                String reps;
                String time;

                if (exercise.getString("Sets").equals("null")) {
                    sets = "null";
                } else {
                    sets = exercise.getString("Sets");
                }

                if (exercise.getString("Reps").equals("null")) {
                    reps = "null";
                } else {
                    reps = exercise.getString("Reps");
                }

                if (exercise.getString("Time").equals("null")) {
                    time = "null";
                } else {
                    time = exercise.getString("Time");
                }

                f1 = ActiveExerciseUpdate.newInstance(
                        exercise.getString("Description"),
                        sets,
                        reps,
                        time
                );

                if(f1 == null) {
                    System.out.println("IT NULL");
                }

                fragmentList.add(f1);
//                FragmentManager fragmentManager = getSupportFragmentManager();
//                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                fragmentTransaction.replace(R.id.viewPager, f1);
//                fragmentTransaction.addToBackStack(null);
//                fragmentTransaction.commit();
                System.out.println("ADDED");

            } catch (JSONException e) {
                throw new RuntimeException(e);

            }
        }
    }

    public void changePage(int index) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.viewPager, fragmentList.get(index));
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
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
        if (id == R.id.nextExercise) {
            currentPage++;
            changePage(currentPage);
            if(currentPage == fragmentList.size()) {
                next.setVisibility(View.GONE);
                previous.setVisibility(View.VISIBLE);
            }
        } else if (id == R.id.previousExercise) {
            currentPage--;
            changePage(currentPage);
        } else if (id == R.id.pauseWorkout) {
            paused = true;
            flashPaused();
            findViewById(R.id.pauseWorkout).setVisibility(View.GONE);

            findViewById(R.id.resumeWorkout).setVisibility(View.VISIBLE);
            findViewById(R.id.finishWorkout).setVisibility(View.VISIBLE);

            next.setClickable(false);
            previous.setClickable(false);
        } else if (id == R.id.resumeWorkout) {
            paused = false;

            next.setClickable(true);
            previous.setClickable(true);
            findViewById(R.id.pauseWorkout).setVisibility(View.VISIBLE);

            findViewById(R.id.resumeWorkout).setVisibility(View.GONE);
            findViewById(R.id.finishWorkout).setVisibility(View.GONE);
        } else if (id == R.id.finishWorkout) {
            if (complete) {
                // Do code when workout is finished
                Toast.makeText(this, "FINISH", Toast.LENGTH_LONG).show();
            } else {
                /// Do code when workout isn't (inform user, ask for double click etc).
                if (clicks < 2) {
                    Toast.makeText(this, "You have not finished your workout! Click again to confirm!", Toast.LENGTH_LONG).show();
                    clicks++;
                } else {
                    // Add workout to database.
                    int minutes = (time % 3600) / 60;
                    OnlineDbHelper.insertHistory(minutes);

                }

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