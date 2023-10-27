package com.firstapp.group10app;

import androidx.appcompat.app.AppCompatActivity;
import android.view.*;

import android.os.Bundle;
import android.widget.Button;
import android.content.Intent;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button goToRegisterButton;
    private Button goToLoginButton;

    private TextView skipText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        goToRegisterButton = findViewById(R.id.goToRegister);
        goToRegisterButton.setOnClickListener(this);

        goToLoginButton = findViewById(R.id.goToLogin);
        goToLoginButton.setOnClickListener(this);

        skipText = findViewById(R.id.skipToHome);
        skipText.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.goToRegister) {
            Intent welcomeToRegister = new Intent(MainActivity.this, Registration.class);
            startActivity(welcomeToRegister);
        }
        else if (id == R.id.goToLogin) {
            Intent welcomeToLogin = new Intent(MainActivity.this, Login.class);
            startActivity(welcomeToLogin);
        }
        else if (id == R.id.skipToHome) {
            Intent skipToHome = new Intent(MainActivity.this, Home.class);
            //Add form of identifier for the rest of the code to fucntion.
            startActivity(skipToHome);
        }
    }
}