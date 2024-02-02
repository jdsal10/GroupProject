package com.firstapp.group10app.Pages;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.firstapp.group10app.DB.DBConnection;
import com.firstapp.group10app.DB.DBHelper;
import com.firstapp.group10app.Other.JSONToDB;
import com.firstapp.group10app.Other.Session;
import com.firstapp.group10app.Other.onlineChecks;
import com.firstapp.group10app.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.ResultSet;
import java.util.Arrays;

public class Home extends AppCompatActivity implements View.OnClickListener, NavigationBarView.OnItemSelectedListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // For now, a check should run at the start of each file for DB connection.
        Session.dbStatus = DBConnection.testConnection();
        System.out.println("STATUS: " + Session.dbStatus);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Button b = findViewById(R.id.TEMPWORK);
        b.setOnClickListener(this);

        Button settingsbtn = findViewById(R.id.goToSettings);
        settingsbtn.setOnClickListener(this);

        // Declare bottom taskbar
        BottomNavigationView bottomNavigationView = findViewById(R.id.mainNavigation);
        bottomNavigationView.setSelectedItemId(R.id.goToHome);
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
            startActivity(new Intent(getApplicationContext(), History.class));
            return true;
        }
        return true;
    }




        @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.goToSettings) {
            startActivity(new Intent(Home.this, Settings.class));
        }
    }
}