package com.firstapp.group10app.Pages.Fragments.Workouts;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.firstapp.group10app.Pages.CreateWorkout;
import com.firstapp.group10app.Pages.WorkoutSearch;
import com.firstapp.group10app.R;

public class WorkoutManual extends Fragment implements View.OnClickListener {

    public Button goCreate, goSearch;

    public WorkoutManual() {
        super(R.layout.activity_workout_manual);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.activity_workout_manual, container, false);

        // Initialise Buttons
        goCreate = rootView.findViewById(R.id.goToCreate);
        goSearch = rootView.findViewById(R.id.goToSearch);

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
            startActivity(new Intent(getContext(), WorkoutSearch.class));
        } else if (id == R.id.goToCreate) {
            startActivity(new Intent(getContext(), CreateWorkout.class));
        }
    }
}