package com.firstapp.group10app.Other;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class StepCounter extends AppCompatActivity implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private int totalStepsCount;
    private TextView stepView;

    public StepCounter() {
        Log.d("StepCounter", "Started");
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

        //provisionally removed
//        stepView = findViewById(R.id.stepTemp);

        if (acceleration > 25) {
            totalStepsCount++;
            Log.d("StepCounter", "Steps: " + totalStepsCount);
            stepView.setText("Steps: " + totalStepsCount);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
}