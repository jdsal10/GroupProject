package com.firstapp.group10app.Pages;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.firstapp.group10app.DB.OnlineDb.OnlineDbHelper;
import com.firstapp.group10app.Other.Session;
import com.firstapp.group10app.R;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Login extends AppCompatActivity implements View.OnClickListener {
    private EditText Email, Password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
            String emailText = Email.getText().toString();
            String passwordText = Password.getText().toString();

            // I commented out the encryption code because I globalized the encryption method
            try {
                OnlineDbHelper db = new OnlineDbHelper();
                if (db.checkUserExistsAndCorrectPassword(emailText, passwordText)) { // .replace("[", "").replace("]", ""))) {
                    Toast.makeText(Login.this, "Welcome \n" + emailText + " ! ", Toast.LENGTH_SHORT).show();
                    setSessionData(emailText);
                    Session.setUserEmail(emailText);
                    Session.setSignedIn(true);

                    startActivity(new Intent(getApplicationContext(), ActivityContainer.class));
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                } else {
                    Toast.makeText(Login.this, "Login Failed. Check your details!", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else if (id == R.id.forgotPassword) {
            startActivity(new Intent(Login.this, ForgotPassword.class));
            overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
        } else if (id == R.id.register) {
            startActivity(new Intent(Login.this, Registration.class));
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        }
    }

    public void setSessionData(String email) throws SQLException {
        OnlineDbHelper db = new OnlineDbHelper();
        ResultSet data = db.getUser(email);

        //Include user statistics in Session
        Integer workouts = db.getTotalinHistory(email);
        System.out.println("workouts num :" + workouts);


        if (data.next()) {
            Session.setUserDetails(new String[7]);
            Session.getUserDetails()[0] = data.getString("DOB");
            Session.getUserDetails()[1] = data.getString("Weight");
            Session.getUserDetails()[2] = data.getString("Height");

            if (data.getString("Sex").equals("M")) {
                Session.getUserDetails()[3] = "Male";
            } else if (data.getString("Sex").equals("F")) {
                Session.getUserDetails()[3] = "Female";
            }

            Session.getUserDetails()[4] = data.getString("HealthCondition");
            Session.getUserDetails()[5] = data.getString("ReasonForDownloading");
            Session.getUserDetails()[6] = Integer.toString(workouts);
            //Add history element to session
        } else {
            Log.d("Login.setSessionData", "No data found for the user with email: " + email);
        }
    }
}