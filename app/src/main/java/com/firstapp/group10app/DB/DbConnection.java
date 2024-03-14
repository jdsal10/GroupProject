package com.firstapp.group10app.DB;

import android.os.StrictMode;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class DbConnection {
    public static Connection conn;
    public static Statement st;

    //Used to initialise a connection to the database
    public DbConnection() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try {
            Log.i("DBConnection", "Initialising connection");

            String connectionString = "jdbc:mysql://gateway01.eu-central-1.prod.aws.tidbcloud.com:4000/test?user=2xn9WQ6ma8aHYPp.root&password=6Tzop9pIbbE6dCbk&sslMode=VERIFY_IDENTITY&enabledTLSProtocols=TLSv1.2,TLSv1.3";
            conn = DriverManager.getConnection(connectionString);

            if (conn == null) {
                Log.w("DBConnection", "Attention, DBConnection.conn is null");
            }

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
    public ResultSet executeQuery(String statement) {
        try {
            Log.i("DBConnection.executeQuery", "Executing " + statement);
            return st.executeQuery(statement);
        } catch (Exception e) {
            Log.e("Error in DBConnection.executeQuery", e.toString());
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