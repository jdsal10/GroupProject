package com.firstapp.group10app.Pages;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.firstapp.group10app.R;

public class ExerciseView extends AppCompatActivity implements View.OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_exercise);

        Button selectWorkout = findViewById(R.id.selectWorkout);
        selectWorkout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.selectWorkout) {
            // Add workout to session info - potential idea
            Intent intent = new Intent(getApplicationContext(), ActivityContainer.class);
            ActivityContainer.currentView = R.layout.activity_home;
            startActivity(intent);
        }
    }
}