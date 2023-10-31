package com.firstapp.group10app;

import androidx.appcompat.app.AppCompatActivity;
import android.view.*;

import android.os.Bundle;
import android.widget.Button;
import android.content.Intent;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Interactions initialised
        Button goToRegisterButton = findViewById(R.id.goToRegister);
        goToRegisterButton.setOnClickListener(this);

        Button goToLoginButton = findViewById(R.id.goToLogin);
        goToLoginButton.setOnClickListener(this);

        TextView skipText = findViewById(R.id.skipToHome);
        skipText.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.goToRegister) {
            startActivity(new Intent(MainActivity.this, Registration.class));
        }
        else if (id == R.id.goToLogin) {
            startActivity(new Intent(MainActivity.this, Login.class));
        }
        else if (id == R.id.skipToHome) {
            startActivity(new Intent(MainActivity.this, Home.class));
            //Add form of identifier for the rest of the code to function.
        }
    }
}