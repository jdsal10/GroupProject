package com.firstapp.group10app.Pages;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import androidx.appcompat.app.AppCompatActivity;

import com.firstapp.group10app.DB.DBHelper;
import com.firstapp.group10app.Other.ItemVisualiser;
import com.firstapp.group10app.Other.OnlineChecks;
import com.firstapp.group10app.Other.Session;
import com.firstapp.group10app.R;
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
            String HistoryContinuedJSON = DBHelper.getUserWorkouts(Session.userEmail);
            ItemVisualiser.startWorkoutGeneration(HistoryContinuedJSON, this, continuedLayout, "tt", R.layout.historypopup, R.id.popupHistory);
            System.out.println(HistoryContinuedJSON);
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
            startActivity(new Intent(getApplicationContext(), Home.class));
            return true;
        } else if (id == R.id.goToWorkouts) {
            startActivity(new Intent(getApplicationContext(), WorkoutOption.class));
            return true;
        } else if (id == R.id.goToHistory) {
            return true;
        }
        return true;
    }
}