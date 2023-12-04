package com.firstapp.group10app.Pages;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.firstapp.group10app.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarMenu;
import com.google.android.material.navigation.NavigationBarView;

public class settings extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener, View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .commit();
        }


        BottomNavigationView settingNav = findViewById(R.id.settingsNav);
                settingNav.setSelectedItemId(R.id.gosettings);

        settingNav.setOnItemSelectedListener(this);

        Button dataControlButton = findViewById(R.id.goDataControl);
        dataControlButton.setOnClickListener(this);

        Button accessibilityButton = findViewById(R.id.goAccessibility);
        accessibilityButton.setOnClickListener(this);

        Button accountButton = findViewById(R.id.goAccount);
        accountButton.setOnClickListener(this);


    }

    @Override
    public boolean onNavigationItemSelected (MenuItem item) {
        System.out.println("An item has been selected!");
        int id = item.getItemId();
        if(id == R.id.gosettings)
        {
            startActivity(new Intent(getApplicationContext(),settings.class));
            return true;
        }
        else if(id == R.id.gostats) {
            return true;
            //Code for stats
        }
        else if(id == R.id.gohome) {
            startActivity(new Intent(getApplicationContext(),Home.class));
            return true;
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.goDataControl) {
            //Go data
        }
        else if(id == R.id.goAccessibility) {
            //Go access
        }
        else if(id == R.id.goAccount) {
            //Go account
        }
    }
}