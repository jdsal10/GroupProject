package com.firstapp.group10app.Pages.Fragments.Workouts;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.firstapp.group10app.DB.OnlineDb.DbConnection;
import com.firstapp.group10app.Other.Session;
import com.firstapp.group10app.Pages.Fragments.MainOptions.WorkoutOption;
import com.firstapp.group10app.Pages.WorkoutAi;
import com.firstapp.group10app.R;

public class WorkoutAi2 extends Fragment implements View.OnClickListener {
    public WorkoutAi2() {
        super(R.layout.activity_workout_ai2);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.activity_workout_ai2, container, false);

        // Initialise Buttons
        Button goAI = rootView.findViewById(R.id.goToAI);

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
            if ((!Session.getSignedIn()) || (!DbConnection.testConnection())) {
                Toast.makeText(getContext(), "No connection!", Toast.LENGTH_SHORT).show();
            } else {
                tellParentToStartNewActivity(new WorkoutAi());
            }
        }
    }

    public void tellParentToStartNewActivity(AppCompatActivity newActivity) {
        WorkoutOption parentFrag = ((WorkoutOption) WorkoutAi2.this.getParentFragment());
        if (parentFrag != null) {
            parentFrag.tellParentToStartNewActivity(newActivity, R.anim.slide_down_in, R.anim.slide_down_out);
        } else {
            startActivity(new Intent(getContext(), newActivity.getClass()));
        }
    }
}