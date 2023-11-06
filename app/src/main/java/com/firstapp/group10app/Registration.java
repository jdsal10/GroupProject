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
    private Spinner heightUnits, weightUnits, reasons;
    private Button backButton, nextButton, tempButton;
    private String[] details = new String[9];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        // Set the dropdowns
        setSpinner(new String[]{"cm", "inches"}, R.id.heightUnitsDropdown);
        setSpinner(new String[]{"kg", "lbs"}, R.id.weightUnitsDropdown);
        setSpinner(new String[]{"I want to lose weight", "I want to gain weight", "I want to maintain my weight"}, R.id.reasonsDropdown);

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

        /*
         * Temporary button for testing the chatGPT API
         *
         * TODO: Currently, an error is thrown when the button is pressed. Read how to fix
         *  here: https://stackoverflow.com/questions/6343166/how-can-i-fix-android-os-networkonmainthreadexception
         *
         * It seem that you may need to use AsyncTask to fix this error and will have to access the
         * AndroidManifest.xml file to allow the app to use the internet.
         *
         * After testing finishes: Remove this button and the temp() and tempPressed() methods
         */
        temp();
    }

    private void temp() {
        tempButton = findViewById(R.id.buttonTemp);
        tempButton.setOnClickListener(this);
    }

    private void tempPressed() {
        try {
            System.out.println(chatGPT_API.chatGPT("Hello, how are you?"));
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
    }

    // Set the dropdowns (for the height and weight units and the reasons for joining)
    private void setSpinner(String[] items, int id) {
        //get the spinner from the xml.
        Spinner dropdown = findViewById(id);

        //create an adapter to describe how the items are displayed, adapters are used in several places in android.
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);

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
        heightUnits = findViewById(R.id.heightUnitsDropdown);
        weight = findViewById(R.id.weightTextNumber);
        weightUnits = findViewById(R.id.weightUnitsDropdown);
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
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                email.setError(emailValidator(s.toString()));
            }
        });
    }

    private String emailValidator(String email) {
        if (email == null || email.length() == 0) return ("Email is required!");
        else {
            Pattern pattern;
            Matcher matcher;

            final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

            pattern = Pattern.compile(EMAIL_PATTERN);
            matcher = pattern.matcher(email);

            return matcher.matches() ? null : "Email is invalid!";
        }
    }

    private boolean emailValid() {
        return emailValidator(email.getText().toString()) == null;
    }

    private void passwordAddTextChangedListener() {
        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                password.setError(null);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                password.setError(passwordValidator(s.toString()));
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private String passwordValidator(String password) {
        if (password == null || password.length() == 0) return "Password is required!";
        else if (password.length() < 8) return "Password must be at least 8 characters long!";
        else if (!password.matches(".*[0-9].*"))
            return "Password must contain at least one number!";
        else if (!password.matches(".*[A-Z].*"))
            return "Password must contain at least one capital letter!";
        else if (!password.matches(".*[a-z].*"))
            return "Password must contain at least one lowercase letter!";
        else if (!password.matches(".*[!@#$%^&*+=?-].*"))
            return "Password must contain at least one special character!";
        else return null;
    }

    private boolean passwordValid() {
        return passwordValidator(password.getText().toString()) == null;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.buttonNext) nextPressed();
        else if (id == R.id.buttonBack) backPressed();
        else if (id == R.id.buttonTemp) tempPressed();
    }

    // If the next button is pressed - logic
    private void nextPressed() {
        if (activePage == 1) {
            // Check if the email and password are valid
            if (emailValid() && passwordValid()) { // If both are valid
                page1.setVisibility(View.GONE);
                page2.setVisibility(View.VISIBLE);
                activePage = 2;
            } else if (!emailValid()) { // If the email is invalid
                email.setError(emailValidator(email.getText().toString()));
                email.requestFocus();
            } else if (!passwordValid()) { // If the password is invalid
                password.setError(passwordValidator(password.getText().toString()));
                password.requestFocus();
            }
        } else if (activePage == 2) {
            nextButton.setText(R.string.finish);

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
    private void backPressed() {
        if (activePage == 1) {
            startActivity(new Intent(Registration.this, MainActivity.class));
        } else if (activePage == 2) {
            page2.setVisibility(View.GONE);
            page1.setVisibility(View.VISIBLE);
            activePage = 1;
        } else if (activePage == 3) {
            nextButton.setText(R.string.next);

            page3.setVisibility(View.GONE);
            page2.setVisibility(View.VISIBLE);
            activePage = 2;
        }
    }

    // Get the chosen sex radio button as a string
    private String getSelectedSex() {
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
        details[5] = height.getText().toString() + " " + heightUnits.getSelectedItem().toString();
        details[6] = weight.getText().toString() + " " + weightUnits.getSelectedItem().toString();
        details[7] = conditions.getText().toString();
        details[8] = reasons.getSelectedItem().toString();
    }
}