package com.firstapp.group10app.Pages;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;

import com.firstapp.group10app.DB.DBConnection;
import com.firstapp.group10app.Other.OnlineChecks;
import com.firstapp.group10app.Other.Session;
import com.firstapp.group10app.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class WorkoutOption extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener, View.OnClickListener, NavigationBarView.OnItemSelectedListener {
    public RadioButton AISelect, manualSelect;
    public LinearLayout aiView, manualView;
    public Button goCreate, goSearch, goAI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_option);

        // Initialise Buttons
        goCreate = findViewById(R.id.goToCreate);
        goSearch = findViewById(R.id.goToSearch);
        goAI = findViewById(R.id.goToAI);

        // Set click listener
        goCreate.setOnClickListener(this);
        goSearch.setOnClickListener(this);
        goAI.setOnClickListener(this);

        // Initialize RadioButtons
        AISelect = findViewById(R.id.toggleAI);
        manualSelect = findViewById(R.id.toggleManual);

        // Set OnCheckedChangeListener
        AISelect.setOnCheckedChangeListener(this);
        manualSelect.setOnCheckedChangeListener(this);
        AISelect.setBackground(getDrawable(R.drawable.rounded_button));
        manualSelect.setBackground(getDrawable(R.drawable.rounded_button_selected));

        // Set Views
        aiView = findViewById(R.id.aiView);
        manualView = findViewById(R.id.manualView);

        // If the user is not signed in / anonymous, they do not access to the AI or to create a workout.
        if ((!Session.dbStatus) || (!Session.signedIn)) {
            AISelect.setEnabled(false);

            // Need to confirm this works!
            manualView.setVisibility(View.GONE);
        }

        BottomNavigationView bottomNavigationView = findViewById(R.id.mainNavigation);
        bottomNavigationView.setSelectedItemId(R.id.goToWorkouts);
        bottomNavigationView.setOnItemSelectedListener(this);

        OnlineChecks.checkNavigationBar(bottomNavigationView);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        // If ensure only one is selected at once
        if (isChecked) {
            if (buttonView.getId() == R.id.toggleAI) {
                if ((!Session.signedIn) || (!DBConnection.testConnection())) {
                    Toast.makeText(this, "No connection!", Toast.LENGTH_SHORT).show();
                    // Need to add capability to enable somehow.
                    // AISelect.setEnabled(false);
                } else {
                    manualView.setAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_left_out));
                    manualView.setVisibility(View.GONE);

                    aiView.setAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_right_in));
                    aiView.setVisibility(View.VISIBLE);

                    AISelect.setBackground(AppCompatResources.getDrawable(this, R.drawable.rounded_button_selected));
                    manualSelect.setBackground(AppCompatResources.getDrawable(this, R.drawable.rounded_button));
                }

            } else if (buttonView.getId() == R.id.toggleManual) {
                aiView.setAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_right_out));
                aiView.setVisibility(View.GONE);

                manualView.setAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_left_in));
                manualView.setVisibility(View.VISIBLE);

                AISelect.setBackground(AppCompatResources.getDrawable(this, R.drawable.rounded_button));
                manualSelect.setBackground(AppCompatResources.getDrawable(this, R.drawable.rounded_button_selected));
            }
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.goToSearch) {
            startActivity(new Intent(WorkoutOption.this, SearchWorkout.class));
        } else if (id == R.id.goToCreate) {
            startActivity(new Intent(WorkoutOption.this, CreateWorkout.class));
        } else if (id == R.id.goToAI) {
            if ((!Session.signedIn) || (!DBConnection.testConnection())) {
                Toast.makeText(this, "No connection!", Toast.LENGTH_SHORT).show();
            } else {
                startActivity(new Intent(getApplicationContext(), WorkoutAi.class));
            }
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.goToHome) {
            startActivity(new Intent(getApplicationContext(), Home.class));
            return true;
        } else if (id == R.id.goToWorkouts) {
            startActivity(new Intent(getApplicationContext(), WorkoutOption.class));
            return true;
        } else if (id == R.id.goToHistory) {
            startActivity(new Intent(getApplicationContext(), History.class));
            return true;
        }
        return true;
    }
}