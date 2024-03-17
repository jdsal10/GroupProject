package com.firstapp.group10app.Other;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ItemVisualiserText {
    public static void showText(Context context, LinearLayout dataHolder, String[] workoutDetails, ArrayList<JSONObject> addedExercises) {
        LinearLayout workoutView = new LinearLayout(context);
        workoutView.setOrientation(LinearLayout.VERTICAL);

        TextView workoutName = new TextView(context);
        TextView workoutDuration = new TextView(context);
        TextView workoutTarget = new TextView(context);
        TextView workoutEquipment = new TextView(context);
        TextView workoutDifficulty = new TextView(context);

        workoutName.setText(String.format("Workout Name: %s", String.format(workoutDetails[0])));
        workoutDuration.setText(String.format("Workout Duration: %s", String.format(workoutDetails[1])));
        workoutTarget.setText(String.format("Workout Target: %s", String.format(workoutDetails[2])));
        workoutEquipment.setText(String.format("Workout Equipment: %s", String.format(workoutDetails[3])));
        workoutDifficulty.setText(String.format("Workout Difficulty: %s", String.format(workoutDetails[4])));

        workoutView.addView(workoutName);
        workoutView.addView(workoutDuration);
        workoutView.addView(workoutTarget);
        workoutView.addView(workoutEquipment);
        workoutView.addView(workoutDifficulty);

        dataHolder.addView(workoutView);

        View view = new View(context);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                1
        );

        layoutParams.setMargins(10, 10, 10, 10);

        view.setBackgroundColor(context.getResources().getColor(android.R.color.darker_gray));

        view.setLayoutParams(layoutParams);

        dataHolder.addView(view);

        // Creates a layout containing the exercise boxes.
        JSONArray jsonArray;
        jsonArray = new JSONArray();
        for (JSONObject exercise : addedExercises) {
            jsonArray.put(exercise);
        }

        // For every exercise, we create a box containing the details.
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject workoutObject;

            try {
                workoutObject = jsonArray.getJSONObject(i);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

            LinearLayout textHolder = new LinearLayout(context);
            textHolder.setOrientation(LinearLayout.VERTICAL);

            TextView exerciseNameText = new TextView(context);
            TextView exerciseDescriptionText = new TextView(context);
            TextView exerciseTargetMuscleGroupText = new TextView(context);
            TextView exerciseEquipmentText = new TextView(context);
            TextView exerciseDifficultyText = new TextView(context);
            TextView exerciseSetsText = new TextView(context);
            TextView exerciseRepsText = new TextView(context);
            TextView exerciseTimeText = new TextView(context);

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

            SpannableStringBuilder sbEquipment = new SpannableStringBuilder("Exercise Equipment: " + workoutObject.optString("Equipment"));
            sbEquipment.setSpan(bss, 0, 18, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            exerciseEquipmentText.setText(sbEquipment);

            SpannableStringBuilder sbDifficulty = new SpannableStringBuilder("Exercise Difficulty: " + workoutObject.optString("Difficulty"));
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

            dataHolder.addView(textHolder);

            View view2 = new View(context);

            LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    1
            );

            layoutParams2.setMargins(10, 10, 10, 10);

            view2.setBackgroundColor(context.getResources().getColor(android.R.color.darker_gray));

            view2.setLayoutParams(layoutParams2);

            dataHolder.addView(view2);
        }
    }

    public static void showTextJSON(Context context, LinearLayout dataHolder, JSONObject data) {
        LinearLayout workoutView = new LinearLayout(context);
        workoutView.setOrientation(LinearLayout.VERTICAL);

        TextView workoutName = new TextView(context);
        TextView workoutDuration = new TextView(context);
        TextView workoutTarget = new TextView(context);
        TextView workoutEquipment = new TextView(context);
        TextView workoutDifficulty = new TextView(context);

        workoutName.setText(String.format(data.optString("WorkoutName", "")));
        workoutDuration.setText(String.format("Workout Duration: %s minutes", data.optString("WorkoutDuration", "")));
        workoutTarget.setText(String.format("Target Muscle Group: %s", data.optString("TargetMuscleGroup", "")));
        workoutEquipment.setText(String.format(String.format("Equipment: %s", data.optString("Equipment", ""))));
        workoutDifficulty.setText(String.format(String.format("Difficulty: %s", data.optString("Difficulty", ""))));

        workoutView.addView(workoutName);
        workoutView.addView(workoutDuration);
        workoutView.addView(workoutTarget);
        workoutView.addView(workoutEquipment);
        workoutView.addView(workoutDifficulty);

        dataHolder.addView(workoutView);

        View view = new View(context);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                1
        );

        layoutParams.setMargins(10, 10, 10, 10);

        view.setBackgroundColor(context.getResources().getColor(android.R.color.darker_gray));

        view.setLayoutParams(layoutParams);

        dataHolder.addView(view);

        String exerciseList = data.optString("Exercises");

        JSONArray jsonArray;
        try {
            jsonArray = new JSONArray(exerciseList);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        // For every exercise, we create a box containing the details.
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject workoutObject;

            try {
                workoutObject = jsonArray.getJSONObject(i);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

            LinearLayout textHolder = new LinearLayout(context);
            textHolder.setOrientation(LinearLayout.VERTICAL);

            TextView exerciseNameText = new TextView(context);
            TextView exerciseDescriptionText = new TextView(context);
            TextView exerciseTargetMuscleGroupText = new TextView(context);
            TextView exerciseEquipmentText = new TextView(context);
            TextView exerciseDifficultyText = new TextView(context);
            TextView exerciseSetsText = new TextView(context);
            TextView exerciseRepsText = new TextView(context);
            TextView exerciseTimeText = new TextView(context);

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

            SpannableStringBuilder sbEquipment = new SpannableStringBuilder("Exercise Equipment: " + workoutObject.optString("Equipment"));
            sbEquipment.setSpan(bss, 0, 18, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            exerciseEquipmentText.setText(sbEquipment);

            SpannableStringBuilder sbDifficulty = new SpannableStringBuilder("Exercise Difficulty: " + workoutObject.optString("Difficulty"));
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

            dataHolder.addView(textHolder);

            View view2 = new View(context);

            LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    1
            );

            layoutParams2.setMargins(10, 10, 10, 10);

            view2.setBackgroundColor(context.getResources().getColor(android.R.color.darker_gray));

            view2.setLayoutParams(layoutParams2);

            dataHolder.addView(view2);
        }
    }
}