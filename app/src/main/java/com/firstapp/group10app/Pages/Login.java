
package com.firstapp.group10app.Pages;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.firstapp.group10app.R;
import com.google.android.material.button.MaterialButton;

public class Login extends AppCompatActivity implements View.OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        TextView username = (TextView) findViewById(R.id.username);
        TextView password = (TextView) findViewById(R.id.password);

        Button loginbtn = (Button) findViewById(R.id.logginbtn);

        //admin and admin
        //when button is clicked return username and password type
        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(username.getText().toString().equals("admin") && password.getText().toString().equals("admin")){
                    //correct
                    Toast.makeText(Login.this, "LOGIN SUCCESSFULL!",Toast.LENGTH_SHORT).show();
                }else
                    //incorrect
                    Toast.makeText(Login.this, "LOGIN FAILED!",Toast.LENGTH_SHORT).show();

            }
        });

        TextView temp = findViewById(R.id.forgotPassword);
        temp.setOnClickListener(this);
    }

    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.forgotPassword) {
            startActivity(new Intent(Login.this, ForgotPassword.class));
        }
    }
}