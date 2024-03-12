package com.firstapp.group10app.Pages.Fragments.MainOptions;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.firstapp.group10app.DB.DbConnection;
import com.firstapp.group10app.Other.Session;
import com.firstapp.group10app.Pages.ActivityContainer;
import com.firstapp.group10app.Pages.Fragments.Workouts.WorkoutAi2;
import com.firstapp.group10app.Pages.Fragments.Workouts.WorkoutManual;
import com.firstapp.group10app.R;

public class WorkoutOption extends Fragment implements CompoundButton.OnCheckedChangeListener {
    public RadioButton AISelect, manualSelect;

    public WorkoutOption() {
        super(R.layout.activity_workout_option);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_workout_option, container, false);

        super.onCreate(savedInstanceState);

        updateView(new WorkoutManual(), -1, -1);

        // Initialize RadioButtons
        AISelect = rootView.findViewById(R.id.toggleAI);
        manualSelect = rootView.findViewById(R.id.toggleManual);

        // Set OnCheckedChangeListener
        AISelect.setOnCheckedChangeListener(this);
        manualSelect.setOnCheckedChangeListener(this);

        // If the user is not signed in / anonymous, they do not access to the AI or to create a workout.
        if ((!Session.dbStatus) || (!Session.signedIn)) {
            AISelect.setEnabled(false);
        }

        return rootView;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        // If ensure only one is selected at once
        if (isChecked) {
            if (buttonView.getId() == R.id.toggleAI) {
                if ((!Session.signedIn) || (!DbConnection.testConnection())) {
                    Toast.makeText(requireContext(), "No connection!", Toast.LENGTH_SHORT).show();
                    // Need to add capability to enable somehow.
                    // AISelect.setEnabled(false);
                } else {
                    Log.d("WorkoutOption.onClick()", "AI selected");
                    updateView(new WorkoutAi2(), R.anim.slide_right_in, R.anim.slide_left_out);
                }
            } else if (buttonView.getId() == R.id.toggleManual) {
                Log.d("WorkoutOption.onCheckedChanged()", "Manual selected");
                updateView(new WorkoutManual(), R.anim.slide_left_in, R.anim.slide_right_out);
            }
        }
    }

    public void updateView(Fragment newView, int enterAnim, int exitAnim) {
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();

        if (enterAnim != -1 && exitAnim != -1) {
            transaction.setCustomAnimations(enterAnim, exitAnim);
        }

        transaction.replace(R.id.workoutsBody, newView);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void tellParentToStartNewActivity(AppCompatActivity newActivity, int enterAnim, int exitAnim) {
        ActivityContainer parent = (ActivityContainer) getActivity();
        if (parent != null) {
            parent.startNewActivity(newActivity, enterAnim, exitAnim);
        } else {
            startActivity(new Intent(getContext(), newActivity.getClass()));
        }
    }
}