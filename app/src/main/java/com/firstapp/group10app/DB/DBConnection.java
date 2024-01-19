package com.firstapp.group10app.DB;

import android.os.StrictMode;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class DBConnection {
    public static Connection conn;
    public static Statement st;

    //Used to initialise a connection to the database
    public DBConnection() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try {
            String connectionString = "jdbc:mysql://gateway01.eu-central-1.prod.aws.tidbcloud.com:4000/test?user=2xn9WQ6ma8aHYPp.root&password=6Tzop9pIbbE6dCbk&sslMode=VERIFY_IDENTITY&enabledTLSProtocols=TLSv1.2,TLSv1.3";
            conn = DriverManager.getConnection(connectionString);
            st = conn.createStatement();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //Executes a query that returns no data
    public static void executeStatement(String createStatement) {
        try {
            st.execute(createStatement);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //Executes a query that returns a ResultSet
    public static ResultSet executeQuery(String statement) {
        try {
            return st.executeQuery(statement);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean testConnection() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try {
            String connectionString = "jdbc:mysql://gateway01.eu-central-1.prod.aws.tidbcloud.com:4000/test?user=2xn9WQ6ma8aHYPp.root&password=6Tzop9pIbbE6dCbk&sslMode=VERIFY_IDENTITY&enabledTLSProtocols=TLSv1.2,TLSv1.3";
            conn = DriverManager.getConnection(connectionString);
            return conn.isValid(1);
        } catch (Exception e) {
            return false;

        }
    }
}