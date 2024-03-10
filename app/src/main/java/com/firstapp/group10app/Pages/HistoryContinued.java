package com.firstapp.group10app.Pages;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import androidx.appcompat.app.AppCompatActivity;

import com.firstapp.group10app.DB.DbHelper;
import com.firstapp.group10app.Other.ItemVisualiser;
import com.firstapp.group10app.Other.OnlineChecks;
import com.firstapp.group10app.Other.Session;
import com.firstapp.group10app.Pages.Fragments.MainOptions.Home;
import com.firstapp.group10app.Pages.Fragments.MainOptions.WorkoutOption;
import com.firstapp.group10app.R;
import com.firstapp.group10app.container;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class HistoryContinued extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {
    LinearLayout continuedLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_continued);

        ScrollView historyContinuedScrollView = findViewById(R.id.historyElementsContinued);
        continuedLayout = new LinearLayout(this);
        continuedLayout.setOrientation(LinearLayout.VERTICAL);

        historyContinuedScrollView.addView(continuedLayout);

        try {
            // gets the workouts user has done, specific to the user
            String HistoryContinuedJSON = DbHelper.getUserWorkouts(Session.userEmail);
            ItemVisualiser.startWorkoutGeneration(HistoryContinuedJSON, this, continuedLayout, "tt", R.layout.popup_history, R.id.popupHistory);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // Declare bottom taskbar
        BottomNavigationView bottomNavigationView = findViewById(R.id.mainNavigation);
        bottomNavigationView.setSelectedItemId(R.id.goToHistory);
        bottomNavigationView.setOnItemSelectedListener(this);

        // Checks if the view should be disabled.
        OnlineChecks.checkNavigationBar(bottomNavigationView);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.goToHome) {
            Intent intent = new Intent(getApplicationContext(), container.class);
            container.currentView = R.layout.activity_home;
            startActivity(intent);

            return true;
        } else if (id == R.id.goToWorkouts) {
            Intent intent = new Intent(getApplicationContext(), container.class);
            container.currentView = R.layout.activity_workout_option;
            startActivity(intent);

            return true;
        } else if (id == R.id.goToHistory) {
            Intent intent = new Intent(getApplicationContext(), container.class);
            container.currentView = R.layout.activity_history;
            startActivity(intent);

            return true;
        }
        return true;
    }
}