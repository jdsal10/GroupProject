package com.firstapp.group10app.Pages;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;

import com.firstapp.group10app.DB.DBConnection;
import com.firstapp.group10app.Other.Session;
import com.firstapp.group10app.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class Home extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // For now, a check should run at the start of each file for DB connection.
        Session.dbStatus = DBConnection.testConnection();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Declare bottom taskbar
        BottomNavigationView bottomNavigationView = findViewById(R.id.mainNavigation);
        bottomNavigationView.setSelectedItemId(R.id.goToHome);
        bottomNavigationView.setOnItemSelectedListener(this);


        // Seems to crash

//        if ((!Session.dbStatus) || (!Session.signedIn)) {
//            bottomNavigationView.getMenu().findItem(R.id.goToWorkouts).setEnabled(false);
////            Enable when history active
////            bottomNavigationView.getMenu().findItem(R.id.).setEnabled(false);
//        }
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
            // Code for history.
            return true;
        }
        return true;
    }
}