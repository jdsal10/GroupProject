package com.firstapp.group10app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

public class Registration extends AppCompatActivity {

    RadioGroup radioGroup_MaleFemale;
    RadioButton radioButton_MaleFemale;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        setSpinner(null);

        radioGroup_MaleFemale = findViewById(R.id.sexButtons);
    }

    public void checkButton(View v) {
        int radioId = radioGroup_MaleFemale.getCheckedRadioButtonId();
        radioButton_MaleFemale = findViewById(radioId);
    }

    public void setSpinner(View v) {
        //get the spinner from the xml.
        Spinner dropdown = findViewById(R.id.reasonsDropdown);

        //create a list of items for the spinner.
        String[] items = new String[]{"1", "2", "three"};

        //create an adapter to describe how the items are displayed, adapters are used in several places in android.
        //There are multiple variations of this, but this is the basic variant.
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);

        //set the spinners adapter to the previously created one.
        dropdown.setAdapter(adapter);
    }
}