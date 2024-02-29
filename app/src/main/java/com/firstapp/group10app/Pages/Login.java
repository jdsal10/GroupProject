
package com.firstapp.group10app.Pages;

import static com.firstapp.group10app.Other.Encryption.getSHA;
import static com.firstapp.group10app.Other.Encryption.toHexString;

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

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Arrays;

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

            String result = null;
            try {
                result = toHexString(getSHA(passwordText));
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }

            System.out.println("TESTING TEXT : " + emailText + "TESTING PASSWORD: " + result.replace("[","").replace("]",""));
            try {
                DBHelper db = new DBHelper();
                if (db.checkUser(emailText,result.replace("[","").replace("]",""))) {
                    Toast.makeText(Login.this, "Welcome \n" + emailText + " ! ", Toast.LENGTH_SHORT).show();
                    Session.userEmail = emailText;
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
}
