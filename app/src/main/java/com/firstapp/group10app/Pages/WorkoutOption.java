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

        updateView(new WorkoutManual());

        // Initialize RadioButtons
        AISelect = findViewById(R.id.toggleAI);
        manualSelect = findViewById(R.id.toggleManual);

        // Set OnCheckedChangeListener
        AISelect.setOnCheckedChangeListener(this);
        manualSelect.setOnCheckedChangeListener(this);
        AISelect.setBackground(getDrawable(R.drawable.rounded_button));
        manualSelect.setBackground(getDrawable(R.drawable.rounded_button_selected));

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
                    // TODO: I (Nick) commented out the following code because I changed the
                    //       Workout Option page to use fragments instead of layouts.
                    //       It would be best to find a way to re-implement this code, but for fragments.
//                    manualView.setAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_left_out));
//                    manualView.setVisibility(View.GONE);
//
//                    aiView.setAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_right_in));
//                    aiView.setVisibility(View.VISIBLE);

                    System.out.println("WorkoutOption.onClick(): AI selected");
                    updateView(new WorkoutAi2());

                    AISelect.setBackground(AppCompatResources.getDrawable(this, R.drawable.rounded_button_selected));
                    manualSelect.setBackground(AppCompatResources.getDrawable(this, R.drawable.rounded_button));
                }

            } else if (buttonView.getId() == R.id.toggleManual) {
                // TODO: I (Nick) commented out the following code because I changed the
                //       Workout Option page to use fragments instead of layouts.
                //       It would be best to find a way to re-implement this code, but for fragments.
//                aiView.setAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_right_out));
//                aiView.setVisibility(View.GONE);
//
//                manualView.setAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_left_in));
//                manualView.setVisibility(View.VISIBLE);

                System.out.println("WorkoutOption.onCheckedChanged(): Manual selected");
                updateView(new WorkoutManual());

                AISelect.setBackground(AppCompatResources.getDrawable(this, R.drawable.rounded_button));
                manualSelect.setBackground(AppCompatResources.getDrawable(this, R.drawable.rounded_button_selected));
            }
        }
    }

    public void updateView(Fragment newView) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.slide_right_in, R.anim.slide_left_out);
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