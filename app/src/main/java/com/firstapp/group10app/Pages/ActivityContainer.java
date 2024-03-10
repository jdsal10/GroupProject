package com.firstapp.group10app.Pages;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.firstapp.group10app.Other.Session;
import com.firstapp.group10app.Pages.Fragments.MainOptions.History;
import com.firstapp.group10app.Pages.Fragments.MainOptions.Home;
import com.firstapp.group10app.Pages.Fragments.MainOptions.WorkoutOption;
import com.firstapp.group10app.R;
import com.firstapp.group10app.databinding.ActivityContainerBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class ActivityContainer extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener, View.OnClickListener {
    private ActivityContainerBinding binding;
    // This is a public variable that is used to store the current view.
    public static int currentView;  // 1 = Home, 2 = Workouts, 3 = History; else = no info
    private static int HOME = 1, WORKOUTS = 2, HISTORY = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityContainerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Button goSettings = findViewById(R.id.goToSettings);
        goSettings.setOnClickListener(this);

        BottomNavigationView bottomNavigationView = findViewById(R.id.mainNavigation);
        bottomNavigationView.setOnItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.goToHome);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // If the current view is not set, set it to 1 (Home = default).
        if (currentView != HOME && currentView != R.layout.activity_home
                && currentView != WORKOUTS && currentView != R.layout.activity_workout_option
                && currentView != HISTORY && currentView != R.layout.activity_history) {
            currentView = HOME;
        }

        if (currentView == HOME || currentView == R.layout.activity_home) {
            Home fragment = new Home();
            fragmentTransaction.add(R.id.fragmentHolder, fragment);
            fragmentTransaction.commit();
        } else if (currentView == WORKOUTS || currentView == R.layout.activity_workout_option) {
            WorkoutOption fragment = new WorkoutOption();
            fragmentTransaction.add(R.id.fragmentHolder, fragment);
            fragmentTransaction.commit();
        } else if (currentView == HISTORY || currentView == R.layout.activity_history) {
            History fragment = new History();
            fragmentTransaction.add(R.id.fragmentHolder, fragment);
            fragmentTransaction.commit();
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.goToSettings) {
            Toast.makeText(this, "Settings is currently disabled!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.goToHome) {
            updateView(new Home());
            return true;
        } else if (id == R.id.goToWorkouts) {
            updateView(new WorkoutOption());
            return true;
        } else if (id == R.id.goToHistory) {
            updateView(new History());
            return true;
        }
        return true;
    }

    public void updateView(Fragment view) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentHolder, view);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void updateView() {
        if (currentView == HOME || currentView == R.layout.activity_home) {
            Home fragment = new Home();
            updateView(fragment);
        } else if (currentView == WORKOUTS || currentView == R.layout.activity_workout_option) {
            WorkoutOption fragment = new WorkoutOption();
            updateView(fragment);
        } else if (currentView == HISTORY || currentView == R.layout.activity_history) {
            History fragment = new History();
            updateView(fragment);
        }

        // Default to Home if no view is set.
        else {
            Home fragment = new Home();
            updateView(fragment);
        }
    }
}