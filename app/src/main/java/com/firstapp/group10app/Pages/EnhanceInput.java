package com.firstapp.group10app.Pages;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

//import com.firstapp.group10app.ChatGPT.ChatGptClient;
import com.firstapp.group10app.Other.Session;
import com.firstapp.group10app.R;

import org.json.JSONException;
import org.json.JSONObject;

public class EnhanceInput extends Dialog implements View.OnClickListener {
    private EditText input;

    public EnhanceInput(Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.enhance_input);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.enhance_input);

        Button generateEnhance = findViewById(R.id.generateEnhance);
        generateEnhance.setOnClickListener(this);

        input = findViewById(R.id.enhanceTextInput);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.generateEnhance) {
            String enhanceData = input.getText().toString();
            // Check theres data first.
            if (!enhanceData.isEmpty()) {
                // Add code to generate workout using Session.currentWorkout and new prompt.
                String workoutString = Session.getSelectedWorkout().toString();
                String prompt = "Given the following workout " + workoutString + ", apply the following request to the data, only returning the JSON, and in the exact format: " + enhanceData + ".";                System.out.println(prompt);

                Toast.makeText(getContext(), "Generating...", Toast.LENGTH_SHORT).show();

//                Runnable task = () -> {
//                    try {
//                        result = (ChatGptClient.chatGPT(prompt));
//                        result = result.replaceAll("\\\\", "");
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                };
//
//                Thread newThread = new Thread(task);
//                newThread.start();
//
//                try {
//                    newThread.join();
//                    System.out.println("Result " + result);
//                } catch (InterruptedException e) {
//                    throw new RuntimeException(e);
//                }

                // For testing, use below:
                String result = "{" +
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

//                 Update Session with new workout
                try {
                    Session.setSelectedWorkout(new JSONObject(result));
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

                this.hide();
                // Show view with new workout - continues in that file
                EnhanceResults enhance2 = new EnhanceResults(getContext());
                enhance2.show();
            }
        }
    }
}
