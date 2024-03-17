package com.firstapp.group10app.Pages.Fragments.MainOptions;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.firstapp.group10app.DB.OnlineDb.DbConnection;
import com.firstapp.group10app.Other.Session;
import com.firstapp.group10app.R;

public class Home extends Fragment {
    private TextView workoutsNum;
    public Home() {
        super(R.layout.activity_home);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_home, container, false);

        // For now, a check should run at the start of each file for DB connection.
        Session.setDbStatus(DbConnection.testConnection());


        //edit number values
        workoutsNum = rootView.findViewById(R.id.workoutCountTextView);

        //Dynamically add value
        setWorkoutCount();

        super.onCreate(savedInstanceState);

        return rootView;
    }

    // Method to dynamically edit workoutCountTextView
    public void setWorkoutCount() {
        if (workoutsNum != null) {
            workoutsNum.setText(String.valueOf(2));
        }
    }
}