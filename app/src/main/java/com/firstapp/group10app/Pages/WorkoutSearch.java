package com.firstapp.group10app.Pages;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import androidx.appcompat.app.AppCompatActivity;

import com.firstapp.group10app.DB.DatabaseManager;
import com.firstapp.group10app.Other.ItemVisualiser;
import com.firstapp.group10app.R;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Objects;

public class WorkoutSearch extends AppCompatActivity implements View.OnClickListener, WorkoutFilter.FilterChangeListener {
    private LinearLayout workoutLayout;
    private String durationString, difficultyString, targetString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_search);
        Intent intent = getIntent();

        initializeLayout();

        // Behaviour when a filter is applied
        if (intent != null && getIntent().hasExtra("duration") && getIntent().hasExtra("difficulty") && getIntent().hasExtra("targetMuscle")) {
            difficultyString = getIntent().getStringExtra("difficulty");
            durationString = getIntent().getStringExtra("duration");
            targetString = getIntent().getStringExtra("targetMuscle");

            visualizeWorkoutsWithFilter(difficultyString, durationString, targetString);
        }

        // Behaviour when there are no filters applied
        else {
            try {
                String data = DatabaseManager.getInstance().getWorkoutsAsJsonArray(null);
                ItemVisualiser.startWorkoutGeneration(data, this, workoutLayout, "search", R.layout.activity_exercise_popup, R.id.exerciseScrollView);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void initializeLayout() {
        workoutLayout = new LinearLayout(getApplicationContext());
        workoutLayout.setOrientation(LinearLayout.VERTICAL);
        ScrollView workoutScrollView = findViewById(R.id.resultSearchWorkout);
        workoutScrollView.addView(workoutLayout);

        Button filterWorkout = findViewById(R.id.openFilter);
        ImageButton backButton = findViewById(R.id.backButton);
        filterWorkout.setOnClickListener(this);
        backButton.setOnClickListener(this);
    }

    @Override
    public void onFilterChanged(String difficulty, String duration, String target) {
        // Update UI or perform actions based on the new filter values
        visualizeWorkoutsWithFilter(difficulty, duration, target);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.backButton) {
            startActivity(new Intent(getApplicationContext(), ActivityContainer.class));
            overridePendingTransition(R.anim.slide_up_in, R.anim.slide_up_out);
        } else if (id == R.id.openFilter) {
            // After creating an instance of workout_filter
            WorkoutFilter customDialog = new WorkoutFilter(this);

            customDialog.setFilterChangeListener(this::visualizeWorkoutsWithFilter);

            Objects.requireNonNull(customDialog.getWindow()).setWindowAnimations(R.style.filterAnimations);

            customDialog.show();
            Log.d("WorkoutSearch.onClick()", "openFilter clicked");
            Log.d("WorkoutSearch.onClick()", "SELECTED VALUES: " + difficultyString + durationString + targetString);
            customDialog.setValue(difficultyString, durationString, targetString);
        }
    }

    public void visualizeWorkoutsWithFilter(String difficulty, String duration, String target) {
        ArrayList<String> toFilter = new ArrayList<>();
        workoutLayout.removeAllViews();
        StringBuilder filter = new StringBuilder();
        filter.append("WHERE");
        if ((!(duration.isEmpty())) && !(duration.equals("Any"))) {
            toFilter.add(" w.WorkoutDuration " + convertDuration(duration));
        }

        if ((!(difficulty.isEmpty())) && !(difficulty.equals("Any"))) {
            toFilter.add(" w.Difficulty = '" + difficulty + "'");
        }

        if ((!(target.isEmpty())) && !(target.equals("Any"))) {
            toFilter.add(" w.TargetMuscleGroup = '" + target + "'");
        }

        try {
            String newData;

            if (toFilter.isEmpty()) {
                newData = DatabaseManager.getInstance().getWorkoutsAsJsonArray(null);
            } else {
                for (int i = 0; i < toFilter.size() - 1; i++) {
                    filter.append(toFilter.get(i)).append(" AND");
                }

                filter.append(toFilter.get(toFilter.size() - 1));

                String newFilter = filter.toString();
                newData = DatabaseManager.getInstance().getWorkoutsAsJsonArray(newFilter);
            }

            ItemVisualiser.startWorkoutGeneration(newData, this, workoutLayout, "search", R.layout.activity_exercise_popup, R.id.exerciseScrollView);

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