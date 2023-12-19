package com.firstapp.group10app.Fragments;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.firstapp.group10app.DB.DBHelper;
import com.firstapp.group10app.Other.Session;
import com.firstapp.group10app.R;

import java.sql.SQLException;

public class settings_account extends Fragment implements View.OnClickListener{

    Button deleteAccount;
    public settings_account() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_settings_account, container, false);

        deleteAccount = rootView.findViewById(R.id.deleteAccountButton);
        deleteAccount.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.deleteAccountButton) {
        showConfirmation();
        }
    }

    public void showConfirmation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        LayoutInflater inflater = getLayoutInflater();

        View dialogView = inflater.inflate(R.layout.delete_account_confirm, null);
        builder.setView(dialogView);
        AlertDialog alertDialog = builder.create();

        Button confirm = dialogView.findViewById(R.id.deleteConfirm);
        EditText passwordDelete = dialogView.findViewById(R.id.accountDeletePassword);

        confirm.setOnClickListener(v -> {
            String password = passwordDelete.getText().toString();
            DBHelper db = new DBHelper();
            try {
                if(db.checkUser(Session.userEmail, password)) {
                    System.out.println("CONFIRM DELETION!");
                    // Add logic for deletion below - requires integration to workouts.
                }
                else {
                    passwordDelete.setError("Incorrect password used.");
//                    alertDialog.dismiss();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
        // Show the dialog
        alertDialog.show();
    }
}