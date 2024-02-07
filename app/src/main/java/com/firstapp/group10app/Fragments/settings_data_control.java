package com.firstapp.group10app.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.firstapp.group10app.DB.DBHelper;
import com.firstapp.group10app.Other.*;
import com.firstapp.group10app.Pages.modify_data;
import com.firstapp.group10app.R;

import java.sql.ResultSet;
import java.util.ArrayList;

public class settings_data_control extends Fragment implements View.OnClickListener {
    // Declared variables
    static TextView dobValue, weightValue, heightValue, allergiesValue, sexValue, reasonsValue;
    Button dobModify, weightModify, heightModify, sexModify, allergiesModify, reasonsModify;
    ArrayList<String> details;

    public settings_data_control() {
        super(R.layout.fragment_settings_data_control);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void updateValues(ArrayList<String> info) {
        // Since DOB is a regular value, no formatting is required
        dobValue.setText(info.get(0));

        // Updates the view value of weight and its units.
        String tempWeight = info.get(1);
        if (tempWeight == null || TextUtils.isEmpty(tempWeight) || tempWeight.equals("")) {
            weightValue.setText("");
        } else {
            weightValue.setText(info.get(1));
        }

        // Updates the view value of height and its units.
        String tempHeight = info.get(2);
        if (tempHeight == null || TextUtils.isEmpty(tempHeight) || tempHeight.equals("")) {
            heightValue.setText("");
        } else {
            heightValue.setText(info.get(2));
        }

        // Updates the units for sex.
        String selectedSex = info.get(3).trim();

        switch (selectedSex) {
            case "":
                sexValue.setText("");
                break;
            case "M":
                sexValue.setText("Male");
                break;
            case "F":
                sexValue.setText("Female");
                break;
            case "O":
                sexValue.setText("Other");
                break;
        }

        allergiesValue.setText(info.get(4));

        // Updates "reason for downloading"
        switch (info.get(5)) {
            case "":
                break;
            case "I want to lose weight":
                reasonsValue.setText("I want to lose weight");
                break;
            case "I want to gain weight":
                reasonsValue.setText("I want to gain weight");
                break;
            case "I want to maintain my weight":
                reasonsValue.setText("I want to maintain my weight");
                break;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_settings_data_control, container, false);

        // Initialise the clear buttons
        dobModify = rootView.findViewById(R.id.modifyDOB);
        weightModify = rootView.findViewById(R.id.modifyWeight);
        heightModify = rootView.findViewById(R.id.modifyHeight);
        sexModify = rootView.findViewById(R.id.modifySex);
        allergiesModify = rootView.findViewById(R.id.modifyAllergies);
        reasonsModify = rootView.findViewById(R.id.modifyReasons);

        // Initialize the value fields.
        dobValue = rootView.findViewById(R.id.DOBValue);
        weightValue = rootView.findViewById(R.id.weightValue);
        heightValue = rootView.findViewById(R.id.heightValue);
        allergiesValue = rootView.findViewById(R.id.allergiesValue);
        sexValue = rootView.findViewById(R.id.sexValue);
        reasonsValue = rootView.findViewById(R.id.reasonValue);

        // Sets onClickListener for the buttons.
        dobModify.setOnClickListener(this);
        weightModify.setOnClickListener(this);
        heightModify.setOnClickListener(this);
        sexModify.setOnClickListener(this);
        allergiesModify.setOnClickListener(this);
        reasonsModify.setOnClickListener(this);

        // Sets the fields as selected so the text auto scrolls.
        allergiesValue.setSelected(true);
        reasonsValue.setSelected(true);

        // Declares an array of the users details.
        String currentUser = Session.userEmail;
        details = new ArrayList<>();

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

        if(id == R.id.modifyDOB) {
            modify_data customDialog = new modify_data(getContext(), new String[]{"DOB", dobValue.getText().toString()});
            customDialog.show();
        }
        else if (id == R.id.modifySex) {
            modify_data customDialog = new modify_data(getContext(), new String[]{"Sex", sexValue.getText().toString()});
            customDialog.show();
        }
        else if (id == R.id.modifyWeight) {
            modify_data customDialog = new modify_data(getContext(), new String[]{"Weight", weightValue.getText().toString()});
            customDialog.show();
        }
        else if (id == R.id.modifyHeight) {
            modify_data customDialog = new modify_data(getContext(), new String[]{"Height", heightValue.getText().toString()});
            customDialog.show();
        }
        else if (id == R.id.modifyAllergies) {
            modify_data customDialog = new modify_data(getContext(), new String[]{"Allergies", allergiesValue.getText().toString()});
            customDialog.show();
        }
        else if (id == R.id.modifyReasons) {
            modify_data customDialog = new modify_data(getContext(), new String[]{"Reasons", reasonsValue.getText().toString()});
            customDialog.show();
        }
    }

    public static void updateValue(String valueTitle, String newValue) {
        switch (valueTitle) {
            case "DOB":
                dobValue.setText(newValue);
                break;
            case "Sex":
                sexValue.setText(newValue);
                break;
            case "Weight":
                weightValue.setText(newValue);
                break;
            case "Height":
                heightValue.setText(newValue);
                break;
            case "Allergies":
                allergiesValue.setText(newValue);
                break;
            case "Reasons":
                reasonsValue.setText(newValue);
                break;
        }
    }
}
