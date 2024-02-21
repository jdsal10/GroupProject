package com.firstapp.group10app.Pages;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
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

        // Temporary button to navigate to the sitemap
        Button goToSitemapButton = findViewById(R.id.goToSitemap);
        goToSitemapButton.setOnClickListener(this);

        // Test database connection
        Session.dbStatus = DBConnection.testConnection();

        // Default value.
        Session.signedIn = false;
        
        // Used to add test data

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
        } else if (id == R.id.anonymous) {
            Session.userEmail = null;
            Session.signedIn = false;
            startActivity(new Intent(MainActivity.this, Home.class));
        } else if (id == R.id.goToSitemap) {
            startActivity(new Intent(MainActivity.this, SitemapActivity.class));
        } else if (id == R.id.goToRegister) {
            startActivity(new Intent(MainActivity.this, Registration.class));
            overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
        }
    }

//    public void addData() throws JSONException {
//        String data = "{" +
//                "\"WorkoutName\": \"Abs Blast\"," +
//                "\"WorkoutDuration\": 35," +
//                "\"TargetMuscleGroup\": \"Abs\"," +
//                "\"Equipment\": \"None\"," +
//                "\"Difficulty\": \"Easy\"," +
//                "\"exercises\": [" +
//                "{" +
//                "\"ExerciseName\": \"Crunches\"," +
//                "\"Description\": \"Lie on your back with knees bent and hands behind your head. Lift your shoulders off the floor using your abdominal muscles, then lower back down.\"," +
//                "\"TargetMuscleGroup\": \"Abs\"," +
//                "\"Equipment\": \"None\"," +
//                "\"Difficulty\": \"Easy\"" +
//                "}," +
//                "{" +
//                "\"ExerciseName\": \"Leg Raises\"," +
//                "\"Description\": \"Lie flat on your back and lift your legs upward, keeping them straight until they form a 90-degree angle with the floor, then lower them back down slowly.\"," +
//                "\"TargetMuscleGroup\": \"Lower Abs\"," +
//                "\"Equipment\": \"None\"," +
//                "\"Difficulty\": \"Easy\"" +
//                "}," +
//                "{" +
//                "\"ExerciseName\": \"Bicycle Crunches\"," +
//                "\"Description\": \"Lie on your back with knees bent and hands behind your head. Bring one knee towards your chest while simultaneously twisting through your core to bring the opposite elbow towards the knee. Alternate sides in a pedaling motion.\"," +
//                "\"TargetMuscleGroup\": \"Obliques\"," +
//                "\"Equipment\": \"None\"," +
//                "\"Difficulty\": \"Easy\"" +
//                "}," +
//                "{" +
//                "\"ExerciseName\": \"Plank\"," +
//                "\"Description\": \"Assume a push-up position, but with your weight on your forearms instead of your hands. Keep your body in a straight line from head to heels, engaging your core muscles.\"," +
//                "\"TargetMuscleGroup\": \"Core\"," +
//                "\"Equipment\": \"None\"," +
//                "\"Difficulty\": \"Easy\"" +
//                "}" +
//                "]" +
//                "}";
//
//        JSONToDB.insertWorkout(data);
//
//    }
}