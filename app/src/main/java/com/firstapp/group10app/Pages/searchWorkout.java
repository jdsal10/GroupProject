package com.firstapp.group10app.Pages;

import androidx.appcompat.app.AppCompatActivity;

import com.firstapp.group10app.Other.ItemVisualiser;


import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.firstapp.group10app.DB.DBHelper;
import com.firstapp.group10app.R;
import com.google.android.material.navigation.NavigationBarView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;

public class searchWorkout extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener, View.OnClickListener {
    private LinearLayout workoutLayout;
    private EditText durationText, difficultyText, targetMuscleText;
    private ScrollView workoutScrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_workout);

        workoutScrollView = findViewById(R.id.resultSearchWorkout);

        workoutLayout = new LinearLayout(this);
        workoutLayout.setOrientation(LinearLayout.VERTICAL);

        workoutScrollView.addView(workoutLayout);

        durationText = findViewById(R.id.durationInput);
        difficultyText = findViewById(R.id.difficultyInput);
        targetMuscleText = findViewById(R.id.targetMuscleInput);

        Button filterWorkout = findViewById(R.id.filterWorkouts);
        filterWorkout.setOnClickListener(this);

        NavigationBarView bottomNavigationView = findViewById(R.id.mainNavigation);
        bottomNavigationView.setOnItemSelectedListener(this);

        try {
            ItemVisualiser.startWorkoutGeneration(null, this, workoutLayout, "search", R.layout.activity_exercise_popup, R.id.exerciseScrollView);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.goSettings) {
            startActivity(new Intent(getApplicationContext(), Settings.class));
            return true;
        } else if (id == R.id.goStats) {
            return true;
            //Code for stats
        } else if (id == R.id.goHome) {
            startActivity(new Intent(getApplicationContext(), Home.class));
            return true;
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.filterWorkouts) {
            try {
                workoutLayout.removeAllViews();
                ItemVisualiser.runFilter(durationText.getText().toString(), difficultyText.getText().toString(),
                        targetMuscleText.getText().toString(), this, workoutLayout, "search", R.layout.activity_exercise_popup, R.id.exerciseScrollView);

            } catch (SQLException | JSONException e) {
                throw new RuntimeException(e);
            }
        }
    }

}