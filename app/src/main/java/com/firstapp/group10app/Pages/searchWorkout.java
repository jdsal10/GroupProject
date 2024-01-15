package com.firstapp.group10app.Pages;


import androidx.appcompat.app.AppCompatActivity;

import com.firstapp.group10app.Other.ItemVisualiser;


import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
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
import java.util.Map;

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
                ItemVisualiser.showEmpty(scrollView);
            } else {
                JSONArray jsonArray = new JSONArray(input);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject workoutObject = jsonArray.getJSONObject(i);
                    Map<Integer, View> out = ItemVisualiser.addDetails(workoutObject, this, workoutLayout);
                    Integer in = null;
                    View v = null;

                    for (Map.Entry<Integer, View> e : out.entrySet()) {
                        in = e.getKey();
                        v = e.getValue();
                    }
                    addButtonFunction(v, in);
                }
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public void addButtonFunction(View v, int wID) {
        Button close = v.findViewById(R.id.closeExercise);
        close.setOnClickListener(this);

        Button selectWorkout = v.findViewById(R.id.selectWorkout);
        selectWorkout.setOnClickListener(v1 -> {
            int id = v1.getId();
            if (id == R.id.selectWorkout) {
                JSONObject workoutObject;
                String out = DBHelper.getAllWorkouts("WHERE w.WorkoutID = '" + wID + "'");
                JSONArray jsonArray;
                try {
                    jsonArray = new JSONArray(out);
                    workoutObject = jsonArray.getJSONObject(0);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

                Session.selectedWorkout = workoutObject;
                System.out.println("Current workout: " + Session.selectedWorkout.toString());
                startActivity(new Intent(searchWorkout.this, Home.class));
            }
        });
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
                // To avoid duplicate parents, the workoutLayout is initialised here again,
                scrollView.removeAllViews();
                workoutLayout = new LinearLayout(this);
                workoutLayout.setOrientation(LinearLayout.VERTICAL);
                scrollView.addView(workoutLayout);
                ItemVisualiser.runFilter(durationText.getText().toString(), difficultyText.getText().toString(),
                        targetMuscleText.getText().toString(), this, workoutLayout);
            } catch (SQLException | JSONException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
