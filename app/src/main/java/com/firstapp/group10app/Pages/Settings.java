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
import com.firstapp.group10app.Pages.Fragments.Settings.SettingsAccessibility;
import com.firstapp.group10app.Pages.Fragments.Settings.SettingsAccount;
import com.firstapp.group10app.Pages.Fragments.Settings.SettingsDataControl;
import com.firstapp.group10app.Other.OnlineChecks;
import com.firstapp.group10app.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class Settings extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener, View.OnClickListener {

    public int currentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        if (savedInstanceState == null) {
            currentView = R.layout.fragment_settings_data_control;
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
            Intent intent = new Intent(getApplicationContext(), ActivityContainer.class);
            ActivityContainer.currentView = R.layout.activity_home;
            startActivity(intent);

            return true;
        } else if (id == R.id.goToWorkouts) {
            Intent intent = new Intent(getApplicationContext(), ActivityContainer.class);
            ActivityContainer.currentView = R.layout.activity_workout_option;
            startActivity(intent);

            return true;
        } else if (id == R.id.goToHistory) {
            if (!DbConnection.testConnection()) {
                Toast.makeText(this, "No connection!", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(getApplicationContext(), ActivityContainer.class);
                ActivityContainer.currentView = R.layout.activity_history;
                startActivity(intent);

                return true;
            }
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.goDataControl) {
            if (currentView != R.layout.fragment_settings_data_control) {
                updateView(new SettingsDataControl(), currentView == R.layout.fragment_settings_accessibility);
                currentView = R.layout.fragment_settings_data_control;
            }
        } else if (id == R.id.goAccessibility) {
            if (currentView != R.layout.fragment_settings_accessibility) {
                updateView(new SettingsAccessibility(), currentView == R.layout.fragment_settings_account);
                currentView = R.layout.fragment_settings_accessibility;
            }
        } else if (id == R.id.goAccount) {
            if (currentView != R.layout.fragment_settings_account) {
                updateView(new SettingsAccount(), currentView == R.layout.fragment_settings_data_control);
                currentView = R.layout.fragment_settings_account;
            }
        }
    }

    public void updateView(Fragment newVIew, boolean forwardBoolean) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        if (forwardBoolean) {
            transaction.setCustomAnimations(R.anim.slide_left_in, R.anim.slide_right_out);
        } else {
            transaction.setCustomAnimations(R.anim.slide_right_in, R.anim.slide_left_out);
        }

        transaction.replace(R.id.settingsBody, newVIew);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}