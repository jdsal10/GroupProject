package com.firstapp.group10app.Pages;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.firstapp.group10app.Other.Session;
import com.firstapp.group10app.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button goToLoginButton;
    private final Handler handler = new Handler();
    private final Runnable internetCheckRunnable = new Runnable() {
        @Override
        public void run() {
            // Test internet connection
            Session.setInternetStatus(isInternetAvailable());

            // If the connection false, disable the login.
            if (!Session.getInternetStatus()) {
                goToLoginButton.setEnabled(false);
                goToLoginButton.setAlpha(.5f);
            } else {
                goToLoginButton.setEnabled(true);
                goToLoginButton.setAlpha(1f);
            }

            handler.postDelayed(this, 10000); // 10 seconds delay
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACTIVITY_RECOGNITION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACTIVITY_RECOGNITION}, 1);
        }

        View rootLayout = findViewById(android.R.id.content);

        AlphaAnimation fadeInAnimation = new AlphaAnimation(0.0f, 1.0f);
        fadeInAnimation.setDuration(1000);

        rootLayout.startAnimation(fadeInAnimation);
        goToLoginButton = findViewById(R.id.goToLogin);
        goToLoginButton.setOnClickListener(this);

        TextView goToRegisterButton = findViewById(R.id.goToRegister);
        goToRegisterButton.setOnClickListener(this);

        TextView skipText = findViewById(R.id.anonymous);
        skipText.setOnClickListener(this);

        // Test internet connection
        Session.setInternetStatus(isInternetAvailable());

        // Default value.
        Session.setSignedIn(false);

        String[] quotes = getResources().getStringArray(R.array.motivational_quotes);
        int randomQuoteIndex = (int) (Math.random() * quotes.length);
        TextView quote = findViewById(R.id.quote);
        quote.setText(quotes[randomQuoteIndex]);
        quote.setTypeface(null, Typeface.ITALIC);

        // Start the periodic internet check
        handler.post(internetCheckRunnable);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(internetCheckRunnable); // Stop the periodic internet check when activity is destroyed
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.goToLogin && Session.getInternetStatus()) {
            startActivity(new Intent(MainActivity.this, Login.class));
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        } else if (id == R.id.anonymous) {
            Session.setUserEmail(null);
            Session.setSignedIn(false);

            Intent intent = new Intent(getApplicationContext(), ActivityContainer.class);
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        } else if (id == R.id.goToRegister && Session.getInternetStatus()) {
            startActivity(new Intent(MainActivity.this, Registration.class));
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        }
    }

    public boolean isInternetAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }
}