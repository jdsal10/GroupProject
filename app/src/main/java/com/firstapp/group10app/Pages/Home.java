package com.firstapp.group10app.Pages;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.firstapp.group10app.Other.Session;
import com.firstapp.group10app.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class Home extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener, View.OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Declare bottom taskbar
        BottomNavigationView bottomNavigationView = findViewById(R.id.mainNavigation);
        bottomNavigationView.setSelectedItemId(R.id.goHome);
        bottomNavigationView.setOnItemSelectedListener(this);

        Button tempButton = findViewById(R.id.TEMPWORK);
        tempButton.setOnClickListener(this);

            if((!Session.dbStatus) || (!Session.signedIn)){
            bottomNavigationView.getMenu().findItem(R.id.goSettings).setEnabled(false);
            bottomNavigationView.getMenu().findItem(R.id.goStats).setEnabled(false);
        }
    }

    @Override
    public boolean onNavigationItemSelected (MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.goSettings) {
            startActivity(new Intent(getApplicationContext(), Settings.class));
            return true;
        }
        else if(id == R.id.goStats) {
            //Code to navigate to stats
            return true;
        }
        else if(id == R.id.goHome) {
            startActivity(new Intent(getApplicationContext(),Home.class));
            return true;
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.TEMPWORK) {
            startActivity(new Intent(Home.this, createOrSearch.class));
        }
    }
}