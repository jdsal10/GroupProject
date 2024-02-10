package com.firstapp.group10app.Pages;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.firstapp.group10app.DB.DBHelper;
import com.firstapp.group10app.Other.ItemVisualiser;
import com.firstapp.group10app.R;

import java.util.ArrayList;

public class workout_filter extends Dialog implements View.OnClickListener {

    Spinner difficulty;
    EditText duration, target;

    public workout_filter(Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_workout_filter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_filter);

        difficulty = findViewById(R.id.difficultyInput);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                getContext(),
                R.array.difficulty,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        difficulty.setAdapter(adapter);
        duration = findViewById(R.id.durationInput);
        target = findViewById(R.id.targetMuscleInput);

        Button applyFilter = findViewById(R.id.applyFilter);
        applyFilter.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.applyFilter) {
            String durationString = duration.getText().toString();
            String difficultyString = difficulty.getSelectedItem().toString();
            String targetMuscleString = target.getText().toString();

            searchWorkout s = new searchWorkout();
            s.applyChange(durationString, difficultyString, targetMuscleString);
        }
    }
}