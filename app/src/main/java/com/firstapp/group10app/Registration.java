package com.firstapp.group10app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

public class Registration extends AppCompatActivity implements View.OnClickListener {

    private RadioGroup radioGroup_MaleFemale;
    private RadioButton radioButton_MaleFemale;
    private LinearLayout page1, page2, page3;
    private int activePage;
    private EditText email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        // Get the pages from the xml
        page1 = findViewById(R.id.p1);
        page2 = findViewById(R.id.p2);
        page3 = findViewById(R.id.p3);

        // Set page1 to be visible
        page1.setVisibility(View.VISIBLE);
        activePage = 1;

        // Set the onClickListeners of the buttons
        Button backButton = findViewById(R.id.buttonBack);
        backButton.setOnClickListener(this);

        Button nextButton = findViewById(R.id.buttonCont);
        nextButton.setOnClickListener(this);

        // Set the onTextChangedListener of the email field
        email = findViewById(R.id.emailTextBox);

        // Set the dropdown for the reasons for joining
        setSpinner(null);

        radioGroup_MaleFemale = findViewById(R.id.sexButtons);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.buttonCont) continuePressed();
        else if (id == R.id.buttonBack) backPressed();
    }

    public void continuePressed() {
        if (activePage == 1) {
            page1.setVisibility(View.GONE);
            page2.setVisibility(View.VISIBLE);
            activePage = 2;
        } else if (activePage == 2) {
            page2.setVisibility(View.GONE);
            page3.setVisibility(View.VISIBLE);
            activePage = 3;
        } else if (activePage == 3) {

        }
    }

    public void backPressed() {
        if (activePage == 1) {
            startActivity(new Intent(Registration.this, MainActivity.class));
        } else if (activePage == 2) {
            page2.setVisibility(View.GONE);
            page1.setVisibility(View.VISIBLE);
            activePage = 1;
        } else if (activePage == 3) {
            page3.setVisibility(View.GONE);
            page2.setVisibility(View.VISIBLE);
            activePage = 2;
        }
    }

    public void checkButton(View v) {
        int radioId = radioGroup_MaleFemale.getCheckedRadioButtonId();
        radioButton_MaleFemale = findViewById(radioId);
    }

    public void setSpinner(View v) {
        //get the spinner from the xml.
        Spinner dropdown = findViewById(R.id.reasonsDropdown);

        //create a list of items for the spinner.
        String[] reasons = {"I want to lose weight", "I want to gain weight", "I want to maintain my weight"};

        //create an adapter to describe how the items are displayed, adapters are used in several places in android.
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, reasons);

        //set the spinners adapter to the previously created one.
        dropdown.setAdapter(adapter);
    }
}