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


import com.firstapp.group10app.R;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
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
        connect();
    }


    public Connection connect() {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection conn = null;
        Log Log = null;
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            @SuppressLint("AuthLeak") String connectionString = "jdbc:mysql://gateway01.eu-central-1.prod.aws.tidbcloud.com:4000/test?user=4JhVNGoguqAHant.root&password=RRdHMohyyNYd0Trx&sslMode=VERIFY_IDENTITY&enabledTLSProtocols=TLSv1.2,TLSv1.3";
            conn = DriverManager.getConnection(connectionString);

            Statement st = conn.createStatement();
            String dropTableSQL = "DROP TABLE IF EXISTS TestTable";
            st.execute(dropTableSQL);

            System.out.println("Table dropped successfully.");

            st.execute("CREATE TABLE TestTable (" +
                    "    ID INT PRIMARY KEY," +
                    "    Name VARCHAR(255)," +
                    "    Age INT," +
                    "    Email VARCHAR(255)" +
                    ");");
            System.out.println("Table created successfully.");
            st.close();
            conn.close();
        } catch (Exception e) {
            Log.e("Connection", "Error connecting to the database: " + e.getMessage());
            throw new RuntimeException(e);
        }
        return conn;
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