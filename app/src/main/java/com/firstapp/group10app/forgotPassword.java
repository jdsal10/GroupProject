package com.firstapp.group10app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.widget.*;

import android.view.*;

public class forgotPassword extends AppCompatActivity implements View.OnClickListener {

    private Button sendEmail;
    private EditText emailToSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        emailToSend = findViewById(R.id.editTextTextEmailAddress);
        emailToSend.setOnClickListener(this);

        sendEmail = findViewById(R.id.sendEmail);
        sendEmail.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.sendEmail) {
            if (emailToSend.getText().toString().equals("")) {
                //Send the email
            }
        }
    }
}