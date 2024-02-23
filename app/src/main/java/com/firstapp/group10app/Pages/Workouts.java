package com.firstapp.group10app.Pages;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.firstapp.group10app.R;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * This class is responsible for displaying the workouts page and generating workouts.
 */
public class Workouts extends AppCompatActivity implements View.OnClickListener {
    private boolean AI; // true if in AI mode, false if in manual mode
    private EditText additionalInfo; // Additional info box where the user types
    private String additionalInfoText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workouts);

        // TEMPORARY: To discuss with Nik if to do in this file or a separate workout_ai file
        if (AI) {
            TextView generateButton = findViewById(R.id.searchButton);
            generateButton.setOnClickListener(this);

        }
    }

    @Override
    public void onClick(View v) {

        if (AI) {
            if (v.getId() == R.id.generateButton) {
                // Missing variables from workouts_ai working on finishing the ai page
                //additionalInfo = findViewById(R.id.additionalInfo);
                //additionalInfoText = additionalInfo.getText().toString();
                StringBuilder output = new StringBuilder(); // Raw String given back by gpt (JSON? Discuss with Nik)

                // Unfinished input: Variables from the workouts_ai page need to be linked
                String input = "Key:\n" +
                        "[] = optional data\n" +
                        "\n" +
                        "Some info about a user: [any age] [average weight] [Male] [Knee injury] [Wants to lose weight].\n" +
                        "\n" +
                        "Generate a workout in exact same JSON format of (WorkoutName, WorkoutDuration (in hours), TargetMuscleGroup, Equipment, Difficulty (easy medium or hard), exercises (ExerciseName, Description, TargetMuscleGroup, Equipment, Difficulty (1easy medium hard))). output only the JSON\n" +
                        "\n" +
                        "Some info about the required workout: [ < 40 minutes] [abs] [easy]\n" +
                        "\n" +
                        "Additional Info:" + additionalInfoText + "\n" +
                        "\n" +
                        "If you cannot generate a workout or there is not enough info, return \"unsure\"\n";


                // Sample API call to GPT by Nik
                ExecutorService executor = Executors.newSingleThreadExecutor();
                executor.execute(() -> {
                    try {   // NOT to run yet
                        //output.append(ChatGPT_Client.chatGPT(input));   // Feeds gpt the input
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });

                executor.shutdown();
            }
        }
    }
}

