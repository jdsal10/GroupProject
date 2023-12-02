package com.firstapp.group10app.Pages;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.firstapp.group10app.R;
public class SitemapActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sitemap);

        LinearLayout layout = findViewById(R.id.sitemap_layout);

        // Add a button for each page in the app
        // Sprint 1 pages
        addButton(layout, "MainActivity", MainActivity.class);
        addButton(layout, "Registration Page", Registration.class);
        addButton(layout, "Login Page", Login.class);
        addButton(layout, "Forgot Password Page", ForgotPassword.class);
        addButton(layout, "Forgot Password Check Page", forgotpasswordcheck.class);
        addButton(layout, "Forgot Password Continued Page", ForgotPasswordContinued.class);

        // Sprint 2 pages
        addButton(layout, "Home Page", Home.class);
        addButton(layout, "Workouts Page", Workouts.class);
        addButton(layout, "Settings Page", settings.class);
    }

    private void addButton(LinearLayout layout, String text, final Class<?> cls) {
        Button button = new Button(this);
        button.setText(text);
        layout.addView(button);

        // Set the click listener to navigate to the corresponding page
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SitemapActivity.this, cls));
            }
        });
    }
}