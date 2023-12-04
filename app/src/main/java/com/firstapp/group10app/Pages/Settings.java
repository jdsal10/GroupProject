package com.firstapp.group10app.Pages;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.firstapp.group10app.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class Settings extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener, View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .commit();
        }

        // Navigation view declaration.
        BottomNavigationView settingNav = findViewById(R.id.settingsNavigation);
        settingNav.setSelectedItemId(R.id.goSettings);
        settingNav.setOnItemSelectedListener(this);

        // Button declaration.
        Button dataControlButton = findViewById(R.id.goDataControl);
        dataControlButton.setOnClickListener(this);

        Button accessibilityButton = findViewById(R.id.goAccessibility);
        accessibilityButton.setOnClickListener(this);

        Button accountButton = findViewById(R.id.goAccount);
        accountButton.setOnClickListener(this);
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

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.goDataControl) {
            //Go data
        } else if (id == R.id.goAccessibility) {
            //Go access
        } else if (id == R.id.goAccount) {
            //Go account
        }
    }
}