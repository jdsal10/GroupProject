package com.firstapp.group10app.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.firstapp.group10app.DB.DBHelper;
import com.firstapp.group10app.Other.*;
import com.firstapp.group10app.R;

import java.sql.ResultSet;
import java.util.ArrayList;

public class settings_data_control extends Fragment {

    EditText dobValue;
    EditText weightValue;
    EditText heightValue;
    EditText sexValue;
    EditText allergiesValue;
    EditText reasonsValue;

    private static ArrayList<String> details = new ArrayList<>();

    public settings_data_control() {
        super(R.layout.fragment_settings_data_control);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void updateValues(ArrayList<String> info) {
        dobValue.setText(info.get(0));
        weightValue.setText(info.get(1));
        heightValue.setText(info.get(2));
        sexValue.setText(info.get(3));
        allergiesValue.setText(info.get(4));
        reasonsValue.setText(info.get(5));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_settings_data_control, container, false);

        dobValue = rootView.findViewById(R.id.DOBValue);
        weightValue = rootView.findViewById(R.id.weightValue);
        heightValue = rootView.findViewById(R.id.heightValue);
        sexValue = rootView.findViewById(R.id.sexValue);
        allergiesValue = rootView.findViewById(R.id.allerguesValue);
        reasonsValue = rootView.findViewById(R.id.reasonValue);

        String currentUser = Session.userEmail;
        details = new ArrayList<>();

        ResultSet data = DBHelper.getUser(currentUser);

        try {
            if (data.next()) { // Move the cursor to the first row
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
}