package com.firstapp.group10app.Pages;


import androidx.appcompat.app.AppCompatActivity;

import com.firstapp.group10app.DB.DBHelper;
import com.firstapp.group10app.Other.ItemVisualiser;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.firstapp.group10app.Other.onlineChecks;
import com.firstapp.group10app.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import org.json.JSONException;

import java.util.ArrayList;

public class searchWorkout extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener, View.OnClickListener {
    private LinearLayout workoutLayout;
    String duration, difficulty, target;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_workout);
        Intent intent = getIntent();
        if (intent != null && getIntent().hasExtra("duration") && getIntent().hasExtra("difficulty") && getIntent().hasExtra("targetMuscle")) {
            String durationString = getIntent().getStringExtra("duration");
            String difficultyString = getIntent().getStringExtra("difficulty");
            String targetMuscleString = getIntent().getStringExtra("targetMuscle");

            ScrollView workoutScrollView = findViewById(R.id.resultSearchWorkout);

            workoutLayout = new LinearLayout(this);
            workoutLayout.setOrientation(LinearLayout.VERTICAL);

            workoutScrollView.addView(workoutLayout);

            Button filterWorkout = findViewById(R.id.openFilter);
            filterWorkout.setOnClickListener(this);

            BottomNavigationView bottomNavigationView = findViewById(R.id.mainNavigation);
            bottomNavigationView.setOnItemSelectedListener(this);

            onlineChecks.checkNavigationBar(bottomNavigationView);
            applyChange(durationString, difficultyString, targetMuscleString);
        } else {
            ScrollView workoutScrollView = findViewById(R.id.resultSearchWorkout);

            workoutLayout = new LinearLayout(this);
            workoutLayout.setOrientation(LinearLayout.VERTICAL);

            workoutScrollView.addView(workoutLayout);

            Button filterWorkout = findViewById(R.id.openFilter);
            filterWorkout.setOnClickListener(this);

            BottomNavigationView bottomNavigationView = findViewById(R.id.mainNavigation);
            bottomNavigationView.setOnItemSelectedListener(this);

            onlineChecks.checkNavigationBar(bottomNavigationView);
            try {
                String data = DBHelper.getAllWorkouts(null);
                ItemVisualiser.startWorkoutGeneration(data, this, workoutLayout, "search", R.layout.activity_exercise_popup, R.id.exerciseScrollView);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.goToHome) {
            startActivity(new Intent(getApplicationContext(), Home.class));
            return true;
        } else if (id == R.id.goToWorkouts) {
            startActivity(new Intent(getApplicationContext(), workout_option.class));
            return true;
        } else if (id == R.id.goToHistory) {
            startActivity(new Intent(getApplicationContext(), History.class));
            return true;
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.openFilter) {
            workout_filter customDialog = new workout_filter(this, duration, difficulty, target);
            customDialog.show();
        }
    }

    public void applyChange(String duration, String difficulty, String target) {
        ArrayList<String> toFilter = new ArrayList<>();
        workoutLayout.removeAllViews();
        StringBuilder filter = new StringBuilder();
        filter.append("WHERE");
        if ((!(duration.length() == 0))) {
            toFilter.add(" w.WorkoutDuration " + convertDuration(duration));
        }

        if ((!(difficulty.length() == 0)) && !(difficulty.equals("Any"))) {
            toFilter.add(" w.Difficulty = '" + difficulty + "'");
        }

        if ((!(target.length() == 0))) {
            toFilter.add(" w.TargetMuscleGroup = '" + target + "'");
        }
        for (String t : toFilter) {
            System.out.println(t);
        }
        try {
            if (toFilter.size() == 0) {
                String newData = DBHelper.getAllWorkouts(null);
                ItemVisualiser.startWorkoutGeneration(newData, this, workoutLayout, "search", R.layout.activity_exercise_popup, R.id.exerciseScrollView);
            } else {
                for (int i = 0; i < toFilter.size() - 1; i++) {
                    filter.append(toFilter.get(i)).append(" AND");
                }

                filter.append(toFilter.get(toFilter.size() - 1));

                String newFilter = filter.toString();
                System.out.println(newFilter);
                String newData = DBHelper.getAllWorkouts(newFilter);
                ItemVisualiser.startWorkoutGeneration(newData, this, workoutLayout, "search", R.layout.activity_exercise_popup, R.id.exerciseScrollView);
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);


        }
    }

    public String convertDuration(String input) {
        String[] durations = getResources().getStringArray(R.array.duration);
        if (durations[0].equals(input)) {
            return " < 10";
        } else if (durations[durations.length - 1].equals(input)) {
            return " > 120";
        } else {
            for (String duration : durations) {
                if (input.equals(duration)) {
                    String[] spl = duration.split("–");
                    return "BETWEEN " + spl[0].trim() + " AND " + spl[spl.length - 1].trim();
                }
            }
        }
        return "";
    }
}

