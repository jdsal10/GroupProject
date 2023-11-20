package com.firstapp.group10app.Pages;
import androidx.appcompat.app.AppCompatActivity;

import android.view.*;

import android.os.Bundle;
import android.widget.Button;
import android.content.Intent;
import android.widget.TextView;
import com.firstapp.group10app.R;

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
            overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
        }
        else if (id == R.id.goToLogin) {
            startActivity(new Intent(MainActivity.this, Login.class));
            overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
        }
        else if (id == R.id.skipToHome) {
            startActivity(new Intent(MainActivity.this, Home.class));
        }
    }
}