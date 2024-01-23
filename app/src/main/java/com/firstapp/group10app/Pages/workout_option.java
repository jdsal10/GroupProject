package com.firstapp.group10app.Pages;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import com.firstapp.group10app.R;
import com.google.android.material.navigation.NavigationBarView;

public class workout_option extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener, View.OnClickListener, NavigationBarView.OnItemSelectedListener {

    public RadioButton AISelect;
    public RadioButton manualSelect;
    public LinearLayout aiView, manualView;
    public Button goCreate, goSearch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_option);

        // Initialise Buttons
        goCreate = findViewById(R.id.goToCreate);
        goSearch = findViewById(R.id.goToSearch);

        // Set click listener
        goCreate.setOnClickListener(this);
        goSearch.setOnClickListener(this);

        // Initialize RadioButtons
        AISelect = findViewById(R.id.toggleAI);
        manualSelect = findViewById(R.id.toggleManual);

        // Set OnCheckedChangeListener
        AISelect.setOnCheckedChangeListener(this);
        manualSelect.setOnCheckedChangeListener(this);

        aiView = findViewById(R.id.aiView);
        manualView = findViewById(R.id.manualView);
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            if (buttonView.getId() == R.id.toggleAI) {
                System.out.println("AI SELECT");
                manualView.setVisibility(View.GONE);
                aiView.setVisibility(View.VISIBLE);

            } else if (buttonView.getId() == R.id.toggleManual) {
                System.out.println("MAN SELECT");
                aiView.setVisibility(View.GONE);
                manualView.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.goToSearch) {
            startActivity(new Intent(workout_option.this, searchWorkout.class));
        } else if (id == R.id.goToCreate) {
            // Update with correct file when created!
            startActivity(new Intent(workout_option.this, Home.class));
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.goSettings) {
            startActivity(new Intent(getApplicationContext(), Settings.class));
            return true;
        } else if (id == R.id.goStats) {
            return true;
            //Code for stats
        } else if (id == R.id.goHome) {
            startActivity(new Intent(getApplicationContext(), Home.class));
            return true;
        }
        return true;
    }
}