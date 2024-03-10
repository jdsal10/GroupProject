package com.firstapp.group10app.Pages.Fragments.Other;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.firstapp.group10app.DB.DbConnection;
import com.firstapp.group10app.Other.NavBarBehaviour;
import com.firstapp.group10app.Pages.Fragments.Settings.SettingsAccessibility;
import com.firstapp.group10app.Pages.Fragments.Settings.SettingsAccount;
import com.firstapp.group10app.Pages.Fragments.Settings.SettingsDataControl;
import com.firstapp.group10app.Other.OnlineChecks;
import com.firstapp.group10app.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class Settings extends Fragment implements View.OnClickListener {

    public int currentView;

    public Settings() {
        super(R.layout.activity_settings);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_settings, container, false);

        if (savedInstanceState == null) {
            currentView = R.layout.fragment_settings_data_control;
            getChildFragmentManager().beginTransaction().replace(R.id.settingsBody, new SettingsDataControl()).setReorderingAllowed(true).commit();
        }

        // Button declaration.
        Button dataControlButton = rootView.findViewById(R.id.goDataControl);
        dataControlButton.setOnClickListener(this);

        Button accessibilityButton = rootView.findViewById(R.id.goAccessibility);
        accessibilityButton.setOnClickListener(this);

        Button accountButton = rootView.findViewById(R.id.goAccount);
        accountButton.setOnClickListener(this);

        return rootView;
    }
//
//    @Override
//    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//        return NavBarBehaviour.onNavigationItemSelected(item, getApplicationContext(), this);
//    }

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
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();

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