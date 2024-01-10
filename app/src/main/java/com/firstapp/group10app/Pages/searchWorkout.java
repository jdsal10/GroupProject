package com.firstapp.group10app.Pages;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.firstapp.group10app.DB.DBHelper;
import com.firstapp.group10app.R;
import com.google.android.material.navigation.NavigationBarView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.ArrayList;

public class searchWorkout extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener, View.OnClickListener {
    private LinearLayout workoutLayout, exerciseLayout;
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
            updateWorkouts(null);
        } catch (JSONException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateWorkouts(String filter) throws JSONException, SQLException {
        String input = DBHelper.getAllWorkouts(filter);

        if (input == null) {
            showEmpty();
        } else {
            JSONArray jsonArray = new JSONArray(input);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject workoutObject = jsonArray.getJSONObject(i);
                addDetails(workoutObject);
            }
        }
    }

    public void addDetails(JSONObject details) {
        LinearLayout box = (LinearLayout) getLayoutInflater().inflate(R.layout.activity_workout_view, null);

        TextView nameView = box.findViewById(R.id.workoutNameView);
        TextView durationView = box.findViewById(R.id.workoutDurationView);
        TextView muscleView = box.findViewById(R.id.workoutMuscleView);
        TextView equipmentView = box.findViewById(R.id.workoutEquipmentView);
        TextView difficultyView = box.findViewById(R.id.workoutDifficultyView);

        box.setId(details.optInt("WorkoutID"));

        // Sets the textViews to the appropriate details.
        nameView.setText("Workout Name: " + details.optString("WorkoutName", ""));
        durationView.setText("Workout Duration: " + details.optString("WorkoutDuration", ""));
        muscleView.setText("Target Muscle Group: " + details.optString("TargetMuscleGroup", ""));
        equipmentView.setText("Equipment: " + details.optString("Equipment", ""));
        difficultyView.setText("Difficulty: " + details.optString("Difficulty", ""));

        String exerciseList = details.optString("Exercises");

        // Adds to a linear layout.
        workoutLayout.addView(box);

        // For now, clicking on a workout shows the exercises - may make easier later.
        box.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            LayoutInflater inflater = getLayoutInflater();

            View dialogView = inflater.inflate(R.layout.activity_exercise_popup, null);

            builder.setView(dialogView);
            AlertDialog alertDialog = builder.create();
            ScrollView exerciseMainView = dialogView.findViewById(R.id.exerciseMainView);

            // Creates a layout containing the exercise boxes.
            exerciseLayout = new LinearLayout(this);
            exerciseLayout.setOrientation(LinearLayout.VERTICAL);

            JSONArray jsonArray = null;
            try {
                jsonArray = new JSONArray(exerciseList);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

            // For every exercise, we create a box containing the details.
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject workoutObject = null;

                try {
                    workoutObject = jsonArray.getJSONObject(i);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

                LinearLayout exerciseBox = (LinearLayout) getLayoutInflater().inflate(R.layout.activity_exercise_view, null);

                TextView exerciseNameView = exerciseBox.findViewById(R.id.exerciseNameView);
                TextView exerciseDescriptionView = exerciseBox.findViewById(R.id.exerciseDescriptionView);
                TextView exerciseIllustrationView = exerciseBox.findViewById(R.id.exerciseIllustrationView);
                TextView exerciseTargetMuscleGroupView = exerciseBox.findViewById(R.id.exerciseTargetMuscleGroupView);
                TextView exerciseEquipmentView = exerciseBox.findViewById(R.id.exerciseEquipmentView);
                TextView exerciseDifficultyView = exerciseBox.findViewById(R.id.exerciseDifficultyView);

                exerciseNameView.setText("Exercise Name: " + workoutObject.optString("ExerciseName", ""));
                exerciseDescriptionView.setText("Exercise Description: " + workoutObject.optString("Description", ""));
                exerciseIllustrationView.setText("Exercise Illustration: " + workoutObject.optString("Illustration", ""));
                exerciseTargetMuscleGroupView.setText("Exercise Target Group: " + workoutObject.optString("TargetMuscleGroup", ""));
                exerciseEquipmentView.setText("Exercise Equipment: " + workoutObject.optString("Equipment", ""));
                exerciseDifficultyView.setText("Exercise Difficulty: " + workoutObject.optString("Difficulty", ""));

                exerciseLayout.addView(exerciseBox);
            }
            // Adds the Linear layout containing all boxes to the scroll view.
            exerciseMainView.addView(exerciseLayout);

            alertDialog.show();
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
                runFilter(durationText.getText().toString(), difficultyText.getText().toString(),
                        targetMuscleText.getText().toString());
            } catch (SQLException | JSONException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void runFilter(String duration, String difficulty, String targetMuscle) throws SQLException, JSONException {
        ArrayList<String> toFilter = new ArrayList<>();

        StringBuilder filter = new StringBuilder();
        filter.append("WHERE");
        if ((!(duration.length() == 0))) {
            toFilter.add(" w.WorkoutDuration = '" + duration + "'");
        }

        if ((!(difficulty.length() == 0))) {
            toFilter.add(" w.Difficulty = '" + difficulty + "'");
        }

        if ((!(targetMuscle.length() == 0))) {
            toFilter.add(" w.TargetMuscleGroup = '" + targetMuscle + "'");
        }

        if (toFilter.size() == 0) {
            updateWorkouts(null);
        } else {
            for (int i = 0; i < toFilter.size() - 1; i++) {
                filter.append(toFilter.get(i)).append(" AND");
            }
            filter.append(toFilter.get(toFilter.size() - 1));

            String newFilter = filter.toString();

            updateWorkouts(newFilter);
        }

    }

    public void showEmpty() {
        LinearLayout emptyLayout = new LinearLayout(this);
        emptyLayout.setOrientation(LinearLayout.VERTICAL);
        TextView empty = new TextView(this);
        empty.setText("No workouts were found");

        emptyLayout.addView(empty);

        scrollView.removeAllViews();

        scrollView.addView(emptyLayout);
    }

}
