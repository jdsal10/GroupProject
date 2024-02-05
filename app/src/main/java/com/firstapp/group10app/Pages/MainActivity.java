package com.firstapp.group10app.Pages;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.firstapp.group10app.DB.DBConnection;
import com.firstapp.group10app.Other.Session;
import com.firstapp.group10app.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button goToLoginButton = findViewById(R.id.goToLogin);
        goToLoginButton.setOnClickListener(this);

        Button goToRegisterButton = findViewById(R.id.goToRegister);
        goToRegisterButton.setOnClickListener(this);

        TextView skipText = findViewById(R.id.skipToHome);
        skipText.setOnClickListener(this);

        // Temporary button to navigate to the sitemap
        Button goToSitemapButton = findViewById(R.id.goToSitemap);
        goToSitemapButton.setOnClickListener(this);

        // Test database connection
        Session.dbStatus = DBConnection.testConnection();

        // Default value.
        Session.signedIn = false;

        // If the connection false, disable the login.
        if (!Session.dbStatus) {
            goToLoginButton.setEnabled(false);
            goToLoginButton.setAlpha(.5f);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.goToLogin) {
            startActivity(new Intent(MainActivity.this, Login.class));
            overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
        } else if (id == R.id.skipToHome) {
            Session.userEmail = null;
            Session.signedIn = false;
            startActivity(new Intent(MainActivity.this, Home.class));
        } else if (id == R.id.goToSitemap) {
            startActivity(new Intent(MainActivity.this, SitemapActivity.class));
        } else if (id == R.id.goToRegister) {
            startActivity(new Intent(MainActivity.this, Registration.class));
        }
    }
}