package com.firstapp.group10app.Pages;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.firstapp.group10app.R;

public class workout_filter extends Dialog implements View.OnClickListener {
    Spinner difficulty, duration;
    EditText target;
    String durationValue, difficultyValue, targetValue;

    public workout_filter(Context context, String duration, String difficulty, String target) {
        super(context);
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
                getContext(),
                R.array.difficulty,
                android.R.layout.simple_spinner_item
        );

        adapterDifficulty.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        difficulty.setAdapter(adapterDifficulty);

        if(difficultyValue != null) {
            System.out.println(difficultyValue);
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
            }
        }

        // Sets values for duration
        duration = findViewById(R.id.durationInput);
        ArrayAdapter<CharSequence> adapterDuration = ArrayAdapter.createFromResource(
                getContext(),
                R.array.duration,
                android.R.layout.simple_spinner_item
        );

        adapterDuration.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        duration.setAdapter(adapterDuration);

        target = findViewById(R.id.targetMuscleInput);

        Button applyFilter = findViewById(R.id.applyFilter);
        applyFilter.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.applyFilter) {
            String durationString = duration.getSelectedItem().toString();
            String difficultyString = difficulty.getSelectedItem().toString();
            String targetMuscleString = target.getText().toString();

            Intent intent = new Intent(getContext(), searchWorkout.class);
            intent.putExtra("duration", durationString);
            intent.putExtra("difficulty", difficultyString);
            intent.putExtra("targetMuscle", targetMuscleString);
            getContext().startActivity(intent);
            hide();
        }
    }
}