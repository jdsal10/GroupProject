
package com.firstapp.group10app.Pages;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.*;
import android.widget.TextView;

import com.firstapp.group10app.R;

public class Login extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        TextView temp = findViewById(R.id.forgotPassword);
        temp.setOnClickListener(this);
    }

    public void onClick(View v){
        int id = v.getId();
        if(id == R.id.forgotPassword) {
            startActivity(new Intent(Login.this, forgotPassword.class));
        }
    }
}


