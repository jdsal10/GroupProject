package com.firstapp.group10app.Other;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class itemVisualiserText {

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
        for (
                JSONObject exercise : addedExercises) {
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

            exerciseNameText.setText(String.format(String.format("Exercise Name: %s", workoutObject.optString("ExerciseName", ""))));
            exerciseDescriptionText.setText(String.format("Exercise Description: %s", workoutObject.optString("Description", "")));
            exerciseTargetMuscleGroupText.setText(String.format("Exercise Target Group: %s", workoutObject.optString("TargetMuscleGroup", "")));
            exerciseEquipmentText.setText(String.format("Exercise Equipment: %s", workoutObject.optString("Equipment", "")));
            exerciseDifficultyText.setText(String.format("Exercise Difficulty: %s", workoutObject.optString("Difficulty")));

            textHolder.addView(exerciseNameText);
            textHolder.addView(exerciseDescriptionText);
            textHolder.addView(exerciseTargetMuscleGroupText);
            textHolder.addView(exerciseEquipmentText);
            textHolder.addView(exerciseDifficultyText);

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
