package com.firstapp.group10app.Pages;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firstapp.group10app.Other.Session;
import com.firstapp.group10app.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class workoutHub extends AppCompatActivity {
    Button enhance, begin, calendar;
    LinearLayout workoutHubLinear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_hub);

        enhance = findViewById(R.id.enhance);
        begin = findViewById(R.id.beginWorkout);
        calendar = findViewById(R.id.addToCalendar);

        JSONObject currentWorkout = Session.selectedWorkout;
        workoutHubLinear = findViewById(R.id.hubWorkoutHolder);
        try {
            showWorkout(currentWorkout);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

    }
    public void showWorkout(JSONObject data) throws JSONException {
        LinearLayout workoutHolder = new LinearLayout(this);
        workoutHolder.setOrientation(LinearLayout.VERTICAL);
        TextView workoutName = new TextView(this);
        TextView workoutDuration = new TextView(this);
        TextView workoutTarget = new TextView(this);
        TextView workoutEquipment = new TextView(this);
        TextView workoutDifficulty = new TextView(this);

        workoutName.setText(String.format("Workout Name: %s", String.format(data.optString("WorkoutName", ""))));
        workoutDuration.setText(String.format("Workout Duration: %s", String.format(data.optString("WorkoutDuration", ""))));
        workoutTarget.setText(String.format("Workout Target: %s", String.format(data.optString("TargetMuscleGroup", ""))));
        workoutEquipment.setText(String.format("Workout Equipment: %s", String.format(data.optString("Equipment", ""))));
        workoutDifficulty.setText(String.format("Workout Difficulty: %s", String.format(data.optString("Difficulty", ""))));

        workoutHolder.addView(workoutName);
        workoutHolder.addView(workoutDuration);
        workoutHolder.addView(workoutTarget);
        workoutHolder.addView(workoutEquipment);
        workoutHolder.addView(workoutDifficulty);

        workoutHubLinear.addView(workoutHolder);

        View view = new View(this);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                1
        );

        layoutParams.setMargins(10, 10, 10, 10);

        view.setBackgroundColor(this.getResources().getColor(android.R.color.darker_gray));

        view.setLayoutParams(layoutParams);

        workoutHubLinear.addView(view);

        JSONArray t = data.getJSONArray("Exercises");

        // For every exercise, we create a box containing the details.
        for (int i = 0; i < t.length(); i++) {
            JSONObject workoutObject;

            try {
                workoutObject = t.getJSONObject(i);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

            LinearLayout textHolder = new LinearLayout(this);
            textHolder.setOrientation(LinearLayout.VERTICAL);

            TextView exerciseNameText = new TextView(this);
            TextView exerciseDescriptionText = new TextView(this);
            TextView exerciseTargetMuscleGroupText = new TextView(this);
            TextView exerciseEquipmentText = new TextView(this);
            TextView exerciseDifficultyText = new TextView(this);
            TextView exerciseSetsText = new TextView(this);
            TextView exerciseRepsText = new TextView(this);
            TextView exerciseTimeText = new TextView(this);

            StyleSpan bss = new StyleSpan(android.graphics.Typeface.BOLD);

            SpannableStringBuilder sbName = new SpannableStringBuilder("Exercise Name: " + workoutObject.optString("ExerciseName"));
            sbName.setSpan(bss, 0, 14, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            exerciseNameText.setText(sbName);

            SpannableStringBuilder sbDescription = new SpannableStringBuilder("Exercise Description: " + workoutObject.optString("Description"));
            sbDescription.setSpan(bss, 0, 21, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            exerciseDescriptionText.setText(sbDescription);

            SpannableStringBuilder sbTarget = new SpannableStringBuilder("Exercise Target Group: " + workoutObject.optString("TargetMuscleGroup"));
            sbTarget.setSpan(bss, 0, 22, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            exerciseTargetMuscleGroupText.setText(sbTarget);

            SpannableStringBuilder sbEquipment = new SpannableStringBuilder("Exercise Equipment: " + workoutObject.optString("ExerciseEquipment"));
            sbEquipment.setSpan(bss, 0, 18, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            exerciseEquipmentText.setText(sbEquipment);

            SpannableStringBuilder sbDifficulty = new SpannableStringBuilder("Exercise Difficulty: " + workoutObject.optString("ExerciseDifficulty"));
            sbDifficulty.setSpan(bss, 0, 20, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            exerciseDifficultyText.setText(sbDifficulty);

            SpannableStringBuilder sbSets = new SpannableStringBuilder("Exercise Sets: " + workoutObject.optString("Sets"));
            sbSets.setSpan(bss, 0, 14, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            exerciseSetsText.setText(sbSets);

            SpannableStringBuilder sbReps = new SpannableStringBuilder("Exercise Reps: " + workoutObject.optString("Reps"));
            sbReps.setSpan(bss, 0, 14, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            exerciseRepsText.setText(sbReps);

            SpannableStringBuilder sbTime = new SpannableStringBuilder("Exercise Time: " + workoutObject.optString("Time"));
            sbTime.setSpan(bss, 0, 14, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            exerciseTimeText.setText(sbTime);

            textHolder.addView(exerciseNameText);
            textHolder.addView(exerciseDescriptionText);
            textHolder.addView(exerciseTargetMuscleGroupText);
            textHolder.addView(exerciseEquipmentText);
            textHolder.addView(exerciseDifficultyText);
            textHolder.addView(exerciseSetsText);
            textHolder.addView(exerciseRepsText);
            textHolder.addView(exerciseTimeText);

            workoutHubLinear.addView(textHolder);

            View view2 = new View(this);

            LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    1
            );

            layoutParams2.setMargins(10, 10, 10, 10);

            view2.setBackgroundColor(this.getResources().getColor(android.R.color.darker_gray));

            view2.setLayoutParams(layoutParams2);

            workoutHubLinear.addView(view2);
        }

    }
}