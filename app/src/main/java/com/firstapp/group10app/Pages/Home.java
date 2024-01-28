package com.firstapp.group10app.Pages;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.firstapp.group10app.DB.DBConnection;
import com.firstapp.group10app.DB.DBHelper;
import com.firstapp.group10app.Other.Session;
import com.firstapp.group10app.Other.onlineChecks;
import com.firstapp.group10app.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.ResultSet;
import java.util.Arrays;

public class Home extends AppCompatActivity implements View.OnClickListener, NavigationBarView.OnItemSelectedListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // For now, a check should run at the start of each file for DB connection.
        Session.dbStatus = DBConnection.testConnection();
        System.out.println("STATUS: " + Session.dbStatus);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Button b = findViewById(R.id.TEMPWORK);
        b.setOnClickListener(this);

        // Declare bottom taskbar
        BottomNavigationView bottomNavigationView = findViewById(R.id.mainNavigation);
        bottomNavigationView.setSelectedItemId(R.id.goToHome);
        bottomNavigationView.setOnItemSelectedListener(this);

        // Checks if the view should be disabled.
        onlineChecks.checkNavigationBar(bottomNavigationView);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.goToHome) {
            startActivity(new Intent(getApplicationContext(), Home.class));
            return true;
        } else if (id == R.id.goToWorkouts) {
            startActivity(new Intent(getApplicationContext(), workout_option.class));
            return true;
        } else if (id == R.id.goToHistory) {
            try {
                splitFunctionTest();
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            return true;
        }
        return true;
    }

    public void splitFunctionTest() throws JSONException {
        String data = "{\n" +
                "\"WorkoutName\": \"Full Body HIIT\",\n" +
                "\"WorkoutDuration\": 1.5,\n" +
                "\"TargetMuscleGroup\": \"Full Body\",\n" +
                "\"Equipment\": \"None\",\n" +
                "\"Difficulty\": 1,\n" +
                "\"exercises\": [\n" +
                "{\n" +
                "\"ExerciseName\": \"Jumping Jacks\",\n" +
                "\"Description\": \"Perform jumping jacks for 1 minute.\",\n" +
                "\"TargetMuscleGroup\": \"Cardio\",\n" +
                "\"Equipment\": \"None\",\n" +
                "\"Difficulty\": 1" +
                "},\n" +
                "{\n" +
                "\"ExerciseName\": \"Push-ups\",\n" +
                "\"Description\": \"Do 3 sets of 15 push-ups.\",\n" +
                "\"TargetMuscleGroup\": \"Chest, Shoulders, Triceps\",\n" +
                "\"Equipment\": \"None\",\n" +
                "\"Difficulty\": 2" +
                "},\n" +
                "{\n" +
                "\"ExerciseName\": \"Bodyweight Squats\",\n" +
                "\"Description\": \"Perform 4 sets of 20 bodyweight squats.\",\n" +
                "\"TargetMuscleGroup\": \"Legs\",\n" +
                "\"Equipment\": \"None\",\n" +
                "\"Difficulty\": 1" +
                "},\n" +
                "{\n" +
                "\"ExerciseName\": \"Plank\",\n" +
                "\"Description\": \"Hold a plank position for 2 minutes.\",\n" +
                "\"TargetMuscleGroup\": \"Core\",\n" +
                "\"Equipment\": \"None\",\n" +
                "\"Difficulty\": 3" +
                "},\n" +
                "{\n" +
                "\"ExerciseName\": \"Burpees\",\n" +
                "\"Description\": \"Complete 3 sets of 10 burpees.\",\n" +
                "\"TargetMuscleGroup\": \"Full Body\",\n" +
                "\"Equipment\": \"None\",\n" +
                "\"Difficulty\": 3" +
                "}\n" +
                "]\n" +
                "}";

        JSONObject workoutData = new JSONObject(data);
        String[] workoutDetails = new String[5];
        workoutDetails[0] = workoutData.getString("WorkoutName");
        workoutDetails[1] = workoutData.getString("WorkoutDuration");
        workoutDetails[2] = workoutData.getString("TargetMuscleGroup");
        workoutDetails[3] = workoutData.getString("Equipment");
        workoutDetails[4] = workoutData.getString("Difficulty");

        DBHelper.insertWorkout(workoutDetails);

        System.out.println(Arrays.toString(workoutDetails));

        String exerciseList = workoutData.getString("exercises");

        exerciseCreate(exerciseList);

    }

    public void exerciseCreate(String data) throws JSONException {
        JSONArray exerciseArray = new JSONArray(data);
        JSONObject individualExercise;
        String[] exerciseData = new String[5];
        for(int i = 0; i < exerciseArray.length(); i++ ){
            individualExercise = exerciseArray.getJSONObject(i);
            exerciseData[0] = individualExercise.getString("ExerciseName");
            exerciseData[1] = individualExercise.getString("Description");
            exerciseData[2] = individualExercise.getString("TargetMuscleGroup");
            exerciseData[3] = individualExercise.getString("Equipment");
            exerciseData[4] = individualExercise.getString("Difficulty");

            DBHelper.insertExercise(exerciseData);
            System.out.println(Arrays.toString(exerciseData));
        }
    }


        @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.TEMPWORK) {
            try {
                splitFunctionTest();
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
    }
}