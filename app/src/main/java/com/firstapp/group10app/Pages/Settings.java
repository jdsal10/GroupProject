package com.firstapp.group10app.Pages;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.firstapp.group10app.Fragments.settings_accessibility;
import com.firstapp.group10app.Fragments.settings_account;
import com.firstapp.group10app.Fragments.settings_data_control;
import com.firstapp.group10app.Other.onlineChecks;
import com.firstapp.group10app.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class Settings extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener, View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.settingsBody, new settings_data_control()).setReorderingAllowed(true).commit();
        }

        // Navigation view declaration.
        BottomNavigationView settingNav = findViewById(R.id.mainNavigation);
//        settingNav.setSelectedItemId();
        settingNav.setOnItemSelectedListener(this);

        // Button declaration.
        Button dataControlButton = findViewById(R.id.goDataControl);
        dataControlButton.setOnClickListener(this);

        Button accessibilityButton = findViewById(R.id.goAccessibility);
        accessibilityButton.setOnClickListener(this);

        Button accountButton = findViewById(R.id.goAccount);
        accountButton.setOnClickListener(this);

        onlineChecks.checkNavigationBar(settingNav);

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

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.goDataControl) {
            updateView(new settings_data_control());
        } else if (id == R.id.goAccessibility) {
            updateView(new settings_accessibility());
        } else if (id == R.id.goAccount) {
            updateView(new settings_account());
        }
    }

    public void updateView(Fragment newVIew) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.settingsBody, newVIew);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}