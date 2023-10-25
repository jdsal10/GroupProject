package com.firstapp.group10app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class Registration extends AppCompatActivity {

    RadioGroup radioGroup_MaleFemale;
    RadioButton radioButton_MaleFemale;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        radioGroup_MaleFemale = findViewById(R.id.radioGroup);
    }

    public void checkButton(View v) {
        int radioId = radioGroup_MaleFemale.getCheckedRadioButtonId();
        radioButton_MaleFemale = findViewById(radioId);
    }
}