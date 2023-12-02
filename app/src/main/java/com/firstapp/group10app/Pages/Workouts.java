package com.firstapp.group10app.Pages;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.firstapp.group10app.R;

/**
 * This class is responsible for displaying the workouts page.
 */
public class Workouts extends AppCompatActivity {
    private boolean AI; // true if in AI mode, false if in manual mode

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workouts);
    }
}