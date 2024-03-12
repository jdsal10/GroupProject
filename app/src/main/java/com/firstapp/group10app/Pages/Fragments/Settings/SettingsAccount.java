package com.firstapp.group10app.Pages.Fragments.Settings;

import static com.firstapp.group10app.Other.Validator.passwordValid;
import static com.firstapp.group10app.Other.Validator.passwordValidator;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import com.firstapp.group10app.DB.DbHelper;
import com.firstapp.group10app.Other.Session;
import com.firstapp.group10app.Pages.MainActivity;
import com.firstapp.group10app.R;

public class SettingsAccount extends Fragment implements View.OnClickListener {

    Button deleteAccount, changePassword;

    public SettingsAccount() {
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
        View rootView = inflater.inflate(R.layout.activity_settings_account, container, false);

        deleteAccount = rootView.findViewById(R.id.deleteAccountButton);
        deleteAccount.setOnClickListener(this);

        changePassword = rootView.findViewById(R.id.changePasswordSignedIn);
        changePassword.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.deleteAccountButton) {
            showConfirmation();
        } else if (id == R.id.changePasswordSignedIn) {
            changePassword();
        }
    }

    public void showConfirmation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        LayoutInflater inflater = getLayoutInflater();

        View dialogView = inflater.inflate(R.layout.popup_confirm_delete_account, null);
        builder.setView(dialogView);
        AlertDialog alertDialog = builder.create();

        Button confirm = dialogView.findViewById(R.id.deleteConfirm);
        EditText passwordDelete = dialogView.findViewById(R.id.accountDeletePassword);

        confirm.setOnClickListener(v -> {
            String password = passwordDelete.getText().toString();
            DbHelper db = new DbHelper();
            try {
                if (db.checkUser(Session.userEmail, password)) {
                    // Add logic for deletion below - requires integration to workouts.
                    db.deleteUser(Session.userEmail);

                    Log.d("SettingsAccount.showConfirmation", "CONFIRM DELETION!");

                    startActivity(new Intent(requireContext(), MainActivity.class));
                } else {
                    passwordDelete.setError("Incorrect password used.");
//                    alertDialog.dismiss();
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        // Show the dialog
        alertDialog.show();
    }

    public void changePassword() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        LayoutInflater inflater = getLayoutInflater();

        View dialogView = inflater.inflate(R.layout.popup_change_password, null);
        builder.setView(dialogView);
        AlertDialog alertDialog = builder.create();

        Button passwordChangeConfirm = dialogView.findViewById(R.id.changePasswordConfirm);

        EditText currentPassword = dialogView.findViewById(R.id.currentPassword);
        EditText newPassword1 = dialogView.findViewById(R.id.newPassword1);
        EditText newPassword2 = dialogView.findViewById(R.id.newPassword2);

        passwordChangeConfirm.setOnClickListener(v -> {
            String cp = currentPassword.getText().toString();
            String np1 = newPassword1.getText().toString();
            String np2 = newPassword2.getText().toString();

            DbHelper db = new DbHelper();
            try {
                if (!db.checkUser(Session.userEmail, cp)) {
                    currentPassword.setError("Incorrect Password");
                } else if (!np1.equals(np2)) {
                    newPassword1.setError("The passwords do not match");
                } else if (!passwordValid(np1)) {
                    newPassword1.setError(passwordValidator(np1));
                } else {
                    DbHelper.updateData("Password", np1);
                    alertDialog.dismiss();
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        alertDialog.show();
    }
}