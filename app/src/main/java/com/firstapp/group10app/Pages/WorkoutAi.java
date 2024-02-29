package com.firstapp.group10app.Pages;

import static com.firstapp.group10app.ChatGPT.ChatGPT_Client.chatGPT;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;

import com.firstapp.group10app.ChatGPT.ChatGPT_Client;
import com.firstapp.group10app.Other.ItemVisualiser;
import com.firstapp.group10app.Other.JSONToDB;
import com.firstapp.group10app.Other.OnlineChecks;
import com.firstapp.group10app.R;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WorkoutAi extends AppCompatActivity implements View.OnClickListener, NavigationBarView.OnItemSelectedListener {
    LinearLayout page1, page2;
    ScrollView page3;
    Spinner muscleGroupSpinner, durationSpinner, difficultySpinner;
    TextView mainGoalEdit;
    EditText equipmentAnswer, mainGoalAnswer, injuriesAnswer, additionalInfoAnswer;
    String muscleGroupAnswer, durationAnswer, difficultyAnswer;
    TextView generateButton, continueButton;
    String gptInput, hasAdditionalInfo, hasEquipment;
    String output;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_ai);

        BottomNavigationView settingNav = findViewById(R.id.mainNavigation);
        settingNav.setOnItemSelectedListener(this);

        continueButton = findViewById(R.id.continueButton);
        generateButton = findViewById(R.id.generateWorkoutButton);
        continueButton.setOnClickListener(this);
        generateButton.setOnClickListener(this);

        page1 = findViewById(R.id.page1);
        page2 = findViewById(R.id.page2);
        page3 = findViewById(R.id.page3);
        page1.setVisibility(View.VISIBLE);
        page2.setVisibility(View.GONE);
        page3.setVisibility(View.GONE);
        OnlineChecks.checkNavigationBar(settingNav);
        mainGoalEdit = findViewById(R.id.mainGoalTitle);
        populateSpinners();
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.goToHome) {
            System.out.println("test----");
            startActivity(new Intent(getApplicationContext(), Home.class));
            return true;
        } else if (id == R.id.goToWorkouts) {
            startActivity(new Intent(getApplicationContext(), WorkoutOption.class));
            return true;
        } else if (id == R.id.goToHistory) {
            return true;
        }
        return true;
    }
    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.continueButton) {
            // To move the variables to the 2nd button
            muscleGroupAnswer = muscleGroupSpinner.getSelectedItem().toString();
            durationAnswer = durationSpinner.getSelectedItem().toString();
            difficultyAnswer = difficultySpinner.getSelectedItem().toString();
            equipmentAnswer = findViewById(R.id.equipmentInputLabel);



            page1.setVisibility(View.GONE);
            page2.setVisibility(View.VISIBLE);

            continueButton.setVisibility(View.GONE);
            generateButton.setVisibility(View.VISIBLE);
        } else {   // If button == GenerateButton

            //System.out.println(output);
            page1.setVisibility(View.GONE);
            page2.setVisibility(View.GONE);
            page3.setVisibility(View.VISIBLE);
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
            //String input = fillGptInput();
            //System.out.println(output);

            // USE EXAMPLE OUTPUT TO NOT WASTE TOKENS
//            Runnable task = () -> {
//                try {
//                    output.append(ChatGPT_Client.chatGPT(input)) ; // This is a test to see if the chatGPT function works.
//
//                }
//                catch(Exception e){
//                    e.printStackTrace();
//                }
//
//            };
//            Thread newThread = new Thread(task);
//            newThread.start();

            output = "{" +
                    "\"WorkoutName\": \"Cardio Abs Blast\"," +
                    "\"WorkoutDuration\": 35," +
                    "\"TargetMuscleGroup\": \"Abs\"," +
                    "\"Equipment\": \"Mat\"," +
                    "\"Difficulty\": \"Easy\"," +
                    "\"exercises\": [" +
                    "{" +
                    "\"ExerciseName\": \"Jumping Jacks\"," +
                    "\"Description\": \"Start with feet together and arms at your sides. Jump while spreading your legs and raising your arms overhead. Jump back to the starting position and repeat.\"," +
                    "\"TargetMuscleGroup\": \"Full Body\"," +
                    "\"Equipment\": \"None\"," +
                    "\"Difficulty\": \"Easy\"," +
                    "\"Sets\": 3," +
                    "\"reps\": null," +
                    "\"Time\": 45" +
                    "}," +
                    "{" +
                    "\"ExerciseName\": \"Mountain Climbers\"," +
                    "\"Description\": \"Start in a push-up position with hands directly under shoulders. Bring one knee toward your chest, then quickly switch legs, bringing the other knee toward your chest. Continue alternating legs as quickly as possible.\"," +
                    "\"TargetMuscleGroup\": \"Abs\"," +
                    "\"Equipment\": \"Mat\"," +
                    "\"Difficulty\": \"Easy\"," +
                    "\"Sets\": 3," +
                    "\"reps\": null," +
                    "\"Time\": 45" +
                    "}," +
                    "{" +
                    "\"ExerciseName\": \"High Knees\"," +
                    "\"Description\": \"Stand in place and quickly alternate lifting your knees toward your chest, pumping your arms as if running in place.\"," +
                    "\"TargetMuscleGroup\": \"Legs, Core\"," +
                    "\"Equipment\": \"None\"," +
                    "\"Difficulty\": \"Easy\"," +
                    "\"Sets\": 3," +
                    "\"reps\": null," +
                    "\"Time\": 45" +
                    "}," +
                    "{" +
                    "\"ExerciseName\": \"Plank\"," +
                    "\"Description\": \"Start in a push-up position, but with your weight on your forearms instead of your hands. Keep your body in a straight line from head to heels, engaging your core muscles. Hold this position for the specified time.\"," +
                    "\"TargetMuscleGroup\": \"Core\"," +
                    "\"Equipment\": \"Mat\"," +
                    "\"Difficulty\": \"Easy\"," +
                    "\"Sets\": 3," +
                    "\"reps\": null," +
                    "\"Time\": 30" +
                    "}," +
                    "{" +
                    "\"ExerciseName\": \"Bicycle Crunches\"," +
                    "\"Description\": \"Lie on your back with hands behind your head and legs lifted, knees bent. Bring right elbow towards left knee while simultaneously straightening right leg. Switch sides, bringing left elbow towards right knee while straightening left leg. Continue alternating sides in a pedaling motion.\"," +
                    "\"TargetMuscleGroup\": \"Abs\"," +
                    "\"Equipment\": \"Mat\"," +
                    "\"Difficulty\": \"Easy\"," +
                    "\"Sets\": 3," +
                    "\"reps\": \"15\"," +
                    "\"Time\": null" +
                    "}," +
                    "{" +
                    "\"ExerciseName\": \"Russian Twists\"," +
                    "\"Description\": \"Sit on the floor with knees bent and feet lifted off the ground. Lean back slightly, keeping your back straight. Clasp your hands together and twist your torso to the right, then to the left, while keeping your core engaged.\"," +
                    "\"TargetMuscleGroup\": \"Obliques\"," +
                    "\"Equipment\": \"Mat\"," +
                    "\"Difficulty\": \"Easy\"," +
                    "\"Sets\": 3," +
                    "\"reps\": \"12\"," +
                    "\"Time\": null" +
                    "}" +
                    "]" +
                    "}";
//            try {
//                addWorkout(output);
//            } catch (JSONException e) {
//                throw new RuntimeException(e);
//            }

            try {
                showWorkout(output);
                System.out.println("Done----------------");
            } catch (JSONException e) {
                throw new RuntimeException(e);
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
        page3.addView(workoutLayout);

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

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        durationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                String item = adapterView.getItemAtPosition(position).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        difficultySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                String item = adapterView.getItemAtPosition(position).toString();

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

    public String fillGptInput(){
        String additionalInfo = additionalInfoAnswer.getText().toString().trim();
        String injuriesInfo = injuriesAnswer.getText().toString().trim();
        String equipmentInfo = equipmentAnswer.getText().toString().trim();
        String mainGoalInfo = mainGoalAnswer.getText().toString().trim();
        if(!additionalInfo.isEmpty()){
            additionalInfo = "Additional Info: " + additionalInfo;
        }
        if(!equipmentInfo.isEmpty()){
            equipmentInfo = "User has the following equipment: " + equipmentInfo;
        }
        if(!injuriesInfo.isEmpty()){
            injuriesInfo = "User suffers from the following injuries: " + injuriesInfo;
        }
        String input = "Key: " +
                "[] = optional data" +
                ". " +
                "Some info about a user: [37 years old] [67 kg] [M] ["+injuriesInfo+"] ["+mainGoalInfo+"]." +
                " " +
                equipmentInfo + ". "+


                "Generate a workout in exact same JSON format of (WorkoutName, WorkoutDuration (in minutes), TargetMuscleGroup, Equipment, Difficulty (Easy, Medium or Hard), " +
                "Exercises (ExerciseName, Description, TargetMuscleGroup, Equipment, Difficulty (easy medium hard), Sets, Reps, Time (if exercise isn't rep based, otherwise leave null))). output only the JSON" +
                ". " +
                "Some info about the required workout: ["+durationAnswer+"] ["+muscleGroupAnswer+"] ["+difficultyAnswer+"]. " +
                ". " +
                additionalInfo + ". " +

                ". " +
                "If you cannot generate a workout or there is not enough info, return (unsure)";


        return input;

    }

}