package com.firstapp.group10app;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.firstapp.group10app.Pages.Fragments.MainOptions.History;
import com.firstapp.group10app.Pages.Fragments.MainOptions.Home;
import com.firstapp.group10app.Pages.Fragments.MainOptions.WorkoutOption;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.firstapp.group10app.databinding.ActivityContainerBinding;
import com.google.android.material.navigation.NavigationBarView;

public class container extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener, View.OnClickListener {
    private ActivityContainerBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityContainerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Button goSettings = findViewById(R.id.goToSettings);
        goSettings.setOnClickListener(this);

        BottomNavigationView bottomNavigationView = findViewById(R.id.mainNavigation);
        bottomNavigationView.setOnItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.goToHome);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        Home fragment = new Home();
        fragmentTransaction.add(R.id.fragmentHolder, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.goToSettings) {
            Toast.makeText(this, "Settings is currently disabled!", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.goToHome) {
            updateView(new Home());
            return true;
        } else if (id == R.id.goToWorkouts) {
            updateView(new WorkoutOption());
            return true;
        } else if (id == R.id.goToHistory) {
            updateView(new History());
            return true;
        }
        return true;
    }

    public void updateView(Fragment view) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentHolder, view);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}