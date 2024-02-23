package com.firstapp.group10app.Pages;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.firstapp.group10app.Other.ItemVisualiser;
import com.firstapp.group10app.Other.JSONToDB;
import com.firstapp.group10app.R;
import static com.firstapp.group10app.ChatGPT.ChatGPT_Client.chatGPT;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class WorkoutAi extends AppCompatActivity implements View.OnClickListener {
    LinearLayout page1, page2;
    Spinner muscleGroupSpinner, durationSpinner, difficultySpinner;
    TextView mainGoalEdit;
    EditText equipmentAnswer, mainGoalAnswer, injuriesAnswer, additionalInfoAnswer;
    String muscleGroupAnswer, durationAnswer, difficultyAnswer;
    TextView generateButton, continueButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_ai);

        continueButton = findViewById(R.id.continueButton);
        generateButton = findViewById(R.id.generateWorkoutButton);
        continueButton.setOnClickListener(this);
        generateButton.setOnClickListener(this);

        page1 = findViewById(R.id.page1);
        page2 = findViewById(R.id.page2);
        page1.setVisibility(View.VISIBLE);
        page2.setVisibility(View.GONE);

        mainGoalEdit = findViewById(R.id.mainGoalTitle);
        populateSpinners();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.continueButton) {
            // To move the variables to the 2nd button
            muscleGroupAnswer = muscleGroupSpinner.getSelectedItem().toString();
            durationAnswer = durationSpinner.getSelectedItem().toString();
            difficultyAnswer = difficultySpinner.getSelectedItem().toString();
            equipmentAnswer = findViewById(R.id.equipmentInputLabel);

            mainGoalEdit.setText(equipmentAnswer.getText().toString());

            page1.setVisibility(View.GONE);
            page2.setVisibility(View.VISIBLE);

            continueButton.setVisibility(View.GONE);
            generateButton.setVisibility(View.VISIBLE);
        } else {   // If button == GenerateButton
            // Misha's code
            /*
            page2.setVisibility(View.GONE);
            page1.setVisibility(View.VISIBLE);

            continueButton.setVisibility(View.VISIBLE); //Temp to navigate back
            generateButton.setVisibility(View.GONE);

            mainGoalAnswer = findViewById(R.id.mainGoalEdit);
            injuriesAnswer = findViewById(R.id.injuriesEdit);
            additionalInfoAnswer = findViewById(R.id.additionalInfoEdit);
            equipmentAnswer.setText(mainGoalAnswer.getText().toString() + injuriesAnswer.getText().toString()
                    + additionalInfoAnswer.getText().toString());
             */

            // Nikola's changes
            mainGoalAnswer = findViewById(R.id.mainGoalEdit);
            injuriesAnswer = findViewById(R.id.injuriesEdit);
            additionalInfoAnswer = findViewById(R.id.additionalInfoEdit);

            try {
                chatGPT("Hello, chatGPT, how are you?"); // This is a test to see if the chatGPT function works.
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // Adds a workout to the database once the user confirms.
    public void addWorkout(String data) throws JSONException {
        JSONObject converted = new JSONObject(data);
        JSONToDB.insertWorkoutAI(converted);

        // Add code to take user to currentWorkout when complete
    }

    // Shows the workout to the user once generated.
    public void showWorkout(String data) throws JSONException {
        LinearLayout workoutLayout = new LinearLayout(this);

        // Unsure if this is correct - confirm with Misha
        page2.addView(workoutLayout);
        
        // Note the code below has buttons in the popup active. Decide if the buttons will be on popup or default page.
        ItemVisualiser.startWorkoutGeneration(data, this, workoutLayout, "search", R.layout.activity_exercise_popup, R.id.exerciseScrollView);


    }

    public void populateSpinners() {
        // POPULATE DURATION SPINNER
        ArrayList<String> muscleGroupList = new ArrayList<>();
        ArrayList<String> durationList = new ArrayList<>();
        ArrayList<String> difficultyList = new ArrayList<>();

        muscleGroupSpinner = findViewById(R.id.muscleGroupSpinner);
        durationSpinner = findViewById(R.id.durationSpinner);
        difficultySpinner = findViewById(R.id.difficultySpinner);
        insertIntoSpinners(muscleGroupList, durationList, difficultyList);

        muscleGroupSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                String item = adapterView.getItemAtPosition(position).toString();
                Toast.makeText(WorkoutAi.this, "Selected:" + item, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        durationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                String item = adapterView.getItemAtPosition(position).toString();
                Toast.makeText(WorkoutAi.this, "Selected:" + item, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        difficultySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                String item = adapterView.getItemAtPosition(position).toString();
                Toast.makeText(WorkoutAi.this, "Selected:" + item, Toast.LENGTH_SHORT).show();
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

    public void insertIntoSpinners(ArrayList<String> muscleList, ArrayList<String> durationList, ArrayList<String> difficultyList) {
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