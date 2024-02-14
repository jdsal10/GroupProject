package com.firstapp.group10app.Pages;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.firstapp.group10app.R;

import java.util.ArrayList;

public class workout_ai extends AppCompatActivity implements View.OnClickListener {
    LinearLayout page1, page2;
    TextView generateButton, continueButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_ai);
        continueButton = findViewById(R.id.continueButton);
        generateButton = findViewById(R.id.generateWorkoutButton);
        page1 = findViewById(R.id.page1);
        page2 = findViewById(R.id.page2);
        page1.setVisibility(View.VISIBLE);
        page2.setVisibility(View.GONE);
        continueButton.setOnClickListener(this);
        generateButton.setOnClickListener(this);
        populateSpinners();


    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.continueButton) {
            page1.setVisibility(View.GONE);
            page2.setVisibility(View.VISIBLE);

            continueButton.setVisibility(View.GONE);
            generateButton.setVisibility(View.VISIBLE);

        } else {   // If button == GenerateButton
            page1.setVisibility(View.VISIBLE);
        }
    }


    public void populateSpinners() {
        // POPULATE DURATION SPINNER
        ArrayList<String> muscleGroupList = new ArrayList<>();
        ArrayList<String> durationList = new ArrayList<>();
        ArrayList<String> difficultyList = new ArrayList<>();

        Spinner muscleGroupSpinner = findViewById(R.id.muscleGroupSpinner);
        Spinner durationSpinner = findViewById(R.id.durationSpinner);
        Spinner difficultySpinner = findViewById(R.id.difficultySpinner);
        insertIntoSpinners(muscleGroupList, durationList, difficultyList);

        muscleGroupSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                String item = adapterView.getItemAtPosition(position).toString();
                Toast.makeText(workout_ai.this, "Selected:" + item, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        durationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                String item = adapterView.getItemAtPosition(position).toString();
                Toast.makeText(workout_ai.this, "Selected:" + item, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        difficultySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                String item = adapterView.getItemAtPosition(position).toString();
                Toast.makeText(workout_ai.this, "Selected:" + item, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        ArrayAdapter<String> muscleGroupAdapter =
                new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, muscleGroupList);
        muscleGroupAdapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        muscleGroupSpinner.setAdapter(muscleGroupAdapter);

        ArrayAdapter<String> durationAdapter =
                new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, durationList);
        durationAdapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        durationSpinner.setAdapter(durationAdapter);

        ArrayAdapter<String> difficultyAdapter =
                new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, difficultyList);
        difficultyAdapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        difficultySpinner.setAdapter(difficultyAdapter);

    }

    public void insertIntoSpinners(ArrayList muscleList, ArrayList durationList, ArrayList difficultyList) {
        muscleList.add("Upper Body");
        muscleList.add("Lower Body");
        muscleList.add("Abs");

        durationList.add("20 min");
        durationList.add("40 min");
        durationList.add("1h");
        durationList.add("1h 20 min");
        durationList.add("1h 40 min");
        durationList.add("2h");

        difficultyList.add("Easy");
        difficultyList.add("Medium");
        difficultyList.add("Hard");

    }


}
