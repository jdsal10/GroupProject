package com.firstapp.group10app.Pages;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.firstapp.group10app.DB.DBHelper;
import com.firstapp.group10app.R;
import com.google.android.material.navigation.NavigationBarView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;

public class searchWorkout extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {
    private LinearLayout mainLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_workout);

        ScrollView scrollView = findViewById(R.id.resultSearchWorkout);
        mainLayout = new LinearLayout(this);

        scrollView.addView(mainLayout);

        try {
            updateWorkouts();
        } catch (JSONException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateWorkouts() throws JSONException, SQLException {
        String input = DBHelper.test();
        System.out.println(input);
        JSONArray jsonArray = new JSONArray(input);

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject workoutObject = jsonArray.getJSONObject(i);
            addDetails(workoutObject);
        }
    }

    public void addDetails(JSONObject details) {
        LinearLayout box = (LinearLayout) getLayoutInflater().inflate(R.layout.workoutview, null);
        TextView viewDetails = box.findViewById(R.id.workoutDetailsTextView);
        viewDetails.setText(details.optString("WorkoutName", "") + " ++  " + details.optString("Exercises", ""));
        mainLayout.addView(box);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.goSettings) {
            startActivity(new Intent(getApplicationContext(), Settings.class));
            return true;
        } else if (id == R.id.goStats) {
            return true;
            //Code for stats
        } else if (id == R.id.goHome) {
            startActivity(new Intent(getApplicationContext(), Home.class));
            return true;
        }
        return true;
    }
}
