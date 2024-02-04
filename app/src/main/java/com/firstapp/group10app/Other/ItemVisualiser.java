package com.firstapp.group10app.Other;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.firstapp.group10app.DB.DBHelper;
import com.firstapp.group10app.Pages.MainActivity;
import com.firstapp.group10app.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.ArrayList;

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
            AlertDialog.Builder builder = new AlertDialog.Builder(cThis);

            View popupView = inflate.inflate(popID, null);
            builder.setView(popupView);
            AlertDialog alertDialog = builder.create();
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
                exerciseMainView.removeView(exerciseBox);

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

    public static void startWorkoutGeneration(String data, Context context, LinearLayout layout, String buttonType, int popupID, int exerciseScrollID) throws JSONException {
        cThis = context;
        workoutLayout = layout;
        exerciseID = exerciseScrollID;
        popID = popupID;

        if (data == null) {
            showEmpty();
        } else {
            JSONArray jsonArray = new JSONArray(data);
            for (int i = 0; i < jsonArray.length(); i++) {
                System.out.println("testing");
                JSONObject workoutObject = jsonArray.getJSONObject(i);
                addDetails(workoutObject, buttonType);

            }
        }
    }

    public static void showEmpty() {
        TextView empty = new TextView(workoutLayout.getContext());
        empty.setGravity(1);

        empty.setText("No workouts were found");

        workoutLayout.removeAllViews();
        workoutLayout.addView(empty);
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