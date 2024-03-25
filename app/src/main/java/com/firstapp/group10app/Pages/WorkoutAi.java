package com.firstapp.group10app.Pages;

import static java.security.AccessController.getContext;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import java.util.Arrays;

public class WorkoutAi extends AppCompatActivity implements View.OnClickListener {
    private LinearLayout page1, page2;
    private ScrollView page3;
    private Spinner muscleGroupSpinner, durationSpinner, difficultySpinner;
    private EditText equipmentAnswer, mainGoalAnswer, injuriesAnswer, additionalInfoAnswer;
    private String muscleGroupAnswer, durationAnswer, difficultyAnswer;
    private TextView generateButton, continueButton;
    private Button beginWorkoutButton;
    private String output3;
    private View loadingAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Session.getSignedIn()) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_workout_ai);

            ImageButton backButton = findViewById(R.id.backButton);
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
            TextView mainGoalEdit = findViewById(R.id.mainGoalTitle);
            populateSpinners();

            loadingAnimation = findViewById(R.id.loadingScreen);
        } else {
            Log.e("WorkoutAI", "User is not signed in. This page should not be accessible.");
//            Session.logout(this, "This page should not be accessible. You are being logged out");
            finish();
        }
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
            Intent intent = new Intent(this, ActivityContainer.class);
            intent.putExtra("workoutHub", WorkoutHub.class);
            startActivity(intent);
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

            Runnable task = () -> {
                try {
                    output3 = (ChatGptClient.chatGPT(input)); // This is a test to see if the chatGPT function works.
                    output3 = output3.replaceAll("\\\\", "");

                    // Show loading animation
                    performAnimation(loadingAnimation, View.VISIBLE);
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

            // Adds the workout to the Database.
            try {
                addWorkout(output3);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

            // Adds shows the generated workout to the user.
            try {
                // Hide the animation.
                performAnimation(loadingAnimation, View.GONE);
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
        Session.setSelectedWorkout(converted);
        Session.setWorkoutID(JsonToDb.insertWorkoutAI(converted));
    }

    // Shows the workout to the user once generated.
    public void showWorkout(String data) throws JSONException {
        LinearLayout workoutLayout = new LinearLayout(this);

        page3.addView(workoutLayout);

        ItemVisualiser.startWorkoutGenerationAI(data, this, workoutLayout, "aiConfirm", R.layout.popup_exercise_ai, R.id.exerciseScrollView);
    }

    public void populateSpinners() {
        muscleGroupSpinner = findViewById(R.id.muscleGroupSpinner);
        durationSpinner = findViewById(R.id.durationSpinner);
        difficultySpinner = findViewById(R.id.difficultySpinner);

        // Set values for difficultly.
        ArrayAdapter<CharSequence> adapterDifficulty = ArrayAdapter.createFromResource(
                this,
                R.array.difficulty,
                android.R.layout.simple_spinner_item
        );

        adapterDifficulty.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        difficultySpinner.setAdapter(adapterDifficulty);
        difficultySpinner.setSelection(0);

        ArrayAdapter<CharSequence> adapterDuration = ArrayAdapter.createFromResource(
                this,
                R.array.duration,
                android.R.layout.simple_spinner_item
        );

        adapterDuration.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        durationSpinner.setAdapter(adapterDuration);
        durationSpinner.setSelection(0);

        // Set values for muscle target.
        ArrayAdapter<CharSequence> adapterTarget = ArrayAdapter.createFromResource(
                this,
                R.array.targetMuscleGroup,
                android.R.layout.simple_spinner_item
        );

        adapterTarget.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        muscleGroupSpinner.setAdapter(adapterTarget);
        muscleGroupSpinner.setSelection(0);
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


        return "Some info about a user: " + Arrays.toString(Session.getUserDetails()) +
                "Some more info: " +
                equipmentInfo + ". " +
                injuriesInfo + ". " +
                mainGoalInfo + ". " +

                "Generate a workout in the exact JSON format of (WorkoutName, WorkoutDuration (in minutes), TargetMuscleGroup, Equipment, Difficulty (Easy, Medium or Hard), Illustration (always set to null)" +
                "Exercises (ExerciseName, Description, Illustration (always set as null), TargetMuscleGroup, Equipment, Difficulty (easy medium hard), Sets, Reps (set to null if time-based), Time (set to null if rep-based))). Output only the JSON." +

                "Some info about the required workout: [Duration " + durationAnswer + "] [" + muscleGroupAnswer + "] [" + difficultyAnswer + "]. " + additionalInfo + ". " +

                "If you cannot generate a workout or there is not enough info, return (unsure). "
                + "Do it on one line as a String, only output JSON";
    }

    public void performAnimation(View v, int visibility) {
        if (visibility == View.GONE) {
            v.setVisibility(View.GONE);
        } else {
            v.setVisibility(View.VISIBLE);
        }

        v.animate().setDuration(200);
    }
}