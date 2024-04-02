package com.firstapp.group10app.Pages;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

//import com.firstapp.group10app.ChatGPT.ChatGptClient;
import com.firstapp.group10app.ChatGPT.ChatGptClient;
import com.firstapp.group10app.Other.Session;
import com.firstapp.group10app.R;

import org.json.JSONException;
import org.json.JSONObject;

public class EnhanceInput extends Dialog implements View.OnClickListener {
    private EditText input;
    private String result;
    private Dialog enhancingWorkout;

    public EnhanceInput(Context context) {
        super(context);
        setContentView(R.layout.popup_enhance_input);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_enhance_input);

        Button generateEnhance = findViewById(R.id.generateEnhance);
        generateEnhance.setOnClickListener(this);

        input = findViewById(R.id.enhanceTextInput);
    }

    public Runnable runTask(String prompt) {
        return () -> {
            try {
                result = "";
                result = (ChatGptClient.chatGPT(prompt));

                System.out.println("RESULT: " + result);

                enhancingWorkout.dismiss();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.generateEnhance) {
            enhancingWorkout = new Dialog(getContext(), android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
            enhancingWorkout.setContentView(R.layout.generating);
            enhancingWorkout.setCancelable(false);
            enhancingWorkout.show();

            String enhanceData = input.getText().toString();
            // Check theres data first.
            if (!enhanceData.isEmpty()) {
                // Add code to generate workout using Session.currentWorkout and new prompt.
                String workoutString = Session.getSelectedWorkout().toString();
                workoutString = workoutString.replace("\"", "\\\"");
                String prompt = "Given the following workout: " + workoutString + ", apply the following update to the data, only returning the JSON, and in the exact format: " + enhanceData + ". Include values for all fields, but set time to null if it is rep, and vice versa. ExerciseID and WorkoutID are not needed. "
                        + "The result should be in the following JSON format on one line: (WorkoutName, WorkoutDuration (only a number, representing minutes), TargetMuscleGroup, Equipment, Difficulty (Easy, Medium or Hard), Illustration (always set to null)"
                        + "Exercises (ExerciseName, Description, Illustration (always set as null), TargetMuscleGroup, Equipment, Difficulty (easy medium hard), Sets, Reps (set to null if time-based), Time (set to null if rep-based)))."
                        + "If you are unsure or if it breaks the format of the required output, please type unsure.";
                System.out.println(prompt);

                Thread newThread = new Thread(runTask(prompt));

                this.dismiss();
                newThread.start();

                try {
                    newThread.join();
                    result = result.replaceAll("\\\\n", "").replaceAll("\\\\", "");
                    System.out.println("Result " + result);
                    if (result.startsWith("unsure") || !result.contains("\"WorkoutName\"")) {
                        Toast.makeText(getContext(), "I'm not sure what you mean. Please try again.", Toast.LENGTH_SHORT).show();
                    } else {
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
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);

                }
            }
        }
    }
}
