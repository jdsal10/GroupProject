package com.firstapp.group10app;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Registration extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout page1, page2, page3;
    private int activePage;
    private EditText email, name, password, dob, height, weight, conditions;
    private RadioGroup sex;
    private Spinner reasons;
    private Button backButton, nextButton;
    private String[] details = new String[9];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        // Set the dropdown for the reasons for joining
        setSpinner();

        // Get the pages, EditText fields, and buttons from the xml
        getAllElements();

        // Add text changed listeners to the email and password fields
        emailAddTextChangedListener();
        passwordAddTextChangedListener();

        // Set page1 to be visible
        page1.setVisibility(View.VISIBLE);
        activePage = 1;

        // Set the onClickListeners of the buttons
        backButton.setOnClickListener(this);
        nextButton.setOnClickListener(this);
    }

    // Set the dropdown for the reasons for joining
    public void setSpinner() {
        //get the spinner from the xml.
        Spinner dropdown = findViewById(R.id.reasonsDropdown);

        //create a list of items for the spinner.
        String[] reasons = {"I want to lose weight", "I want to gain weight", "I want to maintain my weight"};

        //create an adapter to describe how the items are displayed, adapters are used in several places in android.
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, reasons);

        //set the spinners adapter to the previously created one.
        dropdown.setAdapter(adapter);
    }

    private void getAllElements() {
        getAllPages();
        getAllFields();
        getAllButtons();
    }

    // Get page1, page2, page3 from the xml
    private void getAllPages() {
        page1 = findViewById(R.id.p1);
        page2 = findViewById(R.id.p2);
        page3 = findViewById(R.id.p3);
    }

    // Get all the EditText fields from the xml
    private void getAllFields() {
        email = findViewById(R.id.emailTextBox);
        name = findViewById(R.id.nameTextBox);
        password = findViewById(R.id.passwordTextBox);
        dob = findViewById(R.id.dobTextBox);
        sex = findViewById(R.id.sexButtons);
        height = findViewById(R.id.heightTextNumber);
        weight = findViewById(R.id.weightTextNumber);
        conditions = findViewById(R.id.allergiesTextBox);
        reasons = findViewById(R.id.reasonsDropdown);
    }

    // Get backButton, nextButton from the xml
    private void getAllButtons() {
        backButton = findViewById(R.id.buttonBack);
        nextButton = findViewById(R.id.buttonNext);
    }

    private void emailAddTextChangedListener() {
        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                email.setError(null);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    email.setError("Email is required!");
                } else if (!emailValidator(s.toString())) {
                    email.setError("Email is invalid!");
                } else email.setError(null);
            }
        });
    }

    public boolean emailValidator(String email) {
        Pattern pattern;
        Matcher matcher;

        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);

        return matcher.matches();
    }

    private void passwordAddTextChangedListener() {
        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                password.setError(null);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    password.setError("Password is required!");
                } else if (s.length() < 8) {
                    password.setError("Password must be at least 8 characters long!");
                } else if (!s.toString().matches(".*[0-9].*")) {
                    password.setError("Password must contain at least one number!");
                } else if (!s.toString().matches(".*[A-Z].*")) {
                    password.setError("Password must contain at least one capital letter!");
                } else if (!s.toString().matches(".*[a-z].*")) {
                    password.setError("Password must contain at least one lowercase letter!");
                } else if (!s.toString().matches(".*[!@#$%^&*+=?-].*")) {
                    password.setError("Password must contain at least one special character!");
                } else password.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.buttonNext) continuePressed();
        else if (id == R.id.buttonBack) backPressed();
    }

    // If the continue button is pressed - logic
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
            saveUserDetails();

            // For visualisation purposes
            for (String detail : details) {
                System.out.println(detail);
            }

            startActivity(new Intent(Registration.this, Login.class));
        }
    }

    // If the back button is pressed - logic
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

    // Get the chosen sex radio button as a string
    public String getSelectedSex() {
        int radioId = sex.getCheckedRadioButtonId();
        RadioButton selectedSex = findViewById(radioId);

        if (selectedSex == null) return "Other";
        else return selectedSex.getText().toString();
    }

    // Save the user details to the details array
    private void saveUserDetails() {
        details[0] = email.getText().toString();
        details[1] = name.getText().toString();
        details[2] = password.getText().toString();
        details[3] = dob.getText().toString();
        details[4] = getSelectedSex();
        details[5] = height.getText().toString();
        details[6] = weight.getText().toString();
        details[7] = conditions.getText().toString();
        details[8] = reasons.getSelectedItem().toString();
    }
}