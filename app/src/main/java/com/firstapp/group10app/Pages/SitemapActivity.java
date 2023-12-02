package com.firstapp.group10app.Pages;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.firstapp.group10app.ChatGPT.ChatGPT_Client;
import com.firstapp.group10app.ChatGPT.chatGPT_API;
import com.firstapp.group10app.R;

public class SitemapActivity extends AppCompatActivity {
    private LinearLayout layout;
    private boolean chatGPT_switch1 = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sitemap);

        layout = findViewById(R.id.sitemap_layout);

        addText("Sitemap");

        separateElements();

        // Sprint 1 pages
        addText("Sprint 1 Pages");
        addButton("MainActivity", MainActivity.class);
        addButton("Registration Page", Registration.class);
        addButton("Login Page", Login.class);
        addButton("Forgot Password Page", ForgotPassword.class);
        addButton("Forgot Password Check Page", forgotpasswordcheck.class);
        addButton("Forgot Password Continued Page", ForgotPasswordContinued.class);

        separateElements();

        // Sprint 2 pages
        addText("Sprint 2 Pages");
        addButton("Home Page", Home.class);
        addButton("Workouts Page", Workouts.class);
        addButton("Settings Page", settings.class);

        separateElements();

        if (chatGPT_switch1 && chatGPT_switch2) {
            addText("ChatGPT");
            addChatGPT("ChatGPT_Client");
            addChatGPT("ChatGPT_API");
        } else {
            addText("ChatGPT functionality is disabled to prevent loss of credits.");
        }
    }

    private void addText(String text) {
        TextView textView = new TextView(this);
        textView.setText(text);
        layout.addView(textView);
    }

    private void addButton(String text, final Class<?> cls) {
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

    private void addChatGPT(String text) {
        Button button = new Button(this);
        button.setText(text);
        layout.addView(button);

        // Set the click listener to navigate to the corresponding page
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (text.equals("ChatGPT_Client"))
                        ChatGPT_Client.chatGPT("Hello");
                    else if (text.equals("ChatGPT_API"))
                        chatGPT_API.chatGPT("Hello");
                    else
                        throw new Exception("Invalid ChatGPT function");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void separateElements() {
        View view = new View(this);
        view.setMinimumHeight(20);
        layout.addView(view);
    }

    private boolean chatGPT_switch2 = false;
}