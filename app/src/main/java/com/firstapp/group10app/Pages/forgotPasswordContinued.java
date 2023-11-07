package com.firstapp.group10app.Pages;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.firstapp.group10app.R;

public class forgotPasswordContinued extends AppCompatActivity {

    private Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password_continued);


        Uri uri = getIntent().getData();
        if (uri != null) {
            String host = uri.getHost();
            String path = uri.getPath();
            System.out.println(host + " " + path);

            // Handle the deep link based on the host and path
        }
        // ATTENTION: This was auto-generated to handle app links.
        Intent appLinkIntent = getIntent();
        String appLinkAction = appLinkIntent.getAction();
        Uri appLinkData = appLinkIntent.getData();
    }
}