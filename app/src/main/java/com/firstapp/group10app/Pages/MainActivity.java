package com.firstapp.group10app.Pages;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.firstapp.group10app.DB.DbConnection;
import com.firstapp.group10app.DB.LocalDb.LocalDb;
import com.firstapp.group10app.Other.Session;
import com.firstapp.group10app.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Start the local database on a new thread
        if (Session.localDB == null) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    LocalDb localDb = new LocalDb(MainActivity.this);

                    // Perform database operations
                    // Insert sample data into the database
                    localDb.insertSampleData();
                    // Store the LocalDb instance in the Session.localDB variable
                    Session.localDB = localDb.getReadableDatabase();

                    // Close the database connection
                    localDb.close();
                }
            }).start();
        }

        View rootLayout = findViewById(android.R.id.content);

        AlphaAnimation fadeInAnimation = new AlphaAnimation(0.0f, 1.0f);
        fadeInAnimation.setDuration(1000);

        rootLayout.startAnimation(fadeInAnimation);
        Button goToLoginButton = findViewById(R.id.goToLogin);
        goToLoginButton.setOnClickListener(this);

        TextView goToRegisterButton = findViewById(R.id.goToRegister);
        goToRegisterButton.setOnClickListener(this);

        TextView skipText = findViewById(R.id.anonymous);
        skipText.setOnClickListener(this);

        // Test database connection
        Session.dbStatus = DbConnection.testConnection();

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
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        } else if (id == R.id.anonymous) {
            Session.userEmail = null;
            Session.signedIn = false;

            Intent intent = new Intent(getApplicationContext(), ActivityContainer.class);
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        } else if (id == R.id.goToRegister) {
            startActivity(new Intent(MainActivity.this, Registration.class));
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        }
    }
}