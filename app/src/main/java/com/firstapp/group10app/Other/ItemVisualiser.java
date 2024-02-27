package com.firstapp.group10app.Other;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.firstapp.group10app.DB.DBHelper;
import com.firstapp.group10app.Pages.MainActivity;
import com.firstapp.group10app.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ItemVisualiser {
    static LinearLayout box, workoutLayout;
    static Context cThis;
    static int exerciseID, popID;

    public static void addDetails(JSONObject details, String buttonType) {
        LayoutInflater inflate = (LayoutInflater) cThis.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        box = (LinearLayout) inflate.inflate(R.layout.activity_workout_view, null);

        TextView nameView = box.findViewById(R.id.workoutNameView);
        TextView durationView = box.findViewById(R.id.workoutDurationView);
        TextView muscleView = box.findViewById(R.id.workoutMuscleView);
        TextView equipmentView = box.findViewById(R.id.workoutEquipmentView);
        TextView difficultyView = box.findViewById(R.id.workoutDifficultyView);

        ImageView workoutImage = box.findViewById(R.id.workoutImage);

        box.setId(details.optInt("WorkoutID"));

        // Sets the textViews to the appropriate details.
        nameView.setText(String.format(details.optString("WorkoutName", "")));
        durationView.setText(String.format("Workout Duration: %s minutes", details.optString("WorkoutDuration", "")));
        muscleView.setText(String.format("Target Muscle Group: %s", details.optString("TargetMuscleGroup", "")));
        equipmentView.setText(String.format("Equipment: %s", details.optString("Equipment", "")));
        difficultyView.setText(String.format("Difficulty: %s", details.optString("Difficulty", "")));

        workoutImage.setImageResource(R.drawable.icon_workout);
        String exerciseList = details.optString("Exercises");

        // Adds to a linear layout.
        workoutLayout.addView(box);

        // Creates a block
        View view = new View(cThis);

        LinearLayout.LayoutParams layoutParamsView = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                1
        );

        layoutParamsView.setMargins(10, 10, 10, 10);

        view.setBackgroundColor(cThis.getResources().getColor(android.R.color.darker_gray));

        view.setLayoutParams(layoutParamsView);

        workoutLayout.addView(view);

        // For now, clicking on a workout shows the exercises - may make easier later.
        box.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(cThis);

            View popupView = inflate.inflate(popID, null);
            builder.setView(popupView);
            AlertDialog alertDialog = builder.create();
            alertDialog.show();

            ScrollView exerciseMainView = popupView.findViewById(exerciseID);

            exerciseMainView.removeAllViews();

            if (buttonType.equals("search")) {
                addSearchButtons(popupView, alertDialog);
            }

            LinearLayout exerciseLayout = new LinearLayout(cThis);
            exerciseLayout.setOrientation(LinearLayout.VERTICAL);

            // Creates a layout containing the exercise boxes.
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

                LinearLayout exerciseBox = (LinearLayout) inflate.inflate(R.layout.activity_exercise_view, null);

                // Set margins to the exerciseBox
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

                layoutParams.setMargins(0, 0, 0, 10); // left, top, right, bottom
                exerciseBox.setLayoutParams(layoutParams);

                exerciseMainView.removeView(exerciseBox);

                TextView exerciseNameView = exerciseBox.findViewById(R.id.exerciseNameView);
                TextView exerciseDescriptionView = exerciseBox.findViewById(R.id.exerciseDescriptionView);
                TextView exerciseTargetMuscleGroupView = exerciseBox.findViewById(R.id.exerciseTargetMuscleGroupView);
                TextView exerciseEquipmentView = exerciseBox.findViewById(R.id.exerciseEquipmentView);
                TextView exerciseSetsView = exerciseBox.findViewById(R.id.exerciseSetsView);
                TextView exerciseRepsView = exerciseBox.findViewById(R.id.exerciseRepsView);
                TextView exerciseTimeView = exerciseBox.findViewById(R.id.exerciseTimeView);


                ImageView exerciseImage = exerciseBox.findViewById(R.id.exerciseImage);
                View difficultyScale = exerciseBox.findViewById(R.id.difficulty);

                exerciseNameView.setText(String.format(workoutObject.optString("ExerciseName", "")));
                exerciseDescriptionView.setText(workoutObject.optString("Description", ""));
                exerciseTargetMuscleGroupView.setText(String.format("Exercise Target Group: %s", workoutObject.optString("TargetMuscleGroup", "")));
                exerciseEquipmentView.setText(String.format("Exercise Equipment: %s", workoutObject.optString("Equipment", "")));
                exerciseSetsView.setText(String.format("Sets: %s", workoutObject.optString("Sets", "")));
                exerciseRepsView.setText(String.format("Reps: %s", workoutObject.optString("Reps", "")));
                exerciseTimeView.setText(String.format("Time: %s", workoutObject.optString("Time", "")));


                exerciseImage.setImageResource(R.drawable.icon_workout);
                String difficultyValue = workoutObject.optString("Difficulty", "");

                switch (difficultyValue) {
                    case "Easy":
                        difficultyScale.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#00FF00")));
                        break;
                    case "Medium":
                        difficultyScale.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFFF00")));
                        break;
                    case "Hard":
                        difficultyScale.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF0000")));
                        break;
                }
                exerciseLayout.addView(exerciseBox);
            }

            // Adds the Linear layout containing all boxes to the scroll view.
            exerciseLayout.setPadding(0, 0, 0, 10);
            exerciseMainView.addView(exerciseLayout);
            alertDialog.show();
        });
    }

    public static void startWorkoutGeneration(String data, Context context, LinearLayout layout, String buttonType, int popupID, int exerciseScrollID) throws JSONException {
        cThis = context;
        workoutLayout = layout;
        exerciseID = exerciseScrollID;
        popID = popupID;

        if (data == null) {
            showEmpty(layout);
        } else {
            JSONArray jsonArray = new JSONArray(data);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject workoutObject = jsonArray.getJSONObject(i);
                addDetails(workoutObject, buttonType);

            }
        }
    }

    public static void showEmpty(LinearLayout layout) {
        TextView empty = new TextView(layout.getContext());
        empty.setGravity(1);

        empty.setText("No workouts were found");

//        Resource linking not working:
//        empty.setText("@strings/noWorkouts");

        layout.removeAllViews();
        layout.addView(empty);
    }


    // ALL BUTTON FUNCTIONS SHOULD BE DECLARED BELOW, WITH THE INPUTS BEING THE VIEW. CONTEXT AND
    // DIALOG GENERATED


    public static void addSearchButtons(View v, AlertDialog popup) {
        Button selectWorkout = v.findViewById(R.id.selectWorkout);
        selectWorkout.setOnClickListener(v1 -> {
            JSONObject workoutObject;
            String out = DBHelper.getAllWorkouts("WHERE w.WorkoutID = '" + box.getId() + "'");
            JSONArray jsonArray;

            try {
                jsonArray = new JSONArray(out);
                workoutObject = jsonArray.getJSONObject(0);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

            Session.selectedWorkout = workoutObject;
            System.out.println("Current workout: " + Session.selectedWorkout.toString());
            cThis.startActivity(new Intent(cThis, MainActivity.class));
        });

        Button closeWorkout = v.findViewById(R.id.closeExercise);
        closeWorkout.setOnClickListener(v1 -> popup.dismiss());
    }
}