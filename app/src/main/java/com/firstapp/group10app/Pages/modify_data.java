package com.firstapp.group10app.Pages;

import com.firstapp.group10app.Fragments.settings_data_control;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.firstapp.group10app.DB.DBHelper;
import com.firstapp.group10app.Other.Validator;
import com.firstapp.group10app.R;

public class modify_data extends Dialog implements View.OnClickListener {
    String thingToUpdate;
    String updateValue;
    EditText edit;
    Spinner dropdown;

    public modify_data(Context context, String[] data) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_modify_data);

        thingToUpdate = data[0];
        updateValue = data[1];
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_data);

        LinearLayout area = findViewById(R.id.modificationArea);

        switch (thingToUpdate) {
            case "DOB":
                edit = new EditText(getContext());
                area.addView(edit);
                edit.setText(updateValue);
                break;
            case "Sex": {
                dropdown = new Spinner(getContext());
                String[] spinnerEntries = {"Male", "Female", "Other", ""};
                ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, spinnerEntries);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                dropdown.setAdapter(adapter);
                area.addView(dropdown);
                System.out.println(updateValue);

                switch (updateValue) {
                    case "M":
                        dropdown.setSelection(0);
                        break;
                    case "F":
                        dropdown.setSelection(1);
                        break;
                    case "O":
                        dropdown.setSelection(2);
                        break;
                    default:
                        dropdown.setSelection(3);
                        break;
                }
                break;
            }

            case "Weight": {
                String[] spinnerEntries = {"kg", "lbs", ""};
                String[] weightValues = updateValue.split(" ");
                edit = new EditText(getContext());
                edit.setText(weightValues[0]);
                area.addView(edit);

                if (weightValues[1] == null) {
                    dropdown.setSelection(2);
                } else {

                    dropdown = new Spinner(getContext());
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, spinnerEntries);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    dropdown.setAdapter(adapter);
                    area.addView(dropdown);

                    switch (weightValues[1]) {
                        case "kg":
                            dropdown.setSelection(0);
                            break;
                        case "lbs":
                            dropdown.setSelection(1);
                            break;
                    }
                }
            }
        }

        Button confirm = findViewById(R.id.confirmUpdate);
        confirm.setOnClickListener(this);

        Button clear = findViewById(R.id.clearUpdate);
        clear.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.confirmUpdate) {
            switch (thingToUpdate) {
                case "DOB":
                    if (Validator.dobValid(edit.getText().toString())) {
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
                    } else if (dropdown.getSelectedItem().equals("Female")) {
                        DBHelper.updateData("Sex", "F");
                    } else if (dropdown.getSelectedItem().equals("Other")) {
                        DBHelper.updateData("Sex", "O");
                    }

                    settings_data_control.updateValue("Sex", dropdown.getSelectedItem().toString());
                    dismiss();
                    break;
                case "Weight":
                    if (Validator.weightValid(edit.getText().toString(), dropdown.getSelectedItem().toString())) {
                        DBHelper.updateData("Weight", edit.getText().toString() + " " + dropdown.getSelectedItem().toString());
                        settings_data_control.updateValue("Weight", edit.getText().toString() + " " + dropdown.getSelectedItem().toString());
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
                    DBHelper.updateData("Sex", "");
                    settings_data_control.updateValue("Weight", "");

                    break;
            }
            dismiss();
        }
    }
}