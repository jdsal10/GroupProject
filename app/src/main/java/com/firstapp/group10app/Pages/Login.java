
package com.firstapp.group10app.Pages;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.firstapp.group10app.DB.DBConnection;
import com.firstapp.group10app.DB.DBHelper;
import com.firstapp.group10app.Other.Session;
import com.firstapp.group10app.R;

import java.sql.SQLException;

public class Login extends AppCompatActivity implements View.OnClickListener {
    private EditText Email, Password;
    private String EmailText, PasswordText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        createTestUser();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Email = findViewById(R.id.username);
        Password = findViewById(R.id.password);

        Button LoginBtn = findViewById(R.id.loginButton);
        LoginBtn.setOnClickListener(this);

        TextView register = findViewById(R.id.register);
        register.setOnClickListener(this);

        TextView temp = findViewById(R.id.forgotPassword);
        temp.setOnClickListener(this);
    }

    //Actions for when login button or forgot password is pressed
    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.loginButton) {
            EmailText = Email.getText().toString();
            PasswordText = Password.getText().toString();
            System.out.println("TESTING TEXT : " + EmailText + "TESTING PASSWORD: " + PasswordText);
            try {
                DBHelper db = new DBHelper();
                if (db.checkUser(EmailText, PasswordText)) {
                    Toast.makeText(Login.this, "Welcome \n" + EmailText + " ! ", Toast.LENGTH_SHORT).show();
                    Session.userEmail = EmailText;
                    Session.signedIn = true;
                    startActivity(new Intent(getApplicationContext(), Home.class));
                } else {
                    Toast.makeText(Login.this, "Login Failed. Check your details!", Toast.LENGTH_SHORT).show();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else if (id == R.id.forgotPassword) {
            startActivity(new Intent(Login.this, ForgotPassword.class));
        } else if (id == R.id.register) {
            startActivity(new Intent(Login.this, Registration.class));
            overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
        }
    }


    //trying to add some data to the database to test login
    public void createTestUser() {
        try {
            DBConnection db = new DBConnection();
            db.executeStatement("INSERT INTO HealthData.Users (Email, PreferredName, Password, DOB, Weight, Height, Sex, HealthCondition, ReasonForDownloading) VALUES ('user@example.com', 'Juan', 'password', '1990-01-01', 70.5, 175.0, 'Male', 'None', 'Testing app')");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}