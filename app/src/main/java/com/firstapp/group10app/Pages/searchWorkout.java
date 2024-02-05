package com.firstapp.group10app.Pages;

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
    private LinearLayout workoutLayout;
    private EditText durationText, difficultyText, targetMuscleText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_workout);

        ScrollView workoutScrollView = findViewById(R.id.resultSearchWorkout);

        workoutLayout = new LinearLayout(this);
        workoutLayout.setOrientation(LinearLayout.VERTICAL);

        workoutScrollView.addView(workoutLayout);

        durationText = findViewById(R.id.durationInput);
        difficultyText = findViewById(R.id.difficultyInput);
        targetMuscleText = findViewById(R.id.targetMuscleInput);

        Button filterWorkout = findViewById(R.id.filterWorkouts);
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
        if (id == R.id.filterWorkouts) {

            String durationString = durationText.getText().toString();
            String difficultyString = difficultyText.getText().toString();
            String targetMuscleString = targetMuscleText.getText().toString();

            ArrayList<String> toFilter = new ArrayList<>();
            workoutLayout.removeAllViews();
            StringBuilder filter = new StringBuilder();
            filter.append("WHERE");
            if ((!(durationString.length() == 0))) {
                toFilter.add(" w.WorkoutDuration = '" + durationString + "'");
            }

            if ((!(difficultyString.length() == 0))) {
                toFilter.add(" w.Difficulty = '" + difficultyString + "'");
            }

            if ((!(targetMuscleString.length() == 0))) {
                toFilter.add(" w.TargetMuscleGroup = '" + targetMuscleString + "'");
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

}