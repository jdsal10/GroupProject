package com.firstapp.group10app.Pages.Fragments.Settings;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.firstapp.group10app.Other.Session;
import com.firstapp.group10app.R;

import java.util.ArrayList;
import java.util.Arrays;

public class SettingsDataControl extends Fragment implements View.OnClickListener {
    // Declared variables
    private static TextView dobValue, weightValue, heightValue, allergiesValue, sexValue, reasonsValue;

    public SettingsDataControl() {
        super(R.layout.activity_settings_data_control);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void updateValues(ArrayList<String> info) {
        System.out.println(Arrays.toString(Session.getUserDetails()));
        // Since DOB is a regular value, no formatting is required
        dobValue.setText(info.get(0));

        // Updates the view value of weight and its units.
        String tempWeight = info.get(1);
        if (tempWeight == null || TextUtils.isEmpty(tempWeight) || tempWeight.isEmpty()) {
            weightValue.setText("");
        } else {
            weightValue.setText(info.get(1));
        }

        // Updates the view value of height and its units.
        String tempHeight = info.get(2);
        if (tempHeight == null || TextUtils.isEmpty(tempHeight) || tempHeight.isEmpty()) {
            heightValue.setText("");
        } else {
            heightValue.setText(info.get(2));
        }

        // Updates the units for sex.
        if (info.get(3) != null) sexValue.setText(info.get(3).trim());
        else sexValue.setText("");

        allergiesValue.setText(info.get(4));

        // Updates "reason for downloading"
        if (info.get(5) != null) {
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_settings_data_control, container, false);

        // Initialise the clear buttons
        LinearLayout dobModify = rootView.findViewById(R.id.dobLayout);
        LinearLayout weightModify = rootView.findViewById(R.id.weightLayout);
        LinearLayout heightModify = rootView.findViewById(R.id.heightLayout);
        LinearLayout sexModify = rootView.findViewById(R.id.sexLayout);
        LinearLayout allergiesModify = rootView.findViewById(R.id.allergiesLayout);
        LinearLayout reasonsModify = rootView.findViewById(R.id.reasonsLayout);

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
        ArrayList<String> details = new ArrayList<>();

        // Gets the details of the current user.
        try {
            details.add(Session.getUserDetails()[0]);
            details.add(Session.getUserDetails()[1]);
            details.add(Session.getUserDetails()[2]);
            details.add(Session.getUserDetails()[3]);
            details.add(Session.getUserDetails()[4]);
            details.add(Session.getUserDetails()[5]);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        updateValues(details);
        return rootView;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.dobLayout) {
            SettingsModifyData_Dialog customDialog = new SettingsModifyData_Dialog(getContext(), new String[]{"DOB", dobValue.getText().toString()});
            customDialog.show();
        } else if (id == R.id.sexLayout) {
            SettingsModifyData_Dialog customDialog = new SettingsModifyData_Dialog(getContext(), new String[]{"Sex", sexValue.getText().toString()});
            customDialog.show();
        } else if (id == R.id.weightLayout) {
            SettingsModifyData_Dialog customDialog = new SettingsModifyData_Dialog(getContext(), new String[]{"Weight", weightValue.getText().toString()});
            customDialog.show();
        } else if (id == R.id.heightLayout) {
            SettingsModifyData_Dialog customDialog = new SettingsModifyData_Dialog(getContext(), new String[]{"Height", heightValue.getText().toString()});
            customDialog.show();
        } else if (id == R.id.allergiesLayout) {
            SettingsModifyData_Dialog customDialog = new SettingsModifyData_Dialog(getContext(), new String[]{"Allergies", allergiesValue.getText().toString()});
            customDialog.show();
        } else if (id == R.id.reasonsLayout) {
            SettingsModifyData_Dialog customDialog = new SettingsModifyData_Dialog(getContext(), new String[]{"Reasons", reasonsValue.getText().toString()});
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