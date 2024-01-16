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

public class ItemVisualiser  {
    static ScrollView parentView;
    static View dialogView;

    static LinearLayout box;

    public static void addDetails(JSONObject details, Context context, LinearLayout layout, String buttonType, int test) {
        LayoutInflater inflate = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
        layout.addView(box);
        dialogView = inflate.inflate(test, null);

        // For now, clicking on a workout shows the exercises - may make easier later.
        box.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);

            // NEED TO CHANGE TO USER DEFINED VIEW

            View newDialogView = inflate.inflate(test, null);
            builder.setView(newDialogView);
            AlertDialog alertDialog = builder.create();
            ScrollView exerciseMainView = newDialogView.findViewById(R.id.exerciseMainView);
            exerciseMainView.removeAllViews();

            if(buttonType.equals("search")) {
                addSearchButtons(newDialogView, context);
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

    public static void addSearchButtons(View v, Context c) {
        Button b = v.findViewById(R.id.selectWorkout);
        b.setOnClickListener(v1 -> {
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
            c.startActivity(new Intent(c, MainActivity.class));            });
    }

    public static void updateWorkouts(String filter, Context context, LinearLayout layout, ScrollView view, String buttonType, int test) throws JSONException {
        String input = DBHelper.getAllWorkouts(filter);
        parentView = view;

        if (input == null) {
            showEmpty(parentView, context);
        } else {
            JSONArray jsonArray = new JSONArray(input);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject workoutObject = jsonArray.getJSONObject(i);
                addDetails(workoutObject, context, layout, buttonType, test);
            }
        }
    }

    public static void runFilter(String duration, String difficulty, String targetMuscle, Context context, LinearLayout layout, ScrollView sv, String buttonType, int test) throws SQLException, JSONException {
        ArrayList<String> toFilter = new ArrayList<>();
        parentView = sv;
        layout.removeAllViews();
        parentView.removeAllViews();
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
            updateWorkouts(null, context, layout, parentView, buttonType, test);
        } else {
            for (int i = 0; i < toFilter.size() - 1; i++) {
                filter.append(toFilter.get(i)).append(" AND");
            }
            filter.append(toFilter.get(toFilter.size() - 1));

            String newFilter = filter.toString();

            updateWorkouts(newFilter, context, layout, parentView, buttonType, test);
        }
    }

    public static void showEmpty(ScrollView view, Context context) {

        LinearLayout emptyLayout = new LinearLayout(context);
        emptyLayout.setOrientation(LinearLayout.VERTICAL);
        if (view != null) {
            view.removeAllViews();
            view.addView(emptyLayout);
        }
        TextView empty = new TextView(emptyLayout.getContext());

        empty.setText("No workouts were found");

        emptyLayout.addView(empty);

        view.removeAllViews();

        view.addView(emptyLayout);


    }

}