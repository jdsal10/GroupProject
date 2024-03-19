package com.firstapp.group10app.Pages;

import static android.view.View.GONE;

import android.content.Intent;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;

import com.firstapp.group10app.DB.OnlineDb.OnlineDbHelper;
import com.firstapp.group10app.Other.Session;
import com.firstapp.group10app.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class WorkoutHub extends Fragment implements View.OnClickListener {
    private Button enhance, begin, calendar;
    LinearLayout workoutHubLinear;

    public WorkoutHub() {
        super(R.layout.activity_workout_hub);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_workout_hub, container, false);

        // Button declaration.
        enhance = rootView.findViewById(R.id.enhance);
        enhance.setOnClickListener(this);
        begin = rootView.findViewById(R.id.beginWorkout);
        begin.setOnClickListener(this);
        calendar = rootView.findViewById(R.id.addToCalendar);

        // Gets the current workout.
        JSONObject currentWorkout = Session.getSelectedWorkout();
        workoutHubLinear = rootView.findViewById(R.id.hubWorkoutHolder);
        try {
            // Sets the view.
            showWorkout(currentWorkout);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        // If the user is not signed in / anonymous, they cannot add the workout to history.
        if ((!Session.getOnlineDbStatus()) || (!Session.getSignedIn())) {
            begin.setVisibility(GONE);
        }

        return rootView;
    }

    public void showWorkout(JSONObject data) throws JSONException {
        LinearLayout workoutHolder = new LinearLayout(getContext());
        workoutHolder.setOrientation(LinearLayout.VERTICAL);
        TextView workoutName = new TextView(getContext());
        TextView workoutDuration = new TextView(getContext());
        TextView workoutTarget = new TextView(getContext());
        TextView workoutEquipment = new TextView(getContext());
        TextView workoutDifficulty = new TextView(getContext());

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
        if (data.optString("Equipment").isEmpty()) {
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

        View view = new View(getContext());

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
            // If the exercise is null, we break the loop. Otherwise a null pointer exception is thrown.
            if (t.isNull(i)) {
                break;
            }

            JSONObject workoutObject;

            try {
                workoutObject = t.getJSONObject(i);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

            LinearLayout textHolder = new LinearLayout(getContext());
            textHolder.setOrientation(LinearLayout.VERTICAL);

            TextView exerciseNameText = new TextView(getContext());
            TextView exerciseDescriptionText = new TextView(getContext());
            TextView exerciseTargetMuscleGroupText = new TextView(getContext());
            TextView exerciseEquipmentText = new TextView(getContext());
            TextView exerciseDifficultyText = new TextView(getContext());
            TextView exerciseSetsText = new TextView(getContext());
            TextView exerciseRepsText = new TextView(getContext());
            TextView exerciseTimeText = new TextView(getContext());

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

            if (workoutObject.optString("Sets").isEmpty()) {
                exerciseSetsText.setVisibility(GONE);
            } else {
                SpannableStringBuilder sbSets = new SpannableStringBuilder("Exercise Sets: " + workoutObject.optString("Sets"));
                sbSets.setSpan(bss, 0, 14, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                exerciseSetsText.setText(sbSets);
                textHolder.addView(exerciseSetsText);
            }

            if (workoutObject.optString("Reps").isEmpty()) {
                exerciseRepsText.setVisibility(GONE);
            } else {
                SpannableStringBuilder sbReps = new SpannableStringBuilder("Exercise Reps: " + workoutObject.optString("Reps"));
                sbReps.setSpan(bss, 0, 14, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                exerciseRepsText.setText(sbReps);
                textHolder.addView(exerciseRepsText);
            }

            if (workoutObject.optString("Time").isEmpty()) {
                exerciseTimeText.setVisibility(GONE);
                textHolder.addView(exerciseTimeText);
            } else {
                SpannableStringBuilder sbTime = new SpannableStringBuilder("Exercise Time: " + workoutObject.optString("Time"));
                sbTime.setSpan(bss, 0, 14, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                exerciseTimeText.setText(sbTime);
            }

            workoutHubLinear.addView(textHolder);

            View view2 = new View(getContext());

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
            EnhanceInput enhance = new EnhanceInput(getContext());
            enhance.show();
        } else if (id == R.id.beginWorkout) {
            startActivity(new Intent(getContext(), ActiveWorkoutLoading.class));
        } else if (id == R.id.addToCalendar) {
            Toast.makeText(getContext(), "Currently in beta!", Toast.LENGTH_SHORT).show();
        }
    }
}