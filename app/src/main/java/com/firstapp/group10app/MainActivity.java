package com.firstapp.group10app;

import androidx.appcompat.app.AppCompatActivity;
import android.view.*;

import android.os.Bundle;
import android.widget.Button;
import android.content.Intent;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String DATABASE_NAME = "healthdata";
    public static final String url = "jdbc:mysql://healthdata.cgzabjirm4kt.eu-west-2.rds.amazonaws.com:3306/" + DATABASE_NAME;
    public static final String username = "healthadmin", password = "Comp6000health";
//    public static final String

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
//        new Thread(() -> {
//            try {
//                //Seems the code below is the issue, no print statements occur.
//                Class.forName("com.mysql.jdbc.Driver");
//                Connection connection = DriverManager.getConnection(url, username, password);
//                System.out.println("Connected");
//                Statement statement = connection.createStatement();
//                System.out.println("Creating");
//        statement.execute("CREATE TABLE test");
//        connection.close();
//        System.out.println("SUCCESS");
//    } catch (Exception e) {
//        throw new RuntimeException(e);
//    }
//    }).start();
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
            //Add form of identifier for the rest of the code to function.
        }
    }
}