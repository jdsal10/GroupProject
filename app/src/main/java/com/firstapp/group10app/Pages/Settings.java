package com.firstapp.group10app.Pages;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.firstapp.group10app.DB.DbConnection;
import com.firstapp.group10app.Other.OnlineChecks;
import com.firstapp.group10app.Pages.Fragments.Settings.SettingsAccessibility;
import com.firstapp.group10app.Pages.Fragments.Settings.SettingsAccount;
import com.firstapp.group10app.Pages.Fragments.Settings.SettingsDataControl;
import com.firstapp.group10app.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class Settings extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener, View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.settingsBody, new SettingsDataControl()).setReorderingAllowed(true).commit();
        }

        // Navigation view declaration.
        BottomNavigationView settingNav = findViewById(R.id.mainNavigation);
        settingNav.setOnItemSelectedListener(this);

        // Button declaration.
        Button dataControlButton = findViewById(R.id.goDataControl);
        dataControlButton.setOnClickListener(this);

        Button accessibilityButton = findViewById(R.id.goAccessibility);
        accessibilityButton.setOnClickListener(this);

        Button accountButton = findViewById(R.id.goAccount);
        accountButton.setOnClickListener(this);

        OnlineChecks.checkNavigationBar(settingNav);

        // Sets nothing as selected
        settingNav.setSelectedItemId(R.id.invisible);
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
            if (!DbConnection.testConnection()) {
                Toast.makeText(this, "No connection!", Toast.LENGTH_SHORT).show();
            } else {
                startActivity(new Intent(getApplicationContext(), History.class));
                return true;
            }
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.goDataControl) {
            updateView(new SettingsDataControl());

        } else if (id == R.id.goAccessibility) {
            updateView(new SettingsAccessibility());

        } else if (id == R.id.goAccount) {
            updateView(new SettingsAccount());
        }
    }

    public void updateView(Fragment newVIew) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.slide_right_in, R.anim.slide_left_out);
        transaction.replace(R.id.settingsBody, newVIew);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}