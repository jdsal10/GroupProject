package com.firstapp.group10app.Pages;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.firstapp.group10app.DB.DatabaseManager;
import com.firstapp.group10app.DB.LocalDb.LocalDbConnection;
import com.firstapp.group10app.Other.FragmentHolderUpdate;
import com.firstapp.group10app.Other.Session;
import com.firstapp.group10app.Pages.Fragments.MainOptions.History;
import com.firstapp.group10app.Pages.Fragments.MainOptions.Home;
import com.firstapp.group10app.Pages.Fragments.MainOptions.Workout;
import com.firstapp.group10app.Pages.Fragments.Other.Settings;
import com.firstapp.group10app.R;
import com.firstapp.group10app.databinding.ActivityContainerBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.Arrays;

public class ActivityContainer extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener, View.OnClickListener {
    // This is a public variable that is used to store the current view.
    public static int currentView;  // 1 = Home, 2 = Workouts, 3 = History; else = no info
    public static final int HOME = 1, WORKOUTS = 2, HISTORY = 3, SETTINGS = 4;
    private TextView pageTitle, pageWelcome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        com.firstapp.group10app.databinding.ActivityContainerBinding binding = ActivityContainerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Button goSettings = findViewById(R.id.goToSettings);

        pageTitle = findViewById(R.id.pageTitle);
        pageWelcome = findViewById(R.id.pageWelcome);

        // Set the welcome message in the header
//        new Thread(() -> {
//            Session.setUserName();
//            if (Session.getUserName() != null) {
//                pageWelcome.setText("Welcome, " + Session.getUserName() + "!");
//            }
//        }).start();

        // Took that out of the thread, because it would cause crashes
        // Error: Only the original thread that created a view hierarchy can touch its views.
        // TODO: Fix in the future if you have time
        Session.setUserName();
        if (Session.getUserName() != null) {
            pageWelcome.setText(String.format("Welcome, %s!", Session.getUserName()));
        }

        // Behaviour if signed in
        if (Session.getSignedIn()) {
            BottomNavigationView bottomNavigationView = findViewById(R.id.mainNavigation);
            bottomNavigationView.setVisibility(View.VISIBLE);
            bottomNavigationView.setOnItemSelectedListener(this);

            // If the current view is not set, set it to 1 (Home = default).
            if (currentView != HOME && currentView != R.layout.activity_home
                    && currentView != WORKOUTS && currentView != R.layout.activity_workout_option
                    && currentView != HISTORY && currentView != R.layout.activity_history) {
                Log.d("ActivityContainer.onCreate", "currentView static var not set, setting to 1 (Home)");
                currentView = HOME;
            }

            if (currentView == HOME || currentView == R.layout.activity_home) {
                bottomNavigationView.getMenu().findItem(R.id.goToHome).setChecked(true);
                bottomNavigationView.findViewById(R.id.goToHome).setBackgroundResource(R.drawable.navigationbar_rounded);
                updateView(new Home());
            } else if (currentView == WORKOUTS || currentView == R.layout.activity_workout_option) {
                bottomNavigationView.getMenu().findItem(R.id.goToWorkouts).setChecked(true);
                bottomNavigationView.findViewById(R.id.goToWorkouts).setBackgroundResource(R.drawable.navigationbar_rounded);
                updateView(new Workout());
            } else {
                bottomNavigationView.getMenu().findItem(R.id.goToSettings).setChecked(true);
                bottomNavigationView.findViewById(R.id.goToSettings).setBackgroundResource(R.drawable.navigationbar_rounded);
                updateView(new History());
            }

            goSettings.setVisibility(View.VISIBLE);
            goSettings.setOnClickListener(this);
        }

        // Behaviour if anonymous
        else {
            goSettings.setVisibility(View.GONE);

            // Hide the bottom navigation menu
            BottomNavigationView bottomNavigationView = findViewById(R.id.mainNavigation);
            bottomNavigationView.setVisibility(View.GONE);

            updateView(new Home());

            new Thread(() -> {
                boolean localDbIsConnected = true;
                try {
                    DatabaseManager.getInstance().getLocalDb();
                } catch (UnsupportedOperationException e) {
                    Log.i("Local DB Creation", "LocalDb is not connected");

                    try {
                        DatabaseManager.getInstance().connectToLocalDb(this);
                    } catch (Exception e1) {
                        Log.e("Local DB Creation", "MainActivity.onCreate cause an error");
                        Log.e("Local DB Creation", "toString(): " + e1);
                        Log.e("Local DB Creation", "getMessage(): " + e1.getMessage());
                        Log.e("Local DB Creation", "StackTrace: " + Arrays.toString(e1.getStackTrace()));

                        localDbIsConnected = false;
                    }
                }

                // Start the local database on a new thread
                if (!localDbIsConnected) {
                    try {
                        // Create and store the LocalDb instance
                        DatabaseManager.getInstance().connectToLocalDb(this);
                        LocalDbConnection localDbConnection = DatabaseManager.getInstance().getLocalDb();

                        // Insert sample data into the database
                        localDbConnection.insertSampleData();
                    } catch (Exception e) {
                        Log.e("Local DB Creation", "MainActivity.onCreate cause an error");
                        Log.e("Local DB Creation", "toString(): " + e);
                        Log.e("Local DB Creation", "getMessage(): " + e.getMessage());
                        Log.e("Local DB Creation", "StackTrace: " + Arrays.toString(e.getStackTrace()));
                    }
                }
            }).start();
        }

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("workoutHub")) {
            FragmentHolderUpdate.updateView(new WorkoutHub(), this);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.goToSettings) {
            BottomNavigationView bottomNavigationView = findViewById(R.id.mainNavigation);

            for (int i = 0; i < bottomNavigationView.getMenu().size(); i++) {
                MenuItem menuItem = bottomNavigationView.getMenu().getItem(i);
                View view = bottomNavigationView.findViewById(menuItem.getItemId());
                view.setBackgroundColor(Color.WHITE);
            }

            bottomNavigationView.getMenu().findItem(R.id.invisible).setChecked(false);

            currentView = SETTINGS;

            updateView(new Settings());
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        BottomNavigationView bottomNavigationView = findViewById(R.id.mainNavigation);
        int id = item.getItemId();

        for (int i = 0; i < bottomNavigationView.getMenu().size(); i++) {
            MenuItem menuItem = bottomNavigationView.getMenu().getItem(i);
            View view = bottomNavigationView.findViewById(menuItem.getItemId());
            view.setBackgroundColor(Color.WHITE);
        }

        // Change background for selected item
        View selectedView = bottomNavigationView.findViewById(item.getItemId());
        selectedView.setBackgroundResource(R.drawable.navigationbar_rounded);

        if ((id == R.id.goToHome) && (currentView != 1)) {
            if (currentView == SETTINGS) updateView(new Home());
            else updateView(new Home(), false);

            currentView = HOME;
        } else if ((id == R.id.goToWorkouts) && (currentView != 2)) {
            if (currentView == SETTINGS) updateView(new Workout());
            else updateView(new Workout(), currentView < 2);

            currentView = WORKOUTS;
        } else if ((id == R.id.goToHistory) && (currentView != 3)) {
            if (currentView == SETTINGS) updateView(new History());
            else updateView(new History(), true);

            currentView = HISTORY;
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
        pageTitle.setText(view.getClass().getSimpleName() + " Page");
    }

    // Start new activity
    public void startNewActivity(AppCompatActivity newActivity, int enterAnim, int exitAnim) {
        startActivity(new Intent(this, newActivity.getClass()));
        overridePendingTransition(enterAnim, exitAnim);
    }

    public void logOut() {
        Session.logout(this);
        finish();
    }
}