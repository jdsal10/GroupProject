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
import com.firstapp.group10app.ChatGPT.ChatGptClient;
import com.firstapp.group10app.Other.Session;
import com.firstapp.group10app.R;

import org.json.JSONException;
import org.json.JSONObject;

public class EnhanceInput extends Dialog implements View.OnClickListener {
    private EditText input;
    private String result, result2;

    public EnhanceInput(Context context) {
        super(context);
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

    public Runnable runTask(String prompt, String prompt2) {
        return () -> {
            try {
                result = "";
                result = (ChatGptClient.chatGPT(prompt));

                result2 = "";
                result2 = (ChatGptClient.chatGPT(prompt2));
                result2 = result.replaceAll("\\\\", "");
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
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
                String prompt = "Given the following workout " + workoutString;
                String prompt2 = "Apply the following request to the data, only returning the JSON, and in the exact format: " + enhanceData + ".";                System.out.println(prompt);

                Toast.makeText(getContext(), "Generating...", Toast.LENGTH_SHORT).show();

                Thread newThread = new Thread(runTask(prompt, prompt2));
                newThread.start();

                try {
                    newThread.join();
                    System.out.println("Result " + result2);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

//                 Update Session with new workout
                try {
                    Session.setSelectedWorkout(new JSONObject(result2));
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
