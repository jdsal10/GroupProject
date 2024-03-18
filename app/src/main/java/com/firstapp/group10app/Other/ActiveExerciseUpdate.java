package com.firstapp.group10app.Other;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.firstapp.group10app.R;

public class ActiveExerciseUpdate extends Fragment {
    private String description;
    private String sets;
    private String reps;
    private String time;

    public ActiveExerciseUpdate() {}

    public static ActiveExerciseUpdate newInstance(String description, String sets, String reps, String time) {
        ActiveExerciseUpdate fragment = new ActiveExerciseUpdate();
        Bundle args = new Bundle();
        args.putString("description", description);
        args.putString("sets", sets);
        args.putString("reps", reps);
        args.putString("time", time);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            description = getArguments().getString("description");
            sets = getArguments().getString("sets");
            reps = getArguments().getString("reps");
            time = getArguments().getString("time");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.active_details, container, false);
        TextView descriptionTextView = view.findViewById(R.id.activeDescription);
        TextView setsTextView = view.findViewById(R.id.activeSets);
        TextView repsTextView = view.findViewById(R.id.activeReps);
        TextView timeTextView = view.findViewById(R.id.activeTime);
        ProgressBar progressBar = view.findViewById(R.id.activeTimer);

        descriptionTextView.setText(description);

        if(!sets.isEmpty()) {
            setsTextView.setText(sets);
        }
        else {
            setsTextView.setVisibility(View.GONE);
        }

        if(!reps.isEmpty()) {
            repsTextView.setText(reps);
        }
        else {
            repsTextView.setVisibility(View.GONE);
        }

        if(!time.isEmpty()) {
            timeTextView.setText(time);
        }
        else {
            timeTextView.setVisibility(View.GONE);
        }

        return view;
    }
}
