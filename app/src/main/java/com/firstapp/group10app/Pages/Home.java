package com.firstapp.group10app.Pages;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import androidx.appcompat.app.AppCompatActivity;
import com.firstapp.group10app.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class Home extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Declare bottom taskbar
        BottomNavigationView bottomNavigationView = findViewById(R.id.mainNavigation);
        bottomNavigationView.setSelectedItemId(R.id.goHome);
        bottomNavigationView.setOnItemSelectedListener(this);
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
}