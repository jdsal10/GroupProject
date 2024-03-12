package com.firstapp.group10app.Pages;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.firstapp.group10app.Other.Session;
import com.firstapp.group10app.Pages.Fragments.MainOptions.History;
import com.firstapp.group10app.Pages.Fragments.MainOptions.Home;
import com.firstapp.group10app.Pages.Fragments.MainOptions.WorkoutOption;
import com.firstapp.group10app.Pages.Fragments.Other.Settings;
import com.firstapp.group10app.R;
import com.firstapp.group10app.databinding.ActivityContainerBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class ActivityContainer extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener, View.OnClickListener {
    // This is a public variable that is used to store the current view.
    public static int currentView;  // 1 = Home, 2 = Workouts, 3 = History; else = no info
    public static final int HOME = 1, WORKOUTS = 2, HISTORY = 3;
    private TextView pageTitle, pageWelcome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        com.firstapp.group10app.databinding.ActivityContainerBinding binding = ActivityContainerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Button goSettings = findViewById(R.id.goToSettings);
        if (Session.signedIn == null || !Session.signedIn) {
            goSettings.setVisibility(View.GONE);
        } else {
            goSettings.setOnClickListener(this);
        }

        pageTitle = findViewById(R.id.pageTitle);

        BottomNavigationView bottomNavigationView = findViewById(R.id.mainNavigation);
        bottomNavigationView.setOnItemSelectedListener(this);

        // If the current view is not set, set it to 1 (Home = default).
        if (currentView != HOME && currentView != R.layout.activity_home
                && currentView != WORKOUTS && currentView != R.layout.activity_workout_option
                && currentView != HISTORY && currentView != R.layout.activity_history) {
            System.out.println("ActivityContainer.onCreate: currentView static var not set, setting to 2 (Workouts)");
            currentView = HOME;
        }

        if (currentView == HOME || currentView == R.layout.activity_home) {
            bottomNavigationView.getMenu().findItem(R.id.goToHome).setChecked(true);

            updateView(new Home());
        } else if (currentView == WORKOUTS || currentView == R.layout.activity_workout_option) {
            bottomNavigationView.getMenu().findItem(R.id.goToWorkouts).setChecked(true);

            updateView(new WorkoutOption());
        } else {
            bottomNavigationView.getMenu().findItem(R.id.goToSettings).setChecked(true);

            updateView(new History());
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.goToSettings) {
            updateView(new Settings());
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if ((id == R.id.goToHome) && (currentView != 1)) {
            updateView(new Home(), false);
            currentView = 1;
        } else if ((id == R.id.goToWorkouts) && (currentView != 2)) {
            updateView(new WorkoutOption(), currentView < 2);
            currentView = 2;
        } else if ((id == R.id.goToHistory) && (currentView != 3)) {
            updateView(new History(), true);
            currentView = 3;
        }

        return true;
    }

    // Update the fragment view with animation
    private void updateView(Fragment view, boolean animationDirection) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (animationDirection) {
            fragmentTransaction.setCustomAnimations(R.anim.slide_right_in, R.anim.slide_left_out);
        } else {
            fragmentTransaction.setCustomAnimations(R.anim.slide_left_in, R.anim.slide_right_out);
        }

        fragmentTransaction.replace(R.id.fragmentHolder, view);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

        setPageTitle(view);
    }

    // Update the fragment view
    private void updateView(Fragment view) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentHolder, view);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

        setPageTitle(view);
    }


    // Set the page title based on the current view.
    private void setPageTitle(Fragment view) {
        switch (view.getClass().getSimpleName()) {
            case "Home":
                currentView = HOME;
                pageTitle.setText("Home Page");
                break;
            case "WorkoutOption":
                currentView = WORKOUTS;
                pageTitle.setText("Workouts Page");
                break;
            case "History":
                currentView = HISTORY;
                pageTitle.setText("History Page");
                break;
        }
    }

    // Start new activity
    public void startNewActivity(AppCompatActivity newActivity, int enterAnim, int exitAnim) {
        startActivity(new Intent(this, newActivity.getClass()));
        overridePendingTransition(enterAnim, exitAnim);
    }
}