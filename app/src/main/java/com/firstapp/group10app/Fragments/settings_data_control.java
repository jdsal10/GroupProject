package com.firstapp.group10app.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.firstapp.group10app.DB.DBHelper;
import com.firstapp.group10app.Other.*;
import com.firstapp.group10app.R;

import java.sql.ResultSet;
import java.util.ArrayList;

public class settings_data_control extends Fragment implements View.OnClickListener {

    // UNITS FOR WEIGHT CURRENTLY FAILS!

    // Declared variables
    EditText dobValue, weightValue, heightValue, allergiesValue;
    Button dobUpdate, weightUpdate, heightUpdate, sexUpdate, allergiesUpdate, reasonsUpdate;
    Button dobClear, weightClear, heightClear, sexClear, allergiesClear, reasonsClear;
    Spinner weightSpin, heightSpin, sexSpin, reasonSpin;

    public settings_data_control() {
        super(R.layout.fragment_settings_data_control);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void updateValues(ArrayList<String> info) {
        // Debug output
        System.out.println(info.toString());

        // Since DOB is a regular value, no formatting is required
        dobValue.setText(info.get(0));

        // Updates the view value of weight and its units.
        String tempWeight = info.get(1);
        if (tempWeight == null || TextUtils.isEmpty(tempWeight) || tempWeight.equals("")) {
            weightSpin.setSelection(0);
            weightValue.setText("");
        }
        else {
            String weightUnits = info.get(1).split(" ")[1];
            weightValue.setText(info.get(1).split(" ")[0]);

            if (weightUnits == null) {
                weightSpin.setSelection(0);
            } else if (weightUnits.equals("kg")) {
                weightSpin.setSelection(1);
            } else if (weightUnits.equals("lbs")) {
                weightSpin.setSelection(2);
            }
        }

        // Updates the view value of height and its units.
        String tempHeight = info.get(2);
        if (tempHeight == null || TextUtils.isEmpty(tempHeight) || tempHeight.equals("")) {
            heightSpin.setSelection(0);
            heightValue.setText("");
        }
        else {
            String heightUnits = info.get(2).split(" ")[1];
            heightValue.setText(info.get(2).split(" ")[0]);

            if (heightUnits == null) {
                heightSpin.setSelection(0);
            } else if (heightUnits.equals("cm")) {
                heightSpin.setSelection(1);
            } else if (heightUnits.equals("inch")) {
                heightSpin.setSelection(2);
            }
        }

        // Updates the units for sex.
        String selectedSex = info.get(3).trim();

        switch (selectedSex) {
            case "":
                sexSpin.setSelection(0);
                break;
            case "M":
                sexSpin.setSelection(1);
                break;
            case "F":
                sexSpin.setSelection(2);
                break;
            case "O":
                sexSpin.setSelection(3);
                break;
        }

        allergiesValue.setText(info.get(4));

        // Updates "reason for downloading"
        switch (info.get(5)) {
            case "":
                reasonSpin.setSelection(0);
                break;
            case "I want to lose weight":
                reasonSpin.setSelection(1);
                break;
            case "I want to gain weight":
                reasonSpin.setSelection(2);
                break;
            case "I want to maintain my weight":
                reasonSpin.setSelection(3);
                break;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_settings_data_control, container, false);

        // Initialises the widgets

        // Initialise the update buttons.
        dobUpdate = rootView.findViewById(R.id.updateDOB);
        weightUpdate = rootView.findViewById(R.id.updateWeight);
        heightUpdate = rootView.findViewById(R.id.updateHeight);
        sexUpdate = rootView.findViewById(R.id.updateSex);
        allergiesUpdate = rootView.findViewById(R.id.updateAllergies);
        reasonsUpdate = rootView.findViewById(R.id.updateReasons);

        // Initialise all spinners.
        weightSpin = rootView.findViewById(R.id.weightUnitSpinner);
        heightSpin = rootView.findViewById(R.id.heightUnitSpinner);
        sexSpin = rootView.findViewById(R.id.sexValue);
        reasonSpin = rootView.findViewById(R.id.reasonSpin);

        // Initialise the clear buttons
        dobClear = rootView.findViewById(R.id.clearDOB);
        weightClear = rootView.findViewById(R.id.clearWeight);
        heightClear = rootView.findViewById(R.id.clearHeight);
        sexClear = rootView.findViewById(R.id.clearSex);
        allergiesClear = rootView.findViewById(R.id.clearAllergies);
        reasonsClear = rootView.findViewById(R.id.clearReasons);

        // Initialize the value fields.
        dobValue = rootView.findViewById(R.id.DOBValue);
        weightValue = rootView.findViewById(R.id.weightValue);
        heightValue = rootView.findViewById(R.id.heightValue);
        reasonSpin = rootView.findViewById(R.id.reasonSpin);
        allergiesValue = rootView.findViewById(R.id.allergiesValue);

        // Sets onClickListener for the buttons.
        dobClear.setOnClickListener(this);
        dobUpdate.setOnClickListener(this);

        weightClear.setOnClickListener(this);
        weightUpdate.setOnClickListener(this);

        heightClear.setOnClickListener(this);
        heightUpdate.setOnClickListener(this);

        sexClear.setOnClickListener(this);
        sexUpdate.setOnClickListener(this);

        allergiesClear.setOnClickListener(this);
        allergiesUpdate.setOnClickListener(this);

        reasonsClear.setOnClickListener(this);
        reasonsUpdate.setOnClickListener(this);

        // Declares an array of the users details.
        String currentUser = Session.userEmail;
        ArrayList<String> details = new ArrayList<>();

        // Gets the details of the current user.
        DBHelper help = new DBHelper();
        ResultSet data = help.getUser(currentUser);

        try {
            if (data.next()) {
                details.add(data.getString("DOB"));
                details.add(data.getString("Weight"));
                details.add(data.getString("Height"));
                details.add(data.getString("Sex"));
                details.add(data.getString("HealthCondition"));
                details.add(data.getString("ReasonForDownloading"));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        updateValues(details);
        return rootView;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        // Modify DOB
        if (id == R.id.clearDOB) {
            DBHelper.clearData("DOB");
            dobValue.setText("");
        } else if (id == R.id.updateDOB) {
            if (Validator.dobValid(dobValue.getText().toString())) {
                DBHelper.updateData("DOB", dobValue.getText().toString());
            } else {
                dobValue.setError("Invalid format!");
            }
        }

        // Modify Weight
        else if (id == R.id.clearWeight) {
            DBHelper.clearData("Weight");
            weightValue.setText("");
        } else if (id == R.id.updateWeight) {
            if (Validator.weightValid(weightValue.getText().toString(), weightSpin.getSelectedItem().toString())) {
                DBHelper.updateData("Weight", weightValue.getText().toString() + " " + weightSpin.getSelectedItem().toString());
            } else {
                weightValue.setError("Invalid format!");
            }

        }

        // Modify Height
        else if (id == R.id.clearHeight) {
            DBHelper.clearData("Height");
            heightValue.setText("");
        } else if (id == R.id.updateHeight) {
            if (Validator.heightValid(heightValue.getText().toString(), heightSpin.getSelectedItem().toString())) {
                DBHelper.updateData("Height", heightValue.getText().toString() + " " + heightSpin.getSelectedItem().toString());
            } else {
                heightValue.setError("Invalid format!");
            }
        }

        // Modify Sex
        else if (id == R.id.clearSex) {
            DBHelper.clearData("Sex");
            sexSpin.setSelection(0);
        } else if (id == R.id.updateSex) {
            DBHelper.updateData("Sex", sexSpin.getSelectedItem().toString());
        }

        // Modify Allergies
        else if (id == R.id.clearAllergies) {
            DBHelper.clearData("HealthCondition");
            allergiesValue.setText("");
        } else if (id == R.id.updateAllergies) {
            DBHelper.updateData("HealthCondition", allergiesValue.getText().toString());
        }

        // Modify Reasons
        else if (id == R.id.clearReasons) {
            DBHelper.clearData("ReasonForDownloading");
            reasonSpin.setSelection(0);
        } else if (id == R.id.updateReasons) {
            DBHelper.updateData("ReasonForDownloading", reasonSpin.getSelectedItem().toString());
        }
    }

}
