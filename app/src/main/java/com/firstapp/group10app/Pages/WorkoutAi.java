package com.firstapp.group10app.Pages;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.firstapp.group10app.ChatGPT.ChatGptClient;
import com.firstapp.group10app.Other.ItemVisualiser;
import com.firstapp.group10app.Other.JsonToDb;
import com.firstapp.group10app.Other.Session;
import com.firstapp.group10app.R;

import org.json.JSONException;
import org.json.JSONObject;

public class WorkoutAi extends AppCompatActivity implements View.OnClickListener {
    private LinearLayout page1, page2;
    private ScrollView page3;
    private Spinner muscleGroupSpinner, durationSpinner, difficultySpinner;
    private EditText equipmentAnswer, mainGoalAnswer, injuriesAnswer, additionalInfoAnswer;
    private String muscleGroupAnswer, durationAnswer, difficultyAnswer;
    private TextView generateButton, continueButton;
    private Button beginWorkoutButton;
    private String gptOutput;
    private View loadingAnimation;
    private Dialog generatingWorkout;

    private static StringBuilder getMoreInfoPart1(String[] userDetails) {
        StringBuilder moreInfo = new StringBuilder();
        if (userDetails != null) {
            if (userDetails[0] != null) {
                moreInfo.append("Some info about a user: DOB is - ").append(userDetails[0]).append(". ");
            }
            if (userDetails[1] != null) {
                moreInfo.append("User has a weight of ").append(userDetails[1]).append(" and a height of ").append(userDetails[2]).append(". ");
            }
            if (userDetails[3] != null) {
                moreInfo.append("User is a ").append(userDetails[3]).append(" and has the following health conditions: ").append(userDetails[4]).append(". ");
            }
        }
        return moreInfo;
    }

    @NonNull
    private static StringBuilder getMoreInfoPart2(String equipmentInfo, String injuriesInfo, String mainGoalInfo) {
        StringBuilder moreInfo = new StringBuilder();
        if (!equipmentInfo.isEmpty() || !injuriesInfo.isEmpty() || !mainGoalInfo.isEmpty()) {
            moreInfo.append("Some more info: ");
            if (!equipmentInfo.isEmpty())
                moreInfo.append("User has the following equipment: ").append(equipmentInfo).append(". ");
            if (!injuriesInfo.isEmpty())
                moreInfo.append("User suffers from the following injuries: ").append(injuriesInfo).append(". ");
            if (!mainGoalInfo.isEmpty())
                moreInfo.append("User's main goal is: ").append(mainGoalInfo).append(". ");
        }
        return moreInfo;
    }

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
            generatingWorkout = new Dialog(this, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
            generatingWorkout.setContentView(R.layout.generating);
            generatingWorkout.setCancelable(false);
            generatingWorkout.show();

            // Nikola's changes
            mainGoalAnswer = findViewById(R.id.mainGoalEdit);
            injuriesAnswer = findViewById(R.id.injuriesEdit);
            additionalInfoAnswer = findViewById(R.id.additionalInfoEdit);
            String input = fillGptInput();

            Thread newThread = new Thread(getTask(input));
            newThread.start();
        }
    }

    private Runnable getTask(String input) {
        return () -> {
            try {
                gptOutput = (ChatGptClient.chatGPT(input)); // This is a test to see if the chatGPT function works.
                gptOutput = gptOutput.replace("\\n", "");
                gptOutput = gptOutput.replace("\\", "");
                // Show loading animation
                performAnimation(loadingAnimation, View.VISIBLE);
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Create a new Handler to post the result back to the UI thread
            new Handler(Looper.getMainLooper()).post(() -> {
                generatingWorkout.dismiss();

                // Modify the output
                if (gptOutput.startsWith("[") && gptOutput.endsWith("]")) {
                    Log.e("ItemVisualiser.startWorkoutGenerationAI", "Removing brackets");
                    gptOutput = gptOutput.substring(1, gptOutput.length() - 1);
                }

                gptOutput = gptOutput.replaceAll("'", "");

                if (!gptOutput.startsWith("{")) {
                    gptOutput = "{" + gptOutput;
                }
                if (!gptOutput.endsWith("}")) {
                    gptOutput = gptOutput + "}";
                }

                Log.i("WorkoutAI", "Modified output: " + gptOutput);

                if (!gptOutput.contains("\"WorkoutName\"") || gptOutput.startsWith("unsure")) {
                    Toast.makeText(WorkoutAi.this, "Not enough information to generate a workout. Please try again.", Toast.LENGTH_SHORT).show();
                    page1.setVisibility(View.VISIBLE);
                    page2.setVisibility(View.GONE);
                    page3.setVisibility(View.GONE);
                    continueButton.setVisibility(View.VISIBLE);
                    generateButton.setVisibility(View.GONE);
                } else {
                    page1.setVisibility(View.GONE);
                    page2.setVisibility(View.GONE);

                    continueButton.setVisibility(View.GONE);
                    generateButton.setVisibility(View.GONE);
                    page3.setVisibility(View.VISIBLE);

                    try {
                        // Show the workout to the user.
                        showWorkout(gptOutput);
                        beginWorkoutButton.setVisibility(View.VISIBLE);
                        addWorkout(gptOutput);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        };
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
        String additionalInfo = additionalInfoAnswer.getText().toString().trim().replaceAll("[^a-zA-Z]", "");
        String injuriesInfo = injuriesAnswer.getText().toString().trim().replaceAll("[^a-zA-Z]", "");
        String equipmentInfo = equipmentAnswer.getText().toString().trim().replaceAll("[^a-zA-Z]", "");
        String mainGoalInfo = mainGoalAnswer.getText().toString().trim().replaceAll("[^a-zA-Z]", "");

        String[] userDetails = Session.getUserDetails();
        return getMoreInfoPart1(userDetails) +
                " " +
                getMoreInfoPart2(equipmentInfo, injuriesInfo, mainGoalInfo) +

                "Generate a workout in the exact JSON format of (WorkoutName, WorkoutDuration (only a number, representing minutes), TargetMuscleGroup, Equipment, Difficulty (Easy, Medium or Hard), Illustration (always set to null)," +
                " Exercises (ExerciseName, Description, Illustration (always set as null), TargetMuscleGroup, Equipment, Difficulty (easy medium hard), Sets, Reps (set to null if time-based), Time (set to null if rep-based))). "
                + "Output only the JSON and everything must have a value unless specified. "

                + "Some info about the required workout: [Duration: " + durationAnswer + "] [Target Muscle Group: " + muscleGroupAnswer + "] [Difficulty: " + difficultyAnswer + "]. Additional Info: " + additionalInfo + ". " +

                "If you cannot generate a workout as the info given is not relevant or there is not enough info, return only the word unsure. Do it on one line as a String, only output JSON";
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