package com.firstapp.group10app.Other;

import static com.firstapp.group10app.DB.DBHelper.linkExercise;

import com.firstapp.group10app.DB.DBHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

public class JSONToDB {
    public static void insertWorkout(JSONObject data, ArrayList<String> exerciseID) throws JSONException {
        String[] workoutDetails = new String[5];

        workoutDetails[0] = data.getString("WorkoutName");
        workoutDetails[1] = data.getString("WorkoutDuration");
        workoutDetails[2] = data.getString("TargetMuscleGroup");
        workoutDetails[3] = data.getString("Equipment");
        workoutDetails[4] = data.getString("Difficulty");

        Integer id = DBHelper.insertWorkout(workoutDetails);

        System.out.println(Arrays.toString(workoutDetails));

        for (String e : exerciseID) {
            int eid = Integer.parseInt(e);
        linkExercise(id, eid);
    }}
}
