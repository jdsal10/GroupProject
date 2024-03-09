package com.firstapp.group10app.Pages;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.firstapp.group10app.DB.DbConnection;
import com.firstapp.group10app.Other.OnlineChecks;
import com.firstapp.group10app.Other.Session;
import com.firstapp.group10app.Pages.Fragments.Workouts.WorkoutAi2;
import com.firstapp.group10app.Pages.Fragments.Workouts.WorkoutManual;
import com.firstapp.group10app.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class WorkoutOption extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener, NavigationBarView.OnItemSelectedListener {
    public RadioButton AISelect, manualSelect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_option);

        updateView(new WorkoutManual(), -1, -1);

        // Initialize RadioButtons
        AISelect = findViewById(R.id.toggleAI);
        manualSelect = findViewById(R.id.toggleManual);

        // Set OnCheckedChangeListener
        AISelect.setOnCheckedChangeListener(this);
        manualSelect.setOnCheckedChangeListener(this);
        AISelect.setBackground(getDrawable(R.drawable.rounded_button_white));
        manualSelect.setBackground(getDrawable(R.drawable.rounded_button_tetradic2));

        // If the user is not signed in / anonymous, they do not access to the AI or to create a workout.
        if ((!Session.dbStatus) || (!Session.signedIn)) {
            AISelect.setEnabled(false);
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
                if ((!Session.signedIn) || (!DbConnection.testConnection())) {
                    Toast.makeText(this, "No connection!", Toast.LENGTH_SHORT).show();
                    // Need to add capability to enable somehow.
                    // AISelect.setEnabled(false);
                } else {
                    System.out.println("WorkoutOption.onClick(): AI selected");
                    updateView(new WorkoutAi2(), R.anim.slide_right_in, R.anim.slide_left_out);

                    AISelect.setBackground(AppCompatResources.getDrawable(this, R.drawable.rounded_button_tetradic2));
                    manualSelect.setBackground(AppCompatResources.getDrawable(this, R.drawable.rounded_button_white));
                }
            } else if (buttonView.getId() == R.id.toggleManual) {
                System.out.println("WorkoutOption.onCheckedChanged(): Manual selected");
                updateView(new WorkoutManual(), R.anim.slide_left_in, R.anim.slide_right_out);

                AISelect.setBackground(AppCompatResources.getDrawable(this, R.drawable.rounded_button_white));
                manualSelect.setBackground(AppCompatResources.getDrawable(this, R.drawable.rounded_button_tetradic2));
            }
        }
    }

    public void updateView(Fragment newView, int enterAnim, int exitAnim) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (enterAnim != -1 && exitAnim != -1) transaction.setCustomAnimations(enterAnim, exitAnim);
        transaction.replace(R.id.workoutsBody, newView);
        transaction.addToBackStack(null);
        transaction.commit();
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