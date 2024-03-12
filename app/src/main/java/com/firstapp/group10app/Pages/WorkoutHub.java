package com.firstapp.group10app.Pages;

import static android.view.View.GONE;

import android.content.Intent;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.firstapp.group10app.DB.DbHelper;
import com.firstapp.group10app.Other.Session;
import com.firstapp.group10app.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class WorkoutHub extends AppCompatActivity implements View.OnClickListener {
    Button enhance, begin, calendar;
    LinearLayout workoutHubLinear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_hub);

        // Button declaration.
        enhance = findViewById(R.id.enhance);
        begin = findViewById(R.id.beginWorkout);
        begin.setOnClickListener(this);
        calendar = findViewById(R.id.addToCalendar);

        // Gets the current workout.
        JSONObject currentWorkout = Session.selectedWorkout;
        workoutHubLinear = findViewById(R.id.hubWorkoutHolder);
        try {
            // Sets the view.
            showWorkout(currentWorkout);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        // If the user is not signed in / anonymous, they cannot add the workout to history.
        if ((!Session.dbStatus) || (!Session.signedIn)) {
            begin.setVisibility(GONE);
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

        StyleSpan bss = new StyleSpan(android.graphics.Typeface.BOLD);

        SpannableStringBuilder sbWName = new SpannableStringBuilder("Workout Name: " + data.optString("WorkoutName"));
        sbWName.setSpan(bss, 0, 13, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        workoutName.setText(sbWName);

        SpannableStringBuilder sbWDuration = new SpannableStringBuilder("Workout Duration: " + data.optString("WorkoutDuration"));
        sbWDuration.setSpan(bss, 0, 17, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        workoutDuration.setText(sbWDuration);

        SpannableStringBuilder sbWTarget = new SpannableStringBuilder("Workout Target: " + data.optString("TargetMuscleGroup"));
        sbWTarget.setSpan(bss, 0, 15, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        workoutTarget.setText(sbWTarget);

        // Prevents blank views from being shown.
        if (data.optString("Equipment").equals("")) {
            workoutEquipment.setVisibility(GONE);
        } else {
            SpannableStringBuilder sbWEquipment = new SpannableStringBuilder("Workout Equipment: " + data.optString("Equipment"));
            sbWEquipment.setSpan(bss, 0, 18, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            workoutEquipment.setText(sbWEquipment);
            workoutHolder.addView(workoutEquipment);
        }

        SpannableStringBuilder sbWDifficulty = new SpannableStringBuilder("Workout Difficulty: " + data.optString("Difficulty"));
        sbWDifficulty.setSpan(bss, 0, 19, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        workoutDifficulty.setText(sbWDifficulty);

        workoutHolder.addView(workoutName);
        workoutHolder.addView(workoutDuration);
        workoutHolder.addView(workoutTarget);
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

            SpannableStringBuilder sbName = new SpannableStringBuilder("Exercise Name: " + workoutObject.optString("ExerciseName"));
            sbName.setSpan(bss, 0, 14, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            exerciseNameText.setText(sbName);
            textHolder.addView(exerciseNameText);

            SpannableStringBuilder sbDescription = new SpannableStringBuilder("Exercise Description: " + workoutObject.optString("Description"));
            sbDescription.setSpan(bss, 0, 21, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            exerciseDescriptionText.setText(sbDescription);
            textHolder.addView(exerciseDescriptionText);

            SpannableStringBuilder sbTarget = new SpannableStringBuilder("Exercise Target Group: " + workoutObject.optString("TargetMuscleGroup"));
            sbTarget.setSpan(bss, 0, 22, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            exerciseTargetMuscleGroupText.setText(sbTarget);
            textHolder.addView(exerciseTargetMuscleGroupText);

            SpannableStringBuilder sbEquipment = new SpannableStringBuilder("Exercise Equipment: " + workoutObject.optString("Equipment"));
            sbEquipment.setSpan(bss, 0, 18, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            exerciseEquipmentText.setText(sbEquipment);
            textHolder.addView(exerciseEquipmentText);

            SpannableStringBuilder sbDifficulty = new SpannableStringBuilder("Exercise Difficulty: " + workoutObject.optString("Difficulty"));
            sbDifficulty.setSpan(bss, 0, 20, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            exerciseDifficultyText.setText(sbDifficulty);
            textHolder.addView(exerciseDifficultyText);

            if (workoutObject.optString("Sets").equals("")) {
                exerciseSetsText.setVisibility(GONE);
            } else {
                SpannableStringBuilder sbSets = new SpannableStringBuilder("Exercise Sets: " + workoutObject.optString("Sets"));
                sbSets.setSpan(bss, 0, 14, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                exerciseSetsText.setText(sbSets);
                textHolder.addView(exerciseSetsText);
            }

            if (workoutObject.optString("Reps").equals("")) {
                exerciseRepsText.setVisibility(GONE);
            } else {
                SpannableStringBuilder sbReps = new SpannableStringBuilder("Exercise Reps: " + workoutObject.optString("Reps"));
                sbReps.setSpan(bss, 0, 14, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                exerciseRepsText.setText(sbReps);
                textHolder.addView(exerciseRepsText);
            }

            if (workoutObject.optString("Time").equals("")) {
                exerciseTimeText.setVisibility(GONE);
                textHolder.addView(exerciseTimeText);
            } else {
                SpannableStringBuilder sbTime = new SpannableStringBuilder("Exercise Time: " + workoutObject.optString("Time"));
                sbTime.setSpan(bss, 0, 14, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                exerciseTimeText.setText(sbTime);
            }

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

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.enhance) {
            Toast.makeText(this, "Currently in beta!", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.beginWorkout) {
            DbHelper.insertHistory();
            startActivity(new Intent(getApplicationContext(), ActivityContainer.class));
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        } else if (id == R.id.addToCalendar) {
            Toast.makeText(this, "Currently in beta!", Toast.LENGTH_SHORT).show();
        }
    }
}