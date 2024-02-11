package com.firstapp.group10app.Pages;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.firstapp.group10app.R;

public class workout_filter extends AlertDialog implements View.OnClickListener {
    Spinner difficulty, duration, target;
    String durationValue, difficultyValue, targetValue;

    public workout_filter(Context context, String difficulty, String duration, String target) {
        super(context, R.style.filterCorners);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        durationValue = duration;
        difficultyValue = difficulty;
        targetValue = target;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_filter);

        // Sets values for difficulty
        difficulty = findViewById(R.id.difficultyInput);
        ArrayAdapter<CharSequence> adapterDifficulty = ArrayAdapter.createFromResource(
                getContext(), // Use the context passed to the constructor
                R.array.difficulty,
                android.R.layout.simple_spinner_item
        );

        adapterDifficulty.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        difficulty.setAdapter(adapterDifficulty);

        // Sets values for duration
        duration = findViewById(R.id.durationInput);
        ArrayAdapter<CharSequence> adapterDuration = ArrayAdapter.createFromResource(
                getContext(), // Use the context passed to the constructor
                R.array.duration,
                android.R.layout.simple_spinner_item
        );

        adapterDuration.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        duration.setAdapter(adapterDuration);

        // Sets values for target muscle group
        target = findViewById(R.id.targetMuscleInput);
        ArrayAdapter<CharSequence> adapterTarget = ArrayAdapter.createFromResource(
                getContext(), // Use the context passed to the constructor
                R.array.targetMuscleGroup,
                android.R.layout.simple_spinner_item
        );

        adapterTarget.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        target.setAdapter(adapterTarget);
        Window window = getWindow();
        if (window != null) {
            WindowManager.LayoutParams params = window.getAttributes();
            params.gravity = Gravity.BOTTOM;
            params.width = (getContext().getResources().getDisplayMetrics().widthPixels);
            params.height = (int) (getContext().getResources().getDisplayMetrics().heightPixels * 0.5);
            window.setAttributes(params);
        }
        Button applyFilter = findViewById(R.id.applyFilter);
        applyFilter.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.applyFilter) {
            String durationString = duration.getSelectedItem().toString();
            String difficultyString = difficulty.getSelectedItem().toString();
            String targetMuscleString = target.getSelectedItem().toString();

            Intent intent = new Intent(getContext(), searchWorkout.class);
            intent.putExtra("duration", durationString);
            intent.putExtra("difficulty", difficultyString);
            intent.putExtra("targetMuscle", targetMuscleString);
            getContext().startActivity(intent);
            dismiss();
        }
    }

    public void setValue(String difficultyValue, String durationValue, String targetValue) {
        if (difficultyValue != null) {
            switch (difficultyValue) {
                case "Any":
                    difficulty.setSelection(0);
                    break;
                case "Easy":
                    difficulty.setSelection(1);
                    break;
                case "Medium":
                    difficulty.setSelection(2);
                    break;
                case "Hard":
                    difficulty.setSelection(3);
                    break;
            }
        }

        if (durationValue != null) {
            switch (durationValue) {
                case "Less than 10 minutes":
                    duration.setSelection(0);
                    break;
                case "10 – 30":
                    duration.setSelection(1);
                    break;
                case "30 – 50":
                    duration.setSelection(2);
                    break;
                case "50 – 70":
                    duration.setSelection(3);
                    break;
                case "70 – 90":
                    duration.setSelection(4);
                    break;
                case "More than 90 minutes":
                    duration.setSelection(5);
                    break;
            }
        }

        if (targetValue != null) {
            switch (targetValue) {
                case "Any":
                    target.setSelection(0);
                    break;
                case "Abs":
                    target.setSelection(1);
                    break;
                // Add more when new options available!
            }
        }

    }
}