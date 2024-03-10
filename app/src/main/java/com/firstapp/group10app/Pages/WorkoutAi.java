package com.firstapp.group10app.Pages;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.firstapp.group10app.ChatGPT.ChatGptClient;
import com.firstapp.group10app.Other.ItemVisualiser;
import com.firstapp.group10app.Other.JsonToDb;
import com.firstapp.group10app.Other.Session;
import com.firstapp.group10app.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class WorkoutAi extends AppCompatActivity implements View.OnClickListener {
    LinearLayout page1, page2;
    ScrollView page3;
    Spinner muscleGroupSpinner, durationSpinner, difficultySpinner;
    TextView mainGoalEdit;
    EditText equipmentAnswer, mainGoalAnswer, injuriesAnswer, additionalInfoAnswer;
    String muscleGroupAnswer, durationAnswer, difficultyAnswer;
    TextView generateButton, continueButton;
    Button beginWorkoutButton;
    ImageButton backButton;
    String output3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_ai);

        backButton = findViewById(R.id.backButton);
        beginWorkoutButton = findViewById(R.id.beginWorkout);
        continueButton = findViewById(R.id.continueButton);
        generateButton = findViewById(R.id.generateWorkoutButton);
        backButton.setOnClickListener(this);
        continueButton.setOnClickListener(this);
        generateButton.setOnClickListener(this);
        beginWorkoutButton.setOnClickListener(this);

        page1 = findViewById(R.id.page1);
        page2 = findViewById(R.id.page2);
        page3 = findViewById(R.id.page3);
        page1.setVisibility(View.VISIBLE);
        page2.setVisibility(View.GONE);
        page3.setVisibility(View.GONE);
        mainGoalEdit = findViewById(R.id.mainGoalTitle);
        populateSpinners();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.backButton) {
            ActivityContainer.currentView = ActivityContainer.WORKOUTS;
            startActivity(new Intent(getApplicationContext(), ActivityContainer.class));
            overridePendingTransition(R.anim.slide_up_in, R.anim.slide_up_out);
        } else if (id == R.id.continueButton) {
            // To move the variables to the 2nd button
            muscleGroupAnswer = muscleGroupSpinner.getSelectedItem().toString();
            durationAnswer = durationSpinner.getSelectedItem().toString();
            difficultyAnswer = difficultySpinner.getSelectedItem().toString();
            equipmentAnswer = findViewById(R.id.equipmentInputLabel);

            page1.setVisibility(View.GONE);
            page2.setVisibility(View.VISIBLE);

            continueButton.setVisibility(View.GONE);
            generateButton.setVisibility(View.VISIBLE);

        } else if (id == R.id.beginWorkout) {
            startActivity(new Intent(this, WorkoutHub.class));
        } else {   // If button == GenerateButton
            page1.setVisibility(View.GONE);
            page2.setVisibility(View.GONE);

            continueButton.setVisibility(View.GONE);
            generateButton.setVisibility(View.GONE);
            page3.setVisibility(View.VISIBLE);

            // Nikola's changes
            mainGoalAnswer = findViewById(R.id.mainGoalEdit);
            injuriesAnswer = findViewById(R.id.injuriesEdit);
            additionalInfoAnswer = findViewById(R.id.additionalInfoEdit);
            String input = fillGptInput();
            Toast.makeText(WorkoutAi.this, "Generating...", Toast.LENGTH_SHORT).show();
            //USE EXAMPLE OUTPUT TO NOT WASTE TOKENS
            Runnable task = () -> {
                try {
                    output3 = (ChatGptClient.chatGPT(input)); // This is a test to see if the chatGPT function works.
                    output3 = output3.replaceAll("\\\\", "");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            };

            Thread newThread = new Thread(task);
            newThread.start();

            try {
                newThread.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            String output2 = "{" +
                    "\"WorkoutName\": \"Cardio Abs Blast\"," +
                    "\"WorkoutDuration\": 35," +
                    "\"TargetMuscleGroup\": \"Abs\"," +
                    "\"Equipment\": \"Mat\"," +
                    "\"Difficulty\": \"Easy\"," +
                    "\"Exercises\": [" +
                    "{" +
                    "\"ExerciseName\": \"Jumping Jacks\"," +
                    "\"Description\": \"Start with feet together and arms at your sides. Jump while spreading your legs and raising your arms overhead. Jump back to the starting position and repeat.\"," +
                    "\"TargetMuscleGroup\": \"Full Body\"," +
                    "\"Equipment\": \"None\"," +
                    "\"Difficulty\": \"Easy\"," +
                    "\"Sets\": 3," +
                    "\"Reps\": null," +
                    "\"Time\": 45" +
                    "}," +
                    "{" +
                    "\"ExerciseName\": \"Mountain Climbers\"," +
                    "\"Description\": \"Start in a push-up position with hands directly under shoulders. Bring one knee toward your chest, then quickly switch legs, bringing the other knee toward your chest. Continue alternating legs as quickly as possible.\"," +
                    "\"TargetMuscleGroup\": \"Abs\"," +
                    "\"Equipment\": \"Mat\"," +
                    "\"Difficulty\": \"Easy\"," +
                    "\"Sets\": 3," +
                    "\"Reps\": null," +
                    "\"Time\": 45" +
                    "}," +
                    "{" +
                    "\"ExerciseName\": \"High Knees\"," +
                    "\"Description\": \"Stand in place and quickly alternate lifting your knees toward your chest, pumping your arms as if running in place.\"," +
                    "\"TargetMuscleGroup\": \"Legs, Core\"," +
                    "\"Equipment\": \"None\"," +
                    "\"Difficulty\": \"Easy\"," +
                    "\"Sets\": 3," +
                    "\"Reps\": null," +
                    "\"Time\": 45" +
                    "}," +
                    "{" +
                    "\"ExerciseName\": \"Plank\"," +
                    "\"Description\": \"Start in a push-up position, but with your weight on your forearms instead of your hands. Keep your body in a straight line from head to heels, engaging your core muscles. Hold this position for the specified time.\"," +
                    "\"TargetMuscleGroup\": \"Core\"," +
                    "\"Equipment\": \"Mat\"," +
                    "\"Difficulty\": \"Easy\"," +
                    "\"Sets\": 3," +
                    "\"Reps\": null," +
                    "\"Time\": 30" +
                    "}," +
                    "{" +
                    "\"ExerciseName\": \"Bicycle Crunches\"," +
                    "\"Description\": \"Lie on your back with hands behind your head and legs lifted, knees bent. Bring right elbow towards left knee while simultaneously straightening right leg. Switch sides, bringing left elbow towards right knee while straightening left leg. Continue alternating sides in a pedaling motion.\"," +
                    "\"TargetMuscleGroup\": \"Abs\"," +
                    "\"Equipment\": \"Mat\"," +
                    "\"Difficulty\": \"Easy\"," +
                    "\"Sets\": 3," +
                    "\"Reps\": \"15\"," +
                    "\"Time\": null" +
                    "}," +
                    "{" +
                    "\"ExerciseName\": \"Russian Twists\"," +
                    "\"Description\": \"Sit on the floor with knees bent and feet lifted off the ground. Lean back slightly, keeping your back straight. Clasp your hands together and twist your torso to the right, then to the left, while keeping your core engaged.\"," +
                    "\"TargetMuscleGroup\": \"Obliques\"," +
                    "\"Equipment\": \"Mat\"," +
                    "\"Difficulty\": \"Easy\"," +
                    "\"Sets\": 3," +
                    "\"Reps\": \"12\"," +
                    "\"Time\": null" +
                    "}" +
                    "]" +
                    "}";

            // Adds the workout to the Database.
            try {
                addWorkout(output3);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

            // Adds shows the generated workout to the user.
            try {
                showWorkout(output3);
                beginWorkoutButton.setVisibility(View.VISIBLE);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
    }

    // Adds a workout to the database once the user confirms.
    public void addWorkout(String data) throws JSONException {
        JSONObject converted = new JSONObject(data);
        Session.selectedWorkout = converted;
        Session.workoutID = JsonToDb.insertWorkoutAI(converted);
    }

    // Shows the workout to the user once generated.
    public void showWorkout(String data) throws JSONException {
        LinearLayout workoutLayout = new LinearLayout(this);

        page3.addView(workoutLayout);

        ItemVisualiser.startWorkoutGenerationAI(data, this, workoutLayout, "aiConfirm", R.layout.activity_exercise_popup_ai, R.id.exerciseScrollView);
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
                adapterView.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        durationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                adapterView.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        difficultySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                adapterView.getItemAtPosition(position);

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
        muscleList.add("Abs");
        muscleList.add("Back");
        muscleList.add("Upper Body");
        muscleList.add("Lower Body");

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

    public String fillGptInput() {
        String additionalInfo = additionalInfoAnswer.getText().toString().trim();
        String injuriesInfo = injuriesAnswer.getText().toString().trim();
        String equipmentInfo = equipmentAnswer.getText().toString().trim();
        String mainGoalInfo = mainGoalAnswer.getText().toString().trim();
        if (!additionalInfo.isEmpty()) {
            additionalInfo = "Additional Info: " + additionalInfo;
        }
        if (!equipmentInfo.isEmpty()) {
            equipmentInfo = "User has the following equipment: " + equipmentInfo;
        }
        if (!injuriesInfo.isEmpty()) {
            injuriesInfo = "User suffers from the following injuries: " + injuriesInfo;
        }


        return "Key: " +
                "[] = optional data" +
                ". " +
                "Some info about a user: [37 years old] [67 kg] [M]" +
                " " +
                equipmentInfo + ". " +
                injuriesInfo + ". " +
                mainGoalInfo + ". " +

                "Generate a workout in exact same JSON format of (WorkoutName, WorkoutDuration (in minutes), TargetMuscleGroup, Equipment, Difficulty (Easy, Medium or Hard), " +
                "Exercises (ExerciseName, Description, TargetMuscleGroup, Equipment, Difficulty (easy medium hard), Sets, Reps, Time (if exercise isn't rep based, otherwise leave null))). output only the JSON" +
                ". " +
                "Some info about the required workout: [" + durationAnswer + "] [" + muscleGroupAnswer + "] [" + difficultyAnswer + "]. " +
                ". " +
                additionalInfo + ". " +

                ". " +
                "If you cannot generate a workout or there is not enough info, return (unsure). "
                + "Do it on one line as a String, only output JSON";
    }
}