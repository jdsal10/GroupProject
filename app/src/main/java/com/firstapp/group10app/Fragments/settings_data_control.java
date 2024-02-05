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
    static TextView dobValue;
    TextView weightValue;
    TextView heightValue;
    TextView allergiesValue;
    static TextView sexValue;
    TextView reasonValue;
    Button updateAll;
    Button dobModify, weightModify, heightModify, sexModify, allergiesModify, reasonsModify;
    ArrayList<String> details;

    public settings_data_control() {
        super(R.layout.fragment_settings_data_control);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


//    private void dobAddTextChangedListener() {
//        dobValue.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//            }
//
//            int ind, ind1 = 0;
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                // Calculates "-" after year
//                if (s.length() == 4 && ind == 0) {
//                    dobValue.setText(String.format("%s-", s));
//                    dobValue.setSelection(s.length() + 1);
//                    ind = 1;
//                } else if (s.length() < 4 && ind == 1) {
//                    ind = 0;
//                }
//
//                // Calculates "-" after month
//                if (s.length() == 7 && ind1 == 0) {
//                    dobValue.setText(String.format("%s-", s));
//                    dobValue.setSelection(s.length() + 1);
//                    ind1 = 1;
//                } else if (s.length() < 7 && ind1 == 1) {
//                    ind1 = 0;
//                }
//            }
//        });
//    }

    public void updateValues(ArrayList<String> info) {
        // Debug output
        System.out.println(info.toString());

        // Since DOB is a regular value, no formatting is required
        dobValue.setText(info.get(0));

        // Updates the view value of weight and its units.
        String tempWeight = info.get(1);
        if (tempWeight == null || TextUtils.isEmpty(tempWeight) || tempWeight.equals("")) {
//            weightSpin.setSelection(0);
            weightValue.setText("");
        } else {
//            String weightUnits = info.get(1).split(" ")[1];
            weightValue.setText(info.get(1));

//            if (weightUnits == null) {
//                weightSpin.setSelection(0);
//            } else if (weightUnits.equals("kg")) {
//                weightSpin.setSelection(1);
//            } else if (weightUnits.equals("lbs")) {
//                weightSpin.setSelection(2);
//            }
        }

        // Updates the view value of height and its units.
        String tempHeight = info.get(2);
        if (tempHeight == null || TextUtils.isEmpty(tempHeight) || tempHeight.equals("")) {
//            heightSpin.setSelection(0);
            heightValue.setText("");
        } else {
//            String heightUnits = info.get(2).split(" ")[1];
            heightValue.setText(info.get(2));

//            if (heightUnits == null) {
//                heightSpin.setSelection(0);
//            } else if (heightUnits.equals("cm")) {
//                heightSpin.setSelection(1);
//            } else if (heightUnits.equals("inch")) {
//                heightSpin.setSelection(2);
//            }
        }

        // Updates the units for sex.
        String selectedSex = info.get(3).trim();

        switch (selectedSex) {
            case "":
//                sexSpin.setSelection(0);
                sexValue.setText("");
                break;
            case "M":
//                sexSpin.setSelection(1);
                sexValue.setText("Male");
                break;
            case "F":
//                sexSpin.setSelection(2);
                sexValue.setText("Female");
                break;
            case "O":
//                sexSpin.setSelection(3);
                sexValue.setText("Other");
                break;
        }

        allergiesValue.setText(info.get(4));

        // Updates "reason for downloading"
        switch (info.get(5)) {
            case "":
//                reasonSpin.setSelection(0);
                break;
            case "I want to lose weight":
//                reasonSpin.setSelection(1);
                reasonValue.setText("I want to lose weight");
                break;
            case "I want to gain weight":
//                reasonSpin.setSelection(2);
                reasonValue.setText("I want to gain weight");
                break;
            case "I want to maintain my weight":
//                reasonSpin.setSelection(3);
                reasonValue.setText("I want to maintain my weight");
                break;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_settings_data_control, container, false);

        // Initialises the widgets
        updateAll = rootView.findViewById(R.id.updateAllValues);
        updateAll.setOnClickListener(this);
        // Initialise the update buttons.


        // Initialise all spinners.
//        weightSpin = rootView.findViewById(R.id.weightUnitSpinner);
//        heightSpin = rootView.findViewById(R.id.heightUnitSpinner);
//        sexSpin = rootView.findViewById(R.id.sexValue);
//        reasonSpin = rootView.findViewById(R.id.reasonSpin);

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
//        reasonSpin = rootView.findViewById(R.id.reasonSpin);
//        reasonSpin.setSelected(true);
        allergiesValue = rootView.findViewById(R.id.allergiesValue);
        allergiesValue.setSelected(true);
        sexValue = rootView.findViewById(R.id.sexValue);
        reasonValue = rootView.findViewById(R.id.reasonValue);

        // Sets onClickListener for the buttons.
        dobModify.setOnClickListener(this);

        weightModify.setOnClickListener(this);

        heightModify.setOnClickListener(this);

        sexModify.setOnClickListener(this);

        allergiesModify.setOnClickListener(this);

        reasonsModify.setOnClickListener(this);
//        dobAddTextChangedListener();

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

        // Modify DOB
//        if (id == R.id.clearDOB) {
//            DBHelper.clearData("DOB");
//            dobValue.setText("");
//        } else if (id == R.id.updateAllValues) {
//            if (Validator.dobValid(dobValue.getText().toString())) {
//                DBHelper.updateData("DOB", dobValue.getText().toString());
//            } else {
//                dobValue.setError("Invalid format!");
//            }
//            if (Validator.weightValid(weightValue.getText().toString(), weightSpin.getSelectedItem().toString())) {
//                DBHelper.updateData("Weight", weightValue.getText().toString() + " " + weightSpin.getSelectedItem().toString());
//            } else {
//                weightValue.setError("Invalid format!");
//            }
//
//            if (Validator.heightValid(heightValue.getText().toString(), heightSpin.getSelectedItem().toString())) {
//                DBHelper.updateData("Height", heightValue.getText().toString() + " " + heightSpin.getSelectedItem().toString());
//            } else {
//                heightValue.setError("Invalid format!");
//            }
//            DBHelper.updateData("Sex", sexSpin.getSelectedItem().toString());
//
//            DBHelper.updateData("HealthCondition", allergiesValue.getText().toString());
//
//            DBHelper.updateData("ReasonForDownloading", reasonSpin.getSelectedItem().toString());
//
//        }

        // Modify Weight
//        else if (id == R.id.clearWeight) {
//            DBHelper.clearData("Weight");
//            weightValue.setText("");
//        }
//
//        // Modify Height
//        else if (id == R.id.clearHeight) {
//            DBHelper.clearData("Height");
//            heightValue.setText("");
//        }
//        // Modify Sex
//        else if (id == R.id.clearSex) {
//            DBHelper.clearData("Sex");
//            sexSpin.setSelection(0);
//        }
//
//        // Modify Allergies
//        else if (id == R.id.clearAllergies) {
//            DBHelper.clearData("HealthCondition");
//            allergiesValue.setText("");
//        }
//
//        // Modify Reasons
//        else if (id == R.id.clearReasons) {
//            DBHelper.clearData("ReasonForDownloading");
//            reasonSpin.setSelection(0);
//        }
    }

    public static void updateValue(String valueTitle, String newValue) {
        if(valueTitle.equals("DOB")) {
            dobValue.setText(newValue);
        }
        else if (valueTitle.equals("Sex")) {
//            if(newValue.equals("Male")) {
//                sexValue.setText("Male");
//            }
//            else if(newValue.equals("Female")) {
//                sexValue.setText("Female");
//            }
//            else if(newValue.equals("O")) {
//                sexValue.setText("Other");
//            }
            sexValue.setText(newValue);
        }
    }
}
