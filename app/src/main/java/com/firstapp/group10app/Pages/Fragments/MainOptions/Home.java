package com.firstapp.group10app.Pages.Fragments.MainOptions;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.firstapp.group10app.DB.DbConnection;
import com.firstapp.group10app.Other.Session;
import com.firstapp.group10app.R;

public class Home extends Fragment {
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
        Session.dbStatus = DbConnection.testConnection();

        super.onCreate(savedInstanceState);

        return rootView;
    }
}