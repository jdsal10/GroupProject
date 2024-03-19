package com.firstapp.group10app.Other;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.firstapp.group10app.R;

public class ActiveExerciseUpdate extends Fragment {
    private String exerciseName;
    private String description;
    private String sets;
    private String reps;
    private String time;

    public ActiveExerciseUpdate() {}

    public static ActiveExerciseUpdate newInstance(String exerciseName, String description, String sets, String reps, String time) {
        ActiveExerciseUpdate fragment = new ActiveExerciseUpdate();
        Bundle args = new Bundle();
        args.putString("exerciseName", exerciseName);
        args.putString("description", description);
        args.putString("sets", sets);
        args.putString("reps", reps);
        args.putString("time", time);
        fragment.setArguments(args);
        System.out.println("CREATED");
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            exerciseName = getArguments().getString("exerciseName");
            description = getArguments().getString("description");
            sets = getArguments().getString("sets");
            reps = getArguments().getString("reps");
            time = getArguments().getString("time");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.active_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView exerciseNameTextView = view.findViewById(R.id.activeExerciceName);
        TextView descriptionTextView = view.findViewById(R.id.activeDescription);
        TextView setsTextView = view.findViewById(R.id.activeSets);
        TextView repsTextView = view.findViewById(R.id.activeReps);
        TextView timeTextView = view.findViewById(R.id.activeTime);
        ProgressBar progressBar = view.findViewById(R.id.activeTimer);

        progressBar.setVisibility(View.GONE);

        exerciseNameTextView.setText(exerciseName);
        descriptionTextView.setText(description);

        if (!sets.equals("null")) {
            setsTextView.setText(String.format("- %s sets", sets));
        } else {
            setsTextView.setVisibility(View.GONE);
        }

        if (!reps.equals("null")) {
            repsTextView.setText(String.format("- %s reps", reps));
        } else {
            repsTextView.setVisibility(View.GONE);
        }

        if (!time.equals("null")) {
            timeTextView.setText(String.format("- %s seconds?", time));
        } else {
            timeTextView.setVisibility(View.GONE);
        }
    }
}
