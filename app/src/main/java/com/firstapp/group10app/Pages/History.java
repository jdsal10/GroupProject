package com.firstapp.group10app.Pages;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import androidx.appcompat.app.AppCompatActivity;

import com.firstapp.group10app.DB.DbHelper;
import com.firstapp.group10app.Other.ItemVisualiser;
import com.firstapp.group10app.Other.Session;
import com.firstapp.group10app.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class History extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener, View.OnClickListener {
    LinearLayout historyLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        ScrollView historyScrollView = findViewById(R.id.historyElements);

        historyLayout = new LinearLayout(this);
        historyLayout.setOrientation(LinearLayout.VERTICAL);

        historyScrollView.addView(historyLayout);

        try {
            String HistoryJSON = DbHelper.getUserWorkoutsLimited(Session.userEmail);
            if (HistoryJSON == null) {
                ItemVisualiser.showEmpty(historyLayout);
            } else {
                ItemVisualiser.startWorkoutGenerationLimiting(HistoryJSON, this, historyLayout, "tt", R.layout.popup_history, R.id.popupHistory);
                System.out.println("limited printing: " + HistoryJSON);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        //button to view all history
        Button settingsBtn = findViewById(R.id.goToSettings);
        settingsBtn.setOnClickListener(this);
        // Declare bottom taskbar
        BottomNavigationView bottomNavigationView = findViewById(R.id.mainNavigation);
        bottomNavigationView.setSelectedItemId(R.id.goToHistory);
        bottomNavigationView.setOnItemSelectedListener(this);

        // Checks if the view should be disabled.
        // OnlineChecks.checkNavigationBar(bottomNavigationView);
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

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.goToSettings) {
            startActivity(new Intent(History.this, HistoryContinued.class));
        }
    }
}