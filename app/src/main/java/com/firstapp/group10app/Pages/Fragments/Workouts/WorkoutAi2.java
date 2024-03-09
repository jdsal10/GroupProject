package com.firstapp.group10app.Pages.Fragments.Workouts;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.firstapp.group10app.DB.DbConnection;
import com.firstapp.group10app.Other.Session;
import com.firstapp.group10app.Pages.WorkoutAi;
import com.firstapp.group10app.R;

public class WorkoutAi2 extends Fragment implements View.OnClickListener {
    public Button goAI;

    public WorkoutAi2() {
        super(R.layout.fragment_workout_ai);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_workout_ai, container, false);

        // Initialise Buttons
        goAI = rootView.findViewById(R.id.goToAI);

        goAI.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.goToAI) {
            if ((!Session.signedIn) || (!DbConnection.testConnection())) {
                Toast.makeText(getContext(), "No connection!", Toast.LENGTH_SHORT).show();
            } else {
                startActivity(new Intent(getContext(), WorkoutAi.class));
            }
        }
    }
}