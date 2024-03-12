package com.firstapp.group10app.Pages;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import androidx.appcompat.app.AppCompatActivity;

import com.firstapp.group10app.DB.DbHelper;
import com.firstapp.group10app.Other.ItemVisualiser;
import com.firstapp.group10app.Other.OnlineChecks;
import com.firstapp.group10app.Other.Session;
import com.firstapp.group10app.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HistoryContinued extends AppCompatActivity {
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
//        BottomNavigationView bottomNavigationView = findViewById(R.id.mainNavigation);
//        bottomNavigationView.setSelectedItemId(R.id.goToHistory);
//        bottomNavigationView.setOnItemSelectedListener(this);

        // Checks if the view should be disabled.
//        OnlineChecks.checkNavigationBar(bottomNavigationView);
    }
}