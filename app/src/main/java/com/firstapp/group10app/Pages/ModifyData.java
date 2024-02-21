package com.firstapp.group10app.Pages;

import static android.text.InputType.TYPE_CLASS_NUMBER;

import com.firstapp.group10app.Fragments.settings_data_control;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.firstapp.group10app.DB.DBHelper;
import com.firstapp.group10app.Other.Validator;
import com.firstapp.group10app.R;

public class ModifyData extends Dialog implements View.OnClickListener {
    String thingToUpdate, updateValue;
    EditText edit;
    Spinner dropdown;
    TextView description;

    public ModifyData(Context context, String[] data) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_modify_data);

        thingToUpdate = data[0];
        updateValue = data[1];
    }

    private void dobAddTextChangedListener(EditText edit) {
        edit.addTextChangedListener(new TextWatcher() {
            int ind, ind1 = 0;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Calculates "-" after year
                if (s.length() == 4 && ind == 0) {
                    edit.setText(String.format("%s-", s));
                    edit.setSelection(s.length() + 1);
                    ind = 1;
                } else if (s.length() < 4 && ind == 1) {
                    ind = 0;
                }

                // Calculates "-" after month
                if (s.length() == 7 && ind1 == 0) {
                    edit.setText(String.format("%s-", s));
                    edit.setSelection(s.length() + 1);
                    ind1 = 1;
                } else if (s.length() < 7 && ind1 == 1) {
                    ind1 = 0;
                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_data);

        LinearLayout area = findViewById(R.id.modificationArea);
        description = findViewById(R.id.updateDescription);

        switch (thingToUpdate) {
            case "DOB":
                description.append("date of birth.");
                edit = new EditText(getContext());
                edit.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                edit.setInputType(InputType.TYPE_CLASS_NUMBER);
                area.addView(edit);
                dobAddTextChangedListener(edit);
                edit.setText(updateValue);
                break;


            case "Sex": {
                description.append("sex.");
                dropdown = new Spinner(getContext());
                String[] spinnerEntries = {"Male", "Female", "Other", ""};
                ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, spinnerEntries);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                dropdown.setAdapter(adapter);
                area.addView(dropdown);

                switch (updateValue) {
                    case "Male":
                        dropdown.setSelection(0);
                        break;
                    case "Female":
                        dropdown.setSelection(1);
                        break;
                    case "Other":
                        dropdown.setSelection(2);
                        break;
                    default:
                        dropdown.setSelection(3);
                        break;
                }
                break;
            }

            case "Weight": {
                description.append("weight.");
                String[] spinnerEntries = {"kg", "lbs", ""};
                String[] weightValues = updateValue.split(" ");
                edit = new EditText(getContext());
                edit.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                edit.setInputType(TYPE_CLASS_NUMBER);
                if (weightValues.length > 0) {
                    edit.setText(weightValues[0]);
                }
                area.addView(edit);
                dropdown = new Spinner(getContext());
                area.addView(dropdown);
                ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, spinnerEntries);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                dropdown.setAdapter(adapter);


                if (weightValues.length < 2) {
                    dropdown.setSelection(2);
                } else {
                    switch (weightValues[1]) {
                        case "kg":
                            dropdown.setSelection(0);
                            break;
                        case "lbs":
                            dropdown.setSelection(1);
                            break;
                    }
                }
                break;
            }

            case "Height": {
                description.append("height.");
                String[] spinnerEntries = {"cm", "inch", ""};
                String[] heightValues = updateValue.split(" ");

                // Create a horizontal LinearLayout to hold the EditText and Spinner
                LinearLayout horizontalLayout = new LinearLayout(getContext());
                horizontalLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                horizontalLayout.setOrientation(LinearLayout.HORIZONTAL);

                // EditText for height value
                edit = new EditText(getContext());
                edit.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
                edit.setInputType(InputType.TYPE_CLASS_NUMBER);
                if (heightValues.length > 0) {
                    edit.setText(heightValues[0]);
                }
                horizontalLayout.addView(edit);

                // Spinner for unit selection
                dropdown = new Spinner(getContext());
                LinearLayout.LayoutParams dropdownParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                dropdownParams.gravity = Gravity.CENTER_VERTICAL;
                dropdown.setLayoutParams(dropdownParams);
                ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, spinnerEntries);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                dropdown.setAdapter(adapter);

                if (heightValues.length < 2) {
                    dropdown.setSelection(2);
                } else {
                    switch (heightValues[1]) {
                        case "cm":
                            dropdown.setSelection(0);
                            break;
                        case "inch":
                            dropdown.setSelection(1);
                            break;
                    }
                }

                horizontalLayout.addView(dropdown);

                // Add the horizontal layout to the parent area
                area.addView(horizontalLayout);
                break;
            }


            case "Allergies": {
                description.append("allergies.");
                edit = new EditText(getContext());
                edit.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                area.addView(edit);
                edit.setText(updateValue);
                break;
            }

            case "Reasons": {
                description.append("reasons.");
                String[] spinnerEntries = {"I want to lose weight", "I want to gain weight", "I want to maintain my weight", ""};
                String reasonsValues = updateValue;
                dropdown = new Spinner(getContext());
                area.addView(dropdown);
                ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, spinnerEntries);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                dropdown.setAdapter(adapter);

                if (updateValue.equals("")) {
                    dropdown.setSelection(3);
                } else {
                    switch (reasonsValues) {
                        case "I want to lose weight":
                            dropdown.setSelection(0);
                            break;
                        case "I want to gain weight":
                            dropdown.setSelection(1);
                            break;
                        case "I want to maintain my weight":
                            dropdown.setSelection(2);
                            break;
                    }
                }
            }
        }

        Button confirm = findViewById(R.id.confirmUpdate);
        confirm.setOnClickListener(this);

        Button clear = findViewById(R.id.clearUpdate);
        clear.setOnClickListener(this);

        Button cancel = findViewById(R.id.cancelUpdate);
        cancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.confirmUpdate) {
            switch (thingToUpdate) {
                case "DOB":
                    if (edit.getText().toString().equals("")) {
                        DBHelper.updateData("DOB", "");
                        settings_data_control.updateValue("DOB", "");
                        dismiss();
                    } else if (Validator.dobValid(edit.getText().toString())) {
                        DBHelper.updateData("DOB", edit.getText().toString());
                        settings_data_control.updateValue("DOB", edit.getText().toString());
                        dismiss();
                    } else {
                        edit.setError("Invalid format!");
                    }
                    break;

                case "Sex":
                    if (dropdown.getSelectedItem().equals("Male")) {
                        DBHelper.updateData("Sex", "M");
                        settings_data_control.updateValue("Sex", dropdown.getSelectedItem().toString());
                        dismiss();
                    } else if (dropdown.getSelectedItem().equals("Female")) {
                        DBHelper.updateData("Sex", "F");
                        settings_data_control.updateValue("Sex", dropdown.getSelectedItem().toString());
                        dismiss();
                    } else if (dropdown.getSelectedItem().equals("Other")) {
                        DBHelper.updateData("Sex", "O");
                        settings_data_control.updateValue("Sex", dropdown.getSelectedItem().toString());
                        dismiss();
                    } else {
                        edit.setError("Invalid format!");
                    }
                    break;

                case "Weight":
                    if (edit.getText().toString().equals("") && !dropdown.getSelectedItem().toString().equals("")) {
                        edit.setError("Invalid format!");
                    } else if (edit.getText().toString().equals("") && dropdown.getSelectedItem().toString().equals("")) {
                        DBHelper.updateData("Weight", "");
                        settings_data_control.updateValue("Weight", "");
                        dismiss();
                    } else if (Validator.weightValid(edit.getText().toString(), dropdown.getSelectedItem().toString())) {
                        DBHelper.updateData("Weight", edit.getText().toString() + " " + dropdown.getSelectedItem().toString());
                        settings_data_control.updateValue("Weight", edit.getText().toString() + " " + dropdown.getSelectedItem().toString());
                        dismiss();
                    } else {
                        edit.setError("Invalid format!");
                    }
                    break;

                case "Height":
                    if (edit.getText().toString().equals("") && !dropdown.getSelectedItem().toString().equals("")) {
                        edit.setError("Invalid format!");
                    } else if (edit.getText().toString().equals("") && dropdown.getSelectedItem().toString().equals("")) {
                        DBHelper.updateData("Height", "");
                        settings_data_control.updateValue("Height", "");
                        dismiss();
                    } else if (Validator.heightValid(edit.getText().toString(), dropdown.getSelectedItem().toString())) {
                        DBHelper.updateData("Height", edit.getText().toString() + " " + dropdown.getSelectedItem().toString());
                        settings_data_control.updateValue("Height", edit.getText().toString() + " " + dropdown.getSelectedItem().toString());
                        dismiss();
                    } else {
                        edit.setError("Invalid format!");
                    }
                    break;

                case "Allergies":
                    if (edit.getText().toString().equals("")) {
                        DBHelper.updateData("HealthCondition", "");
                        settings_data_control.updateValue("Allergies", "");
                        dismiss();
                    } else {
                        DBHelper.updateData("HealthCondition", edit.getText().toString());
                        settings_data_control.updateValue("Allergies", edit.getText().toString());
                        dismiss();
                    }
                    break;

                case "Reasons":
                    if (dropdown.getSelectedItem().toString().equals("")) {
                        DBHelper.updateData("ReasonForDownloading", "");
                        settings_data_control.updateValue("Reasons", "");
                        dismiss();
                    } else {
                        DBHelper.updateData("ReasonForDownloading", dropdown.getSelectedItem().toString());
                        settings_data_control.updateValue("Reasons", dropdown.getSelectedItem().toString());
                        dismiss();
                    }
                    break;
            }
        } else if (id == R.id.clearUpdate) {
            switch (thingToUpdate) {
                case "DOB":
                    DBHelper.updateData("DOB", "");
                    settings_data_control.updateValue("DOB", "");
                    break;
                case "Sex":
                    DBHelper.updateData("Sex", "");
                    settings_data_control.updateValue("Sex", "");
                    break;
                case "Weight":
                    DBHelper.updateData("Weight", "");
                    settings_data_control.updateValue("Weight", "");
                    break;
                case "Height":
                    DBHelper.updateData("Height", "");
                    settings_data_control.updateValue("Height", "");
                    break;
                case "Allergies":
                    DBHelper.updateData("HealthCondition", "");
                    settings_data_control.updateValue("Allergies", "");
                    break;
                case "Reasons":
                    DBHelper.updateData("ReasonForDownloading", "");
                    settings_data_control.updateValue("Reasons", "");
                    break;
            }
            dismiss();
        } else if (id == R.id.cancelUpdate) {
            dismiss();
        }
    }
}