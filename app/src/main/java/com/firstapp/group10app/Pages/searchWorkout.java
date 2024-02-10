package com.firstapp.group10app.Pages;

import static java.security.AccessController.getContext;

import androidx.appcompat.app.AppCompatActivity;

import com.firstapp.group10app.DB.DBHelper;
import com.firstapp.group10app.Other.ItemVisualiser;


import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.firstapp.group10app.Other.onlineChecks;
import com.firstapp.group10app.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import org.json.JSONException;

import java.sql.SQLException;
import java.util.ArrayList;

public class searchWorkout extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener, View.OnClickListener {
    private static LinearLayout workoutLayout;
    private EditText durationText, difficultyText, targetMuscleText;

    public searchWorkout() {
        ScrollView workoutScrollView = findViewById(R.id.resultSearchWorkout);

        workoutLayout = new LinearLayout(this);
        workoutLayout.setOrientation(LinearLayout.VERTICAL);

        workoutScrollView.addView(workoutLayout);


        Button filterWorkout = findViewById(R.id.openFilter);
        filterWorkout.setOnClickListener(this);

        BottomNavigationView bottomNavigationView = findViewById(R.id.mainNavigation);
        bottomNavigationView.setOnItemSelectedListener(this);

        onlineChecks.checkNavigationBar(bottomNavigationView);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_workout);

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
            // Code for history.
            return true;
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.openFilter) {
            workout_filter customDialog = new workout_filter(this);
            customDialog.show();
        }
    }

    public void applyChange(String duration, String difficulty, String target) {
        ArrayList<String> toFilter = new ArrayList<>();
        workoutLayout.removeAllViews();
        StringBuilder filter = new StringBuilder();
        filter.append("WHERE");
        if ((!(duration.length() == 0))) {
            toFilter.add(" w.WorkoutDuration = '" + duration + "'");
        }

        if ((!(difficulty.length() == 0))) {
            toFilter.add(" w.Difficulty = '" + difficulty + "'");
        }

        if ((!(target.length() == 0))) {
            toFilter.add(" w.TargetMuscleGroup = '" + target + "'");
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
                String newData = DBHelper.getAllWorkouts(newFilter);
                ItemVisualiser.startWorkoutGeneration(newData, this, workoutLayout, "search", R.layout.activity_exercise_popup, R.id.exerciseScrollView);
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);


        }
    }
}