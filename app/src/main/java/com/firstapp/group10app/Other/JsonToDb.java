package com.firstapp.group10app.Other;

import static com.firstapp.group10app.DB.OnlineDb.OnlineDbHelper.linkExerciseToWorkout;

import android.util.Log;

import com.firstapp.group10app.DB.OnlineDb.OnlineDbHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class JsonToDb {
    public static Integer insertWorkout(JSONObject data, ArrayList<String> exerciseID) throws JSONException {
        String[] workoutDetails = new String[5];

        workoutDetails[0] = data.getString("WorkoutName");
        workoutDetails[1] = data.getString("WorkoutDuration");
        workoutDetails[2] = data.getString("TargetMuscleGroup");
        workoutDetails[3] = data.getString("Equipment");
        workoutDetails[4] = data.getString("Difficulty");

        Integer id = OnlineDbHelper.insertWorkout(workoutDetails);

        for (String e : exerciseID) {
            int eid = Integer.parseInt(e);
            linkExerciseToWorkout(id, eid);
        }
        return id;
    }

    public static Integer insertWorkoutAI(JSONObject data) throws JSONException {
        String[] workoutDetails = new String[5];

        workoutDetails[0] = data.getString("WorkoutName");
        workoutDetails[1] = data.getString("WorkoutDuration");
        workoutDetails[2] = data.getString("TargetMuscleGroup");
        workoutDetails[3] = data.getString("Equipment");
        workoutDetails[4] = data.getString("Difficulty");

        Integer id = OnlineDbHelper.insertWorkout(workoutDetails);

        String exerciseList = data.getString("Exercises");

        insertExercise(exerciseList, id);
        return id;
    }

    public static void insertExercise(String data, Integer id) throws JSONException {
        JSONArray exerciseArray = new JSONArray(data);
        Log.d("JsonToDb exerciseArray", exerciseArray.toString());
        JSONObject individualExercise;
        String[] exerciseData = new String[5];

        for (int i = 0; i < exerciseArray.length(); i++) {
            individualExercise = exerciseArray.getJSONObject(i);
            exerciseData[0] = individualExercise.getString("ExerciseName");
            exerciseData[1] = individualExercise.getString("Description");
            exerciseData[2] = individualExercise.getString("TargetMuscleGroup");
            exerciseData[3] = individualExercise.getString("Equipment");
            exerciseData[4] = individualExercise.getString("Difficulty");

            OnlineDbHelper.insertExercise(exerciseData, id);
        }
    }
}