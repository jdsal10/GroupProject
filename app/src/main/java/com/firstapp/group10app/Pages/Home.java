package com.firstapp.group10app.Pages;
import android.view.Menu;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.firstapp.group10app.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;

public class Home extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        BottomNavigationView bottomNavigationView = findViewById(R.id.mainNav);
        bottomNavigationView.setOnItemSelectedListener(this);


    }


    @Override
    public boolean onNavigationItemSelected (MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.opensettings)
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
}