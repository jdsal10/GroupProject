package com.firstapp.group10app.Other;

import com.firstapp.group10app.DB.DBHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class JSONToDB {
    public static void insertWorkout(String data) throws JSONException {
        JSONObject workoutData = new JSONObject(data);
        String[] workoutDetails = new String[5];

        workoutDetails[0] = workoutData.getString("WorkoutName");
        workoutDetails[1] = workoutData.getString("WorkoutDuration");
        workoutDetails[2] = workoutData.getString("TargetMuscleGroup");
        workoutDetails[3] = workoutData.getString("Equipment");
        workoutDetails[4] = workoutData.getString("Difficulty");

        Integer id = DBHelper.insertWorkout(workoutDetails);

        System.out.println(Arrays.toString(workoutDetails));

        String exerciseList = workoutData.getString("exercises");

        insertExercise(exerciseList, id);
    }

    public static void insertExercise(String data, Integer id) throws JSONException {
        JSONArray exerciseArray = new JSONArray(data);
        JSONObject individualExercise;
        String[] exerciseData = new String[5];

        for (int i = 0; i < exerciseArray.length(); i++) {
            individualExercise = exerciseArray.getJSONObject(i);
            exerciseData[0] = individualExercise.getString("ExerciseName");
            exerciseData[1] = individualExercise.getString("Description");
            exerciseData[2] = individualExercise.getString("TargetMuscleGroup");
            exerciseData[3] = individualExercise.getString("Equipment");
            exerciseData[4] = individualExercise.getString("Difficulty");

            DBHelper.insertExercise(exerciseData, id);
            System.out.println(Arrays.toString(exerciseData));
        }
    }
}
