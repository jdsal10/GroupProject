package com.firstapp.group10app.Pages;
import android.view.Menu;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.firstapp.group10app.R;
import com.google.android.material.navigation.NavigationView;

public class Home extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        NavigationView settingsN = (NavigationView) findViewById(R.id.gosettings);

        if (settingsN != null) {
            settingsN.setNavigationItemSelectedListener(this);

        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.gosettings) {
                    Log.d("OnClick", "Button clicked with ID: " + id);
            Intent go = new Intent(Home.this, settings.class);
            startActivity(go);
            return true;

        }
        return false;
    }
    @Override
// Update with new variables when added
    public void onClick(View v) {
        int id = v.getId();
        Log.d("OnClick", "Button clicked with ID: " + id);

        if (id == R.id.gosettings) {
            Intent go = new Intent(Home.this, settings.class);
            startActivity(go);
        }
    }
}