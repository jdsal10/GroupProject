package com.firstapp.group10app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.firstapp.group10app.Pages.Home;

public class exerciseView extends AppCompatActivity implements View.OnClickListener {

    private Button selectWorkout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_popup);
        
        selectWorkout = findViewById(R.id.selectWorkout);
        selectWorkout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.selectWorkout) {
            // Add workout to session info - potential idea
            startActivity(new Intent(exerciseView.this, Home.class));
        }
    }
}