package com.firstapp.group10app.Pages;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.firstapp.group10app.DB.DBConnection;
import com.firstapp.group10app.DB.DBHelper;
import com.firstapp.group10app.Other.JSONToDB;
import com.firstapp.group10app.Other.Session;
import com.firstapp.group10app.Other.onlineChecks;
import com.firstapp.group10app.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.sql.ResultSet;
import java.util.Arrays;

public class Home extends AppCompatActivity implements View.OnClickListener, NavigationBarView.OnItemSelectedListener, SensorEventListener {
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private int totalStepsCount;
    private TextView stepView;

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

        stepView = findViewById(R.id.stepTemp);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager != null) {
            accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Reactive the sensor
        if (accelerometer != null) {
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float[] values = event.values;
        float x = values[0];
        float y = values[1];
        float z = values[2];

        // Calculate acceleration.
        float acceleration = (float) Math.sqrt(x * x + y * y + z * z);

        if (acceleration > 25) {
            totalStepsCount++;
            System.out.println("Steps: " + totalStepsCount);
            stepView.setText("Steps: " + totalStepsCount);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
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
            return true;
        }
        return true;
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.TEMPWORK) {
            try {
                String testData = "{\n" +
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
                JSONToDB.splitFunctionTest(testData);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
    }
}