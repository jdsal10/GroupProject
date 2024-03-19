package com.firstapp.group10app.Pages;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.firstapp.group10app.DB.OnlineDb.OnlineDbHelper;
import com.firstapp.group10app.Other.ItemVisualiser;
import com.firstapp.group10app.Other.Session;
import com.firstapp.group10app.R;

public class HistoryContinued extends AppCompatActivity implements View.OnClickListener {
    private ImageButton backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Behaviour when the user is signed in
        if (Session.getSignedIn()) {
            setContentView(R.layout.activity_history_continued);

            backButton = findViewById(R.id.backButton);
            backButton.setOnClickListener(this);

            ScrollView historyContinuedScrollView = findViewById(R.id.historyElementsContinued);
            LinearLayout continuedLayout = new LinearLayout(this);
            continuedLayout.setOrientation(LinearLayout.VERTICAL);

            historyContinuedScrollView.addView(continuedLayout);

            try {
                // gets the workouts user has done, specific to the user
                String HistoryContinuedJSON = OnlineDbHelper.getUserWorkouts(Session.getUserEmail());
                ItemVisualiser.startWorkoutGeneration(HistoryContinuedJSON, this, continuedLayout, "tt", R.layout.popup_history, R.id.popupHistory);
            } catch (Exception e) {
                // TODO: handle exception better
                throw new RuntimeException(e);
            }
        }

        // Behaviour when the user is anonymous
        else {
            Log.e("WorkoutAI", "User is not signed in. This page should not be accessible.");
            Session.logout(this, "This page should not be accessible. You are being logged out");
            finish();
        }
    }

    @Override
    public void onClick(View v) {
        if (v == backButton) {
            finish();
        }
    }
}