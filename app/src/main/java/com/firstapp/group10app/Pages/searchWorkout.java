package com.firstapp.group10app.Pages;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.firstapp.group10app.DB.DBHelper;
import com.firstapp.group10app.Other.ItemVisualiser;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
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
import java.util.Objects;

public class searchWorkout extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener, View.OnClickListener, workout_filter.FilterChangeListener  {
    LinearLayout workoutLayout;
    String durationString, difficultyString, targetString;
    workout_filter customDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_workout);
        Intent intent = getIntent();

        if (intent != null && getIntent().hasExtra("duration") && getIntent().hasExtra("difficulty") && getIntent().hasExtra("targetMuscle")) {
            difficultyString = getIntent().getStringExtra("difficulty");
            durationString = getIntent().getStringExtra("duration");
            targetString = getIntent().getStringExtra("targetMuscle");

            initializeLayout();
            applyChange(difficultyString, durationString, targetString);
        } else {
            initializeLayout();
            try {
                String data = DBHelper.getAllWorkouts(null);
                System.out.println(data);
                ItemVisualiser.startWorkoutGeneration(data, this, workoutLayout, "search", R.layout.activity_exercise_popup, R.id.exerciseScrollView);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void initializeLayout() {
        ScrollView workoutScrollView = findViewById(R.id.resultSearchWorkout);
        workoutLayout = new LinearLayout(getApplicationContext());
        workoutLayout.setOrientation(LinearLayout.VERTICAL);
        workoutScrollView.addView(workoutLayout);

        Button filterWorkout = findViewById(R.id.openFilter);
        filterWorkout.setOnClickListener(this);

        BottomNavigationView bottomNavigationView = findViewById(R.id.mainNavigation);
        bottomNavigationView.setOnItemSelectedListener(this);
        bottomNavigationView.getMenu().findItem(R.id.goToWorkouts).setChecked(true);

        onlineChecks.checkNavigationBar(bottomNavigationView);
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
    public void onFilterChanged(String difficulty, String duration, String target) {
        // Update UI or perform actions based on the new filter values
        System.out.println("CHANGED");
        applyChange(difficulty, duration, target);
    }
    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.openFilter) {
            customDialog = new workout_filter(searchWorkout.this);
            // After creating an instance of workout_filter
            workout_filter customDialog = new workout_filter(this);
            customDialog.setFilterChangeListener(new workout_filter.FilterChangeListener() {
                @Override
                public void onFilterChanged(String difficulty, String duration, String target) {
                    System.out.println("CHANGED");
                    applyChange(difficulty, duration, target);                }
            });

            Objects.requireNonNull(customDialog.getWindow()).setWindowAnimations(R.style.filterAnimations); // Apply animation style
            customDialog.show();
            customDialog.setValue(difficultyString, durationString, targetString);
        } else if (id == R.id.goToSettings) {
            startActivity(new Intent(searchWorkout.this, Settings.class));
        }
    }

    public void applyChange(String difficulty, String duration, String target) {
        ArrayList<String> toFilter = new ArrayList<>();
        workoutLayout.removeAllViews();
        StringBuilder filter = new StringBuilder();
        filter.append("WHERE");
        if ((!(duration.length() == 0)) && !(duration.equals("Any"))) {
            toFilter.add(" w.WorkoutDuration " + convertDuration(duration));
        }

        if ((!(difficulty.length() == 0)) && !(difficulty.equals("Any"))) {
            toFilter.add(" w.Difficulty = '" + difficulty + "'");
        }

        if ((!(target.length() == 0)) && !(target.equals("Any"))) {
            toFilter.add(" w.TargetMuscleGroup = '" + target + "'");
        }

        try {
            if (toFilter.size() == 0) {
                String newData = DBHelper.getAllWorkouts(null);
                ItemVisualiser.startWorkoutGeneration(newData, workoutLayout.getContext(), workoutLayout, "search", R.layout.activity_exercise_popup, R.id.exerciseScrollView);
            } else {
                for (int i = 0; i < toFilter.size() - 1; i++) {
                    filter.append(toFilter.get(i)).append(" AND");
                }

                filter.append(toFilter.get(toFilter.size() - 1));

                String newFilter = filter.toString();
                String newData = DBHelper.getAllWorkouts(newFilter);
                ItemVisualiser.startWorkoutGeneration(newData, workoutLayout.getContext(), workoutLayout, "search", R.layout.activity_exercise_popup, R.id.exerciseScrollView);
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public String convertDuration(String input) {
        String[] durations = getResources().getStringArray(R.array.duration);
        if (durations[1].equals(input)) {
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

