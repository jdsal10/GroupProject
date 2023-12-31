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

import java.sql.ResultSet;
import java.sql.SQLException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button goToLoginButton = findViewById(R.id.goToLogin);
        goToLoginButton.setOnClickListener(this);

        TextView skipText = findViewById(R.id.skipToHome);
        skipText.setOnClickListener(this);

        // Temporary button to navigate to the sitemap
        Button goToSitemapButton = findViewById(R.id.goToSitemap);
        goToSitemapButton.setOnClickListener(this);

        // TEMP CODE TO CHECK CONNECTION
        Session.dbStatus = DBConnection.testConnection();
        test();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.goToLogin) {
            startActivity(new Intent(MainActivity.this, Login.class));
            overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
        } else if (id == R.id.skipToHome) {
            Session.userEmail = null;
            startActivity(new Intent(MainActivity.this, Home.class));
        } else if (id == R.id.goToSitemap) {
            startActivity(new Intent(MainActivity.this, SitemapActivity.class));
        }
    }

    public void test() {
        DBConnection d = new DBConnection();
        ResultSet out = d.executeQuery("SELECT\n" +
                "  JSON_ARRAYAGG(\n" +
                "    JSON_OBJECT(\n" +
                "      'WorkoutID', w.WorkoutID,\n" +
                "      'WorkoutName', w.WorkoutName,\n" +
                "      'WorkoutDuration', w.WorkoutDuration,\n" +
                "      'TargetMuscleGroup', w.TargetMuscleGroup,\n" +
                "      'Equipment', w.Equipment,\n" +
                "      'Difficulty', w.Difficulty,\n" +
                "      'Exercises', (\n" +
                "        SELECT JSON_ARRAYAGG(\n" +
                "          JSON_OBJECT(\n" +
                "            'ExerciseID', e.ExerciseID,\n" +
                "            'ExerciseName', e.ExerciseName,\n" +
                "            'Description', e.Description,\n" +
                "            'Illustration', e.Illustration,\n" +
                "            'TargetMuscleGroup', e.TargetMuscleGroup,\n" +
                "            'Equipment', e.Equipment,\n" +
                "            'Difficulty', e.Difficulty\n" +
                "          )\n" +
                "        )\n" +
                "        FROM HealthData.ExerciseWorkoutPairs ewp\n" +
                "        JOIN HealthData.Exercises e ON ewp.ExerciseID = e.ExerciseID\n" +
                "        WHERE ewp.WorkoutID = w.WorkoutID\n" +
                "      )\n" +
                "    )\n" +
                "  ) AS Result\n" +
                "FROM\n" +
                "  HealthData.Workouts w;\n");

        while (true) {
            try {
                if (!out.next()) break;
                for (int i = 1; i <= out.getMetaData().getColumnCount(); i++) {
                    System.out.print(out.getString(i) + "\t");
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            System.out.println();
        }
    }
}