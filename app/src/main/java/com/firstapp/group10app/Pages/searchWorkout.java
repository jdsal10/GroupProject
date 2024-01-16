package com.firstapp.group10app.Pages;


import static com.firstapp.group10app.Other.ItemVisualiser.addSearchButtons;

import androidx.appcompat.app.AppCompatActivity;

import com.firstapp.group10app.Other.ItemVisualiser;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.firstapp.group10app.DB.DBHelper;
import com.firstapp.group10app.Other.Session;
import com.firstapp.group10app.R;
import com.google.android.material.navigation.NavigationBarView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;

public class searchWorkout extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener, View.OnClickListener {
    private LinearLayout workoutLayout;
    private EditText durationText, difficultyText, targetMuscleText;
    private ScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_workout);


        scrollView = findViewById(R.id.resultSearchWorkout);
        workoutLayout = new LinearLayout(this);
        workoutLayout.setOrientation(LinearLayout.VERTICAL);

        scrollView.addView(workoutLayout);

        durationText = findViewById(R.id.durationInput);
        difficultyText = findViewById(R.id.difficultyInput);
        targetMuscleText = findViewById(R.id.targetMuscleInput);

        Button filterWorkout = findViewById(R.id.filterWorkouts);
        filterWorkout.setOnClickListener(this);

        NavigationBarView bottomNavigationView = findViewById(R.id.mainNavigation);
        bottomNavigationView.setOnItemSelectedListener(this);

        try {
            String input = DBHelper.getAllWorkouts(null);

            if (input == null) {
                ItemVisualiser.showEmpty(scrollView, this);
            } else {
                JSONArray jsonArray = new JSONArray(input);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject workoutObject = jsonArray.getJSONObject(i);
                    ItemVisualiser.addDetails(workoutObject, this, workoutLayout, "search", R.layout.activity_exercise_popup);
                }
            }
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
                        targetMuscleText.getText().toString(), this, workoutLayout, scrollView, "search", R.layout.activity_exercise_popup);
                LinearLayout containerView = new LinearLayout(this);
                containerView.setOrientation(LinearLayout.VERTICAL);
                if (workoutLayout.getParent() != null) {
                    ((ViewGroup) workoutLayout.getParent()).removeView(workoutLayout);
                }

                containerView.addView(workoutLayout);

                // Add the container view to scrollView
                scrollView.removeAllViews();
                scrollView.addView(containerView);
            } catch (SQLException | JSONException e) {
                throw new RuntimeException(e);
            }
        }
    }

}