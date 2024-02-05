package com.firstapp.group10app.Pages;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.view.View;


import com.firstapp.group10app.R;

public class workouts_ai extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workouts_ai);
        TextView generateButton = findViewById(R.id.searchButton);
        generateButton.setOnClickListener(this);
    }

    public void onClick(View v) {

    }
}
