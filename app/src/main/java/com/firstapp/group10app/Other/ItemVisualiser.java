package com.firstapp.group10app.Other;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.firstapp.group10app.DB.DatabaseManager;
import com.firstapp.group10app.DB.LocalDb.WorkoutContract;
import com.firstapp.group10app.Pages.ActivityContainer;
import com.firstapp.group10app.Pages.WorkoutHub;
import com.firstapp.group10app.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ItemVisualiser {
    private static LinearLayout workoutLayout;
    private static Context context;
    static int exerciseID, popupID;

    public static void addDetails(JSONObject details, String buttonType) {
        LayoutInflater inflate = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout box = (LinearLayout) inflate.inflate(R.layout.element_workout_view, null);

        TextView nameView = box.findViewById(R.id.workoutNameView);
        TextView durationView = box.findViewById(R.id.workoutDurationView);
        TextView muscleView = box.findViewById(R.id.workoutMuscleView);
        TextView equipmentView = box.findViewById(R.id.workoutEquipmentView);
        TextView difficultyView = box.findViewById(R.id.workoutDifficultyView);

        ImageView workoutImage = box.findViewById(R.id.workoutImage);
        Log.e("WorkoutID", String.valueOf(details.optInt("WorkoutID")));
        box.setId(details.optInt("WorkoutID"));

        // Sets the textViews to the appropriate details.
        nameView.setText(String.format(details.optString("WorkoutName", "")));
        durationView.setText(String.format("Workout Duration: %s minutes", details.optString("WorkoutDuration", "")));
        muscleView.setText(String.format("Target Muscle Group: %s", details.optString("TargetMuscleGroup", "")));
        equipmentView.setText(String.format("Equipment: %s", details.optString("Equipment", "")));
        difficultyView.setText(String.format("Difficulty: %s", details.optString("Difficulty", "")));

        workoutImage.setImageResource(R.drawable.icon_workout);
        String exerciseList = details.optString("Exercises");

        // If day theme, set workout icon color to black (and vice versa)
        if (Session.getDeviceTheme(context) == Index.DAY) {
            workoutImage.setColorFilter(ContextCompat.getColor(context, R.color.black));
        } else {
            workoutImage.setColorFilter(ContextCompat.getColor(context, R.color.white));
        }

        // Adds to a linear layout.
        workoutLayout.addView(box);

        // Creates a block
        View view = new View(context);

        LinearLayout.LayoutParams layoutParamsView = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                1
        );

        layoutParamsView.setMargins(10, 10, 10, 10);

        view.setBackgroundColor(context.getResources().getColor(android.R.color.darker_gray));

        view.setLayoutParams(layoutParamsView);

        workoutLayout.addView(view);

        // For now, clicking on a workout shows the exercises - may make easier later.
        box.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());

            View popupView = inflate.inflate(popupID, null);
            builder.setView(popupView);
            AlertDialog alertDialog = builder.create();
            alertDialog.show();

            ScrollView exerciseMainView = popupView.findViewById(exerciseID);

            exerciseMainView.removeAllViews();

            // Adds button function if required.
            switch (buttonType) {
                case "search":
                    addSearchButtons(popupView, alertDialog, box.getId());
                    break;
                case "aiConfirm":
                    addCloseButton(popupView, alertDialog);
                    break;
                case "history":
                    addRedoButtons(popupView, alertDialog, box.getId());
                    break;
            }

            LinearLayout exerciseLayout = new LinearLayout(context);
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

                LinearLayout exerciseBox = (LinearLayout) inflate.inflate(R.layout.element_exercise_view, null);

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
                // If day theme, set workout icon color to black (and vice versa)
                if (Session.getDeviceTheme(context) == Index.DAY) {
                    workoutImage.setColorFilter(ContextCompat.getColor(context, R.color.black));
                } else {
                    workoutImage.setColorFilter(ContextCompat.getColor(context, R.color.white));
                }

                View difficultyScale = exerciseBox.findViewById(R.id.difficulty);
                TextView difficultyText = exerciseBox.findViewById(R.id.difficultyText);

                exerciseNameView.setText(String.format(workoutObject.optString("ExerciseName", "")));
                exerciseDescriptionView.setText(workoutObject.optString("Description", ""));
                exerciseTargetMuscleGroupView.setText(String.format("Exercise Target Group: %s", workoutObject.optString("TargetMuscleGroup", "")));
                exerciseEquipmentView.setText(String.format("Exercise Equipment: %s", workoutObject.optString("Equipment", "")));

                if (workoutObject.optString("Sets", "").equals("null")) {
                    exerciseSetsView.setVisibility(View.GONE);
                } else {
                    exerciseSetsView.setText(String.format("Sets: %s", workoutObject.optString("Sets", "")));
                }

                if (workoutObject.optString("Reps", "").equals("null")) {
                    exerciseRepsView.setVisibility(View.GONE);
                } else {
                    exerciseRepsView.setText(String.format("Reps: %s", workoutObject.optString("Reps", "")));
                }

                if (workoutObject.optString("Time", "").equals("null")) {
                    exerciseTimeView.setVisibility(View.GONE);
                } else {
                    exerciseTimeView.setText(String.format("Time: %s", workoutObject.optString("Time", "")));
                }

                exerciseImage.setImageResource(R.drawable.icon_workout);
                String difficultyValue = workoutObject.optString("Difficulty", "");

                // If day theme, set workout icon color to black (and vice versa)
                if (Session.getDeviceTheme(context) == Index.DAY) {
                    workoutImage.setColorFilter(ContextCompat.getColor(context, R.color.black));
                } else {
                    workoutImage.setColorFilter(ContextCompat.getColor(context, R.color.white));
                }

                switch (difficultyValue) {
                    case "Easy":
                        difficultyScale.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.pastel_green)));
                        difficultyText.setText("Easy");
                        difficultyText.setTextColor(ContextCompat.getColor(context, R.color.white));
                        difficultyText.setTextSize(14);
                        break;
                    case "Medium":
                        difficultyScale.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.pastel_yellow)));
                        difficultyText.setText("Medium");
                        difficultyText.setTextColor(ContextCompat.getColor(context, R.color.black));
                        difficultyText.setTextSize(14);
                        break;
                    case "Hard":
                        difficultyScale.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.pastel_red)));
                        difficultyText.setText("Hard");
                        difficultyText.setTextColor(ContextCompat.getColor(context, R.color.white));
                        difficultyText.setTextSize(14);
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
        ItemVisualiser.context = context;
        workoutLayout = layout;
        exerciseID = exerciseScrollID;
        ItemVisualiser.popupID = popupID;

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

    public static void startWorkoutGenerationLimiting(String data, Context context, LinearLayout layout, String buttonType, int popupID, int exerciseScrollID) throws JSONException {
        ItemVisualiser.context = context;
        workoutLayout = layout;
        exerciseID = exerciseScrollID;
        ItemVisualiser.popupID = popupID;

        if (data == null) {
            showEmpty(layout);
        } else {
            JSONArray jsonArray = new JSONArray(data);
            int length = Math.min(jsonArray.length(), 4);
            for (int i = 0; i < length; i++) {
                JSONObject workoutObject = jsonArray.getJSONObject(i);
                addDetails(workoutObject, buttonType);
            }
        }
    }

    public static void startWorkoutGenerationAI(String data, Context context, LinearLayout layout, String buttonType, int popupID, int exerciseScrollID) throws JSONException {
        ItemVisualiser.context = context;
        workoutLayout = layout;
        exerciseID = exerciseScrollID;
        ItemVisualiser.popupID = popupID;

        if (data == null) {
            showEmpty(layout);
        } else {
            Log.e("ItemVisualiser.startWorkoutGenerationAI", data);
            if(data.startsWith("[") && data.endsWith("]")) {
                Log.e("ItemVisualiser.startWorkoutGenerationAI", "Removing brackets");
                data = data.substring(1, data.length() - 1);
            }
            JSONObject workoutObject = new JSONObject(data);
            addDetails(workoutObject, buttonType);
        }
    }

    public static void showEmpty(LinearLayout layout) {
        TextView empty = new TextView(layout.getContext());
        empty.setGravity(1);

        empty.setText("No workouts were found");

        layout.removeAllViews();
        layout.addView(empty);
    }


    // ALL BUTTON FUNCTIONS SHOULD BE DECLARED BELOW, WITH THE INPUTS BEING THE VIEW. CONTEXT AND
    // DIALOG GENERATED
    public static void addSearchButtons(View v, AlertDialog popup, int id) {
        Button selectWorkout = v.findViewById(R.id.selectWorkout);
        selectWorkout.setOnClickListener(v1 -> {
            JSONObject workoutObject;

            // Dirty fix of a SQL error that would happen in anonymous due to differences in the db instances
            String result;
            if (Session.getSignedIn()) {
                result = DatabaseManager.getInstance().getWorkoutsAsJsonArray("WHERE w.WorkoutID = '" + id + "'");
            } else {
                result = DatabaseManager.getInstance().getWorkoutsAsJsonArray("WHERE w." + WorkoutContract.WorkoutEntry._ID + " = '" + id + "'");
            }
            Log.d("ItemVisualiser.addSearchButtons", "result: " + result);

            JSONArray jsonArray;

            try {
                jsonArray = new JSONArray(result);
                workoutObject = jsonArray.getJSONObject(0);
            } catch (JSONException e) {
                Log.e("ItemVisualiser.addSearchButtons", "Error: " + e);
                throw new RuntimeException(e);
            }

            Session.setSelectedWorkout(workoutObject);
            Session.setWorkoutID(id);

            Intent intent = new Intent(context, ActivityContainer.class);
            intent.putExtra("workoutHub", WorkoutHub.class);
            context.startActivity(intent);
        });

        Button closeWorkout = v.findViewById(R.id.closeExercise);
        closeWorkout.setOnClickListener(v1 -> popup.dismiss());
    }

    private static void addRedoButtons(View popupView, AlertDialog alertDialog, int id) {
        Button redoWorkout = popupView.findViewById(R.id.redoExercise);
        redoWorkout.setOnClickListener(v1 -> {
            JSONObject workoutObject;

            // Dirty fix of a SQL error that would happen in anonymous due to differences in the db instances
            String result;
            if (Session.getSignedIn()) {
                result = DatabaseManager.getInstance().getWorkoutsAsJsonArray("WHERE w.WorkoutID = '" + id + "'");
            } else {
                result = DatabaseManager.getInstance().getWorkoutsAsJsonArray("WHERE w." + WorkoutContract.WorkoutEntry._ID + " = '" + workoutLayout.getId() + "'");
            }
            Log.d("ItemVisualiser.addRedoButtons", "result: " + result);

            JSONArray jsonArray;

            try {
                jsonArray = new JSONArray(result);
                workoutObject = jsonArray.getJSONObject(0);
            } catch (JSONException e) {
                Log.e("ItemVisualiser.addRedoButtons", "Error: " + e);
                throw new RuntimeException(e);
            }

            Session.setSelectedWorkout(workoutObject);
            Session.setWorkoutID(id);

            Intent intent = new Intent(context, ActivityContainer.class);
            intent.putExtra("workoutHub", WorkoutHub.class);
            context.startActivity(intent);
        });

        Button closeWorkout = popupView.findViewById(R.id.closeExercise);
        closeWorkout.setOnClickListener(v1 -> alertDialog.dismiss());
    }

    public static void addCloseButton(View v, AlertDialog popup) {
        Button closeWorkout = v.findViewById(R.id.closeButton);
        closeWorkout.setOnClickListener(v1 -> popup.dismiss());
    }
}