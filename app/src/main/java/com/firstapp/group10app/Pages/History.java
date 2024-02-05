package com.firstapp.group10app.Pages;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.firstapp.group10app.DB.DBHelper;
import com.firstapp.group10app.Other.ItemVisualiser;
import com.firstapp.group10app.Other.Session;
import com.firstapp.group10app.Other.onlineChecks;
import com.firstapp.group10app.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class History extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {
    private LinearLayout historyLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        ScrollView historyScrollView = findViewById(R.id.historyElements);


        historyLayout = new LinearLayout(this);
        historyLayout.setOrientation(LinearLayout.VERTICAL);

        historyScrollView.addView(historyLayout);

        try {
            //gets the workouts user has done, specific to the user
            String HistoryJSON = DBHelper.getUserWorkouts(Session.userEmail);
            ItemVisualiser.startWorkoutGeneration(HistoryJSON, this, historyLayout, "tt", R.layout.historypopup, R.id.popupHistory);
            System.out.println(HistoryJSON);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


        // Declare bottom taskbar
        BottomNavigationView bottomNavigationView = findViewById(R.id.mainNavigation);
        bottomNavigationView.setSelectedItemId(R.id.goToHistory);
        bottomNavigationView.setOnItemSelectedListener(this);

        // Checks if the view should be disabled.
        onlineChecks.checkNavigationBar(bottomNavigationView);

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
            return true;
        }
        return true;
    }
}