package com.firstapp.group10app.Pages;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.firstapp.group10app.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String DATABASE_NAME = "healthdata";
    public static final String url = "jdbc:mysql://healthdata.cgzabjirm4kt.eu-west-2.rds.amazonaws.com:3306/" + DATABASE_NAME;
    public static final String username = "healthadmin", password = "Comp6000health";

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
        System.out.println("Attempting");
        connect();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.goToRegister) {
            startActivity(new Intent(MainActivity.this, Registration.class));
        } else if (id == R.id.goToLogin) {
            startActivity(new Intent(MainActivity.this, Login.class));
        } else if (id == R.id.skipToHome) {
            startActivity(new Intent(MainActivity.this, Home.class));
            //Add form of identifier for the rest of the code to function.
        }
    }
}