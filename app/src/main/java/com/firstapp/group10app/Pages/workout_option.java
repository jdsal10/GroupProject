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

import com.firstapp.group10app.Other.Session;
import com.firstapp.group10app.Other.onlineChecks;
import com.firstapp.group10app.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
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

        // Set Views
        aiView = findViewById(R.id.aiView);
        manualView = findViewById(R.id.manualView);

        // If the user is not signed in / anonymous, they do not access to the AI.
        if ((!Session.dbStatus) || (!Session.signedIn)) {
            AISelect.setEnabled(false);
        }

        BottomNavigationView bottomNavigationView = findViewById(R.id.mainNavigation);
        bottomNavigationView.setSelectedItemId(R.id.goToWorkouts);
        bottomNavigationView.setOnItemSelectedListener(this);

        onlineChecks.checkNavigationBar(bottomNavigationView);
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        // If ensure only one is selected at once
        if (isChecked) {
            if (buttonView.getId() == R.id.toggleAI) {
                manualView.setVisibility(View.GONE);
                aiView.setVisibility(View.VISIBLE);

            } else if (buttonView.getId() == R.id.toggleManual) {
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
        } else if (id == R.id.goToAI) {
//            Add when Misha's code is merged
//            startActivity(new Intent(workout_option.this, ));
        }
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
            // Code for history.
            return true;
        }
        return true;
    }
}