package com.firstapp.group10app.Pages;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;


import com.firstapp.group10app.R;


public class EnhanceInput extends Dialog implements View.OnClickListener {
    public EditText input;

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
            if (!enhanceData.isEmpty()) {
                System.out.println(enhanceData);
                // Add code to generate workout using Session.currentWorkout and new prompt.
            }
        }
    }
}
