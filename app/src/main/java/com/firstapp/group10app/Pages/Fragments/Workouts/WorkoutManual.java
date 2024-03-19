package com.firstapp.group10app.Pages.Fragments.Workouts;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.firstapp.group10app.Other.Session;
import com.firstapp.group10app.Pages.CreateWorkout;
import com.firstapp.group10app.Pages.Fragments.MainOptions.WorkoutOption;
import com.firstapp.group10app.Pages.WorkoutSearch;
import com.firstapp.group10app.R;

public class WorkoutManual extends Fragment implements View.OnClickListener {
    public WorkoutManual() {
        super(R.layout.activity_workout_manual);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.activity_workout_manual, container, false);

        // If the user is signed in, they can both create and search for workouts.
        if (Session.getSignedIn()) {
            rootView.findViewById(R.id.searchBox).setVisibility(View.VISIBLE);
            rootView.findViewById(R.id.orText).setVisibility(View.VISIBLE);
            rootView.findViewById(R.id.createBox).setVisibility(View.VISIBLE);
        }

        // If the user is not signed in, they can only search for workouts.
        else {
            rootView.findViewById(R.id.searchBox).setVisibility(View.VISIBLE);
            rootView.findViewById(R.id.orText).setVisibility(View.GONE);
            rootView.findViewById(R.id.createBox).setVisibility(View.GONE);
        }

        // Initialise Buttons
        Button goCreate = rootView.findViewById(R.id.goToCreate);
        Button goSearch = rootView.findViewById(R.id.goToSearch);

        goCreate.setOnClickListener(this);
        goSearch.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.goToSearch) {
            tellParentToStartNewActivity(new WorkoutSearch());
        } else if (id == R.id.goToCreate) {
            tellParentToStartNewActivity(new CreateWorkout());
        }
    }

    private void tellParentToStartNewActivity(AppCompatActivity newActivity) {
        WorkoutOption parentFrag = (WorkoutOption) WorkoutManual.this.getParentFragment();
        if (parentFrag != null) {
            parentFrag.tellParentToStartNewActivity(newActivity, R.anim.slide_down_in, R.anim.slide_down_out);
        } else {
            startActivity(new Intent(getContext(), newActivity.getClass()));
        }
    }
}