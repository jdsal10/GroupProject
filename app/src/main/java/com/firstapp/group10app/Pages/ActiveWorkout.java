package com.firstapp.group10app.Pages;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.firstapp.group10app.DB.DatabaseManager;
import com.firstapp.group10app.DB.OnlineDb.OnlineDbHelper;
import com.firstapp.group10app.Other.ActiveExerciseUpdate;
import com.firstapp.group10app.Other.Session;
import com.firstapp.group10app.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class ActiveWorkout extends AppCompatActivity implements View.OnClickListener {
    TextView timerText;
    TimerTask flashTask;
    Timer timer;
    int time, currentPage = 0;
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

        // Workout control buttons
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
        changePage(0, true);

        // Disables button if length is 1.
        if (fragmentList.size() == 1) {
            next.setVisibility(View.GONE);
        }

        // Since its the first exercise, set visibility to GONE.
        previous.setVisibility(View.GONE);
    }


    public void generateExercisePages(JSONArray list) {
        for (int i = 0; i < list.length(); i++) {
            try {
                Fragment exerciseData;

                JSONObject exercise = exerciseArray.getJSONObject(i);

                // Handles the case where there is no illustration. (There was a crash in CreateWorkout)
                String illustration = "viz_";
                try {
                    illustration += exercise.getString("Illustration");
                } catch (Exception e) {
                    illustration += exercise.getString("ExerciseName").toLowerCase().replace("-", "_");
                }
                illustration = illustration.replace(" ", "_");
                Log.d("ActiveWorkout", "Exercise: " + exercise.getString("ExerciseName"));
                Log.d("ActiveWorkout", "Illustration: " + illustration);

                exerciseData = ActiveExerciseUpdate.newInstance(
                        exercise.getString("ExerciseName"),
                        exercise.getString("Description"),
                        exercise.getString("Sets"),
                        exercise.getString("Reps"),
                        exercise.getString("Time"),
                        illustration);

                fragmentList.add(exerciseData);

            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void changePage(int index, boolean forward) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (forward) {
            fragmentTransaction.setCustomAnimations(R.anim.slide_right_in, R.anim.slide_left_out);
        } else {
            fragmentTransaction.setCustomAnimations(R.anim.slide_left_in, R.anim.slide_right_out);
        }

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
            changePage(currentPage, true);
            if (currentPage == fragmentList.size() - 1) {
                next.setVisibility(View.GONE);
                previous.setVisibility(View.VISIBLE);
            } else {
                next.setVisibility(View.VISIBLE);
                previous.setVisibility(View.VISIBLE);
            }
        } else if (id == R.id.previousExercise) {
            currentPage--;
            changePage(currentPage, false);

            if (currentPage == 0) {
                next.setVisibility(View.VISIBLE);
                previous.setVisibility(View.GONE);
            } else {
                next.setVisibility(View.VISIBLE);
                previous.setVisibility(View.VISIBLE);
            }
        } else if (id == R.id.pauseWorkout) {
            next.setAlpha(.5f);
            previous.setAlpha(.5f);

            timerText.setTextColor(getResources().getColor(R.color.pastel_red));

            paused = true;
            findViewById(R.id.pauseWorkout).setVisibility(View.GONE);
            findViewById(R.id.resumeWorkout).setVisibility(View.VISIBLE);
            findViewById(R.id.finishWorkout).setVisibility(View.VISIBLE);

            next.setClickable(false);
            previous.setClickable(false);
        } else if (id == R.id.resumeWorkout) {
            paused = false;

            timerText.setTextColor(getResources().getColor(R.color.black));

            next.setClickable(true);
            previous.setClickable(true);

            next.setAlpha(1f);
            previous.setAlpha(1f);

            findViewById(R.id.pauseWorkout).setVisibility(View.VISIBLE);
            findViewById(R.id.resumeWorkout).setVisibility(View.GONE);
            findViewById(R.id.finishWorkout).setVisibility(View.GONE);
        } else if (id == R.id.finishWorkout) {
            complete = currentPage == fragmentList.size() - 1;
            if (complete) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.popup_finish_workout, null);

                builder.setView(dialogView);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();

                TextView title = dialogView.findViewById(R.id.confirmFinishTitle);
                title.setText("You have finished your workout! Confirm below to save!");
                Button cancelFinish = dialogView.findViewById(R.id.cancelWorkoutFinish);
                Button finish = dialogView.findViewById(R.id.confirmWorkoutFinish);

                cancelFinish.setOnClickListener(v1 -> alertDialog.dismiss());

                finish.setOnClickListener(v12 -> {
                    int minutes = (time % 3600) / 60;
                    DatabaseManager.getInstance().insertHistory(minutes);
                    alertDialog.dismiss();
                    startActivity(new Intent(this, ActivityContainer.class));
                });

            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.popup_finish_workout, null);

                builder.setView(dialogView);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();

                TextView title = dialogView.findViewById(R.id.confirmFinishTitle);
                title.setText("You haven't finished your workout! You can still save your progress!");
                Button cancelFinish = dialogView.findViewById(R.id.cancelWorkoutFinish);
                Button finish = dialogView.findViewById(R.id.confirmWorkoutFinish);

                cancelFinish.setOnClickListener(v1 -> alertDialog.dismiss());

                finish.setOnClickListener(v12 -> {
                    int minutes = (time % 3600) / 60;
                    OnlineDbHelper.insertHistory(minutes);
                    alertDialog.dismiss();
                    startActivity(new Intent(this, ActivityContainer.class));


                });
            }

        }
    }
}