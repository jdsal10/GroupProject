package com.firstapp.group10app.Pages;

import static com.firstapp.group10app.Other.Validator.dobValid;
import static com.firstapp.group10app.Other.Validator.dobValidator;
import static com.firstapp.group10app.Other.Validator.emailValid;
import static com.firstapp.group10app.Other.Validator.emailValidator;
import static com.firstapp.group10app.Other.Validator.heightValid;
import static com.firstapp.group10app.Other.Validator.heightValidator;
import static com.firstapp.group10app.Other.Validator.passwordValid;
import static com.firstapp.group10app.Other.Validator.passwordValidator;
import static com.firstapp.group10app.Other.Validator.weightValid;
import static com.firstapp.group10app.Other.Validator.weightValidator;

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

import com.firstapp.group10app.DB.DBHelper;
import com.firstapp.group10app.Other.Index;
import com.firstapp.group10app.R;

import java.util.Arrays;

/**
 * The Registration class is the activity that allows the user to create an account.
 */
public class Registration extends AppCompatActivity implements View.OnClickListener {
    private LinearLayout page1, page2, page3; // The 3 pages of the registration
    private int activePage; // The page that is currently active
    private EditText email, name, password, dob, height, weight, conditions;
    private RadioGroup sex;
    private Spinner heightUnits, weightUnits, reasons;
    private Button backButton, nextButton;
    private final String[] details = new String[9];

    // Set the layout of the activity to activity_registration.xml
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        // Set the dropdowns
        setSpinner(new String[]{"cm", "inch"}, R.id.heightUnitsDropdown);
        setSpinner(new String[]{"kg", "lbs"}, R.id.weightUnitsDropdown);
        setSpinner(new String[]{"", "I want to lose weight", "I want to gain weight", "I want to maintain my weight"}, R.id.reasonsDropdown);

        // Get the pages, EditText fields, and buttons from the xml
        getAllElements();

        // Add text changed listeners to the email and password fields
        emailAddTextChangedListener();
        passwordAddTextChangedListener();
        dobAddTextChangedListener();

        // Set page1 to be visible
        page1.setVisibility(View.VISIBLE);
        activePage = 1;

        // Set the onClickListeners of the buttons
        backButton.setOnClickListener(this);
        nextButton.setOnClickListener(this);
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

    // Get all the elements from the xml (pages, EditText fields, and buttons)
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

    // Get the text from the EditText fields
    private String emailText() {
        return email.getText().toString();
    }

    private String passwordText() {
        return password.getText().toString();
    }

    private String dobText() {
        return dob.getText().toString();
    }

    private String heightText() {
        return height.getText().toString();
    }

    private String heightUnits() {
        return heightUnits.getSelectedItem().toString();
    }

    private String weightText() {
        return weight.getText().toString();
    }

    private String weightUnits() {
        return weightUnits.getSelectedItem().toString();
    }

    // Get the chosen sex radio button as a string
    private String getSelectedSex() {
        int radioId = sex.getCheckedRadioButtonId();
        RadioButton selectedSex = findViewById(radioId);

        if (selectedSex == null) return "Other";
        else return selectedSex.getText().toString();
    }

    // Add text changed listeners to the email and password fields
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

    private void dobAddTextChangedListener() {
        dob.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            int ind, ind1 = 0;

            @Override
            public void afterTextChanged(Editable s) {

                // Calculates "-" after year
                if (s.length() == 4 && ind == 0) {
                    dob.setText(String.format("%s-", s));
                    dob.setSelection(s.length() + 1);
                    ind = 1;
                } else if (s.length() < 4 && ind == 1) {
                    ind = 0;
                }

                // Calculates "-" after month
                if (s.length() == 7 && ind1 == 0) {
                    dob.setText(String.format("%s-", s));
                    dob.setSelection(s.length() + 1);
                    ind1 = 1;
                } else if (s.length() < 7 && ind1 == 1) {
                    ind1 = 0;
                }
            }
        });
    }

    // Actions for when the back and next buttons are pressed
    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.buttonNext) nextPressed();
        else if (id == R.id.buttonBack) backPressed();
        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
    }

    // If the back button is pressed - logic
    private void backPressed() {
        if (activePage == 1) goToMainActivity();
        else if (activePage == 2) goToP1();
        else if (activePage == 3) goToP2();
    }

    // If the next button is pressed - logic
    private void nextPressed() {
        if (activePage == 1) {
            if (p1Valid()) goToP2();
            else p1PointErrors();
        } else if (activePage == 2) {
            if (p2Valid()) goToP3();
            else p2PointErrors();
        } else if (activePage == 3) {
            saveUserDetails();
            System.out.println(Arrays.toString(details));
            DBHelper.insertUser(details);

            goToLogin();
        }
    }

    // Go to the main activity
    private void goToMainActivity() {
        startActivity(new Intent(Registration.this, MainActivity.class));
    }

    // Go to page 1
    private void goToP1() {
        page2.setVisibility(View.GONE);
        page1.setVisibility(View.VISIBLE);
        activePage = 1;
    }

    // Check if the email and password are valid
    private boolean p1Valid() {
        return emailValid(emailText()) && passwordValid(passwordText());
    }

    // Point out the errors in the email and password fields
    private void p1PointErrors() {
        if (!emailValid(emailText())) {
            email.setError(emailValidator(emailText()));
            email.requestFocus();
        }
        if (!passwordValid(passwordText())) {
            password.setError(passwordValidator(passwordText()));
            password.requestFocus();
        }
    }

    // Go to page 2
    private void goToP2() {
        if (activePage == 1) page1.setVisibility(View.GONE);
        else if (activePage == 3) {
            nextButton.setText(R.string.next);

            page3.setVisibility(View.GONE);
        }

        page2.setVisibility(View.VISIBLE);
        activePage = 2;
    }

    // Check if the dob, height, and weight are valid
    private boolean p2Valid() {
        return dobValid(dobText()) && heightValid(heightText(), heightUnits()) && weightValid(weightText(), weightUnits());
    }

    // Point out the errors in the dob, height, and weight fields
    private void p2PointErrors() {
        if (!dobValid(dobText())) {
            dob.setError(dobValidator(dobText()));
            dob.requestFocus();
        }
        if (!heightValid(heightText(), heightUnits())) {
            height.setError(heightValidator(heightText(), heightUnits()));
            height.requestFocus();
        }
        if (!weightValid(weightText(), weightUnits())) {
            weight.setError(weightValidator(weightText(), weightUnits()));
            weight.requestFocus();
        }
    }

    // Go to page 3
    private void goToP3() {
        nextButton.setText(R.string.finish);

        page2.setVisibility(View.GONE);
        page3.setVisibility(View.VISIBLE);
        activePage = 3;
    }

    // Save the user details to the details array
    private void saveUserDetails() {
        details[Index.EMAIL] = emailText();
        details[Index.NAME] = name.getText().toString();
        details[Index.PASSWORD] = passwordText();
        details[Index.DOB] = dobText();

        if (weightText().isEmpty()) {
            System.out.println("ADDED");
            details[Index.WEIGHT] = "";
        } else {
            details[Index.WEIGHT] = weightText() + " " + weightUnits();
        }

        if (heightText().isEmpty()) {
            details[Index.HEIGHT] = "";
        } else {
            details[Index.HEIGHT] = heightText() + " " + heightUnits();

        }
        details[Index.SEX] = getSelectedSex();
        details[Index.CONDITIONS] = conditions.getText().toString();
        details[Index.REASONS] = reasons.getSelectedItem().toString();
    }

    // Go to the login activity
    private void goToLogin() {
        startActivity(new Intent(Registration.this, Login.class));
    }
}