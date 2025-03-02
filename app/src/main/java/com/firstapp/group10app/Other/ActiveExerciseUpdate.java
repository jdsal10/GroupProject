package com.firstapp.group10app.Other;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.firstapp.group10app.R;

public class ActiveExerciseUpdate extends Fragment {
    private String exerciseName, description, sets, reps, time, illustration;

    public ActiveExerciseUpdate() {
    }

    public static ActiveExerciseUpdate newInstance(String exerciseName, String description, String sets, String reps, String time, String image) {
        ActiveExerciseUpdate fragment = new ActiveExerciseUpdate();
        Bundle args = new Bundle();
        args.putString("exerciseName", exerciseName);
        args.putString("description", description);
        args.putString("sets", sets);
        args.putString("reps", reps);
        args.putString("time", time);
        args.putString("image", image);
        fragment.setArguments(args);
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
            illustration = getArguments().getString("image");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.active_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ImageView diagram = view.findViewById(R.id.exerciseImage);

        int resourceId;

        // This is a security measure to prevent the app from crashing if the image is not found
        try {
            resourceId = getResources().getIdentifier(illustration.replace(".png", ""), "drawable", "com.firstapp.group10app");
        } catch (Exception e) {
            resourceId = 0;
        }

        if (resourceId != 0) {
            diagram.setImageResource(resourceId);
        } else {
            diagram.setImageResource(R.drawable.icon_workout);
        }

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
            timeTextView.setText(String.format("- %s seconds", time));
        } else {
            timeTextView.setVisibility(View.GONE);
        }
    }
}
