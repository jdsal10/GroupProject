package com.firstapp.group10app.Pages;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.firstapp.group10app.R;
import com.google.android.material.navigation.NavigationBarView;

public class createOrSearch extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener, View.OnClickListener  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_or_search);

        Button goToSearch = findViewById(R.id.goToSearch);
        goToSearch.setOnClickListener(this);

        Button goToCreate = findViewById(R.id.goToCreate);
        goToCreate.setOnClickListener(this);

        NavigationBarView bottomNavigationView = findViewById(R.id.mainNavigation);
        bottomNavigationView.setOnItemSelectedListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.goToSearch) {
            startActivity(new Intent(createOrSearch.this, searchWorkout.class));
        } else if (id == R.id.goToCreate) {
            // Update with correct file when created!
//            startActivity(new Intent(createOrSearch.this, searchWorkout.class));
        }
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
}