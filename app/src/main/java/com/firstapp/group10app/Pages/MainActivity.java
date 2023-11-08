package com.firstapp.group10app.Pages;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.StrictMode;
import android.util.Log;
import android.view.*;

import android.os.Bundle;
import android.widget.Button;
import android.content.Intent;
import android.widget.TextView;
import com.firstapp.group10app.DB.DBConnection;
import com.firstapp.group10app.R;

import java.sql.ResultSet;
import java.sql.SQLException;

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
        System.out.println("Attempting");
        try {
            connect();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void connect() throws SQLException {
        try {
            DBConnection d = new DBConnection();
            d.executeStatement("INSERT INTO HealthData.USER_TABLE (Email, PreferredName, Password) VALUES ('Ethan', 'test@gmail.com', 'guess123');");
            ResultSet set = d.executeQuery("SELECT * FROM HealthData.USER_TABLE");
            while (set.next()) {
                System.out.println(set.getString(3));
            }
        }
        catch (Exception e) {
            throw new SQLException(e);
        }


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
        }
    }
}