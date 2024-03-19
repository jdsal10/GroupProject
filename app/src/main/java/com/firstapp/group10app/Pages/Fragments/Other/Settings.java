package com.firstapp.group10app.Pages.Fragments.Other;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.firstapp.group10app.Pages.Fragments.Settings.SettingsAccount;
import com.firstapp.group10app.Pages.Fragments.Settings.SettingsDataControl;
import com.firstapp.group10app.R;

public class Settings extends Fragment implements View.OnClickListener {
    private int currentView;
    private RadioButton dataControlButton, accountButton;

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
            currentView = R.layout.activity_settings_data_control;
            getChildFragmentManager().beginTransaction().replace(R.id.settingsBody, new SettingsDataControl()).setReorderingAllowed(true).commit();
        }

        // Button declaration.
        dataControlButton = rootView.findViewById(R.id.goDataControl);
        dataControlButton.setOnClickListener(this);

        accountButton = rootView.findViewById(R.id.goAccount);
        accountButton.setOnClickListener(this);

        dataControlButton.setTextColor(getResources().getColor(R.color.white));

        return rootView;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.goDataControl) {
            if (currentView != R.layout.activity_settings_data_control) {
                updateView(new SettingsDataControl(), true);
                currentView = R.layout.activity_settings_data_control;

                resetButtonTextColor();
                dataControlButton.setTextColor(getResources().getColor(R.color.white));
            }
        } else if (id == R.id.goAccount) {
            if (currentView != R.layout.activity_settings_account) {
                updateView(new SettingsAccount(), false);
                currentView = R.layout.activity_settings_account;

                resetButtonTextColor();
                accountButton.setTextColor(getResources().getColor(R.color.white));
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

    private void resetButtonTextColor() {
        dataControlButton.setTextColor(getResources().getColor(R.color.accent_grayscale));
        accountButton.setTextColor(getResources().getColor(R.color.accent_grayscale));
    }
}