package com.firstapp.group10app.DB.OnlineDb;

import android.os.StrictMode;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;

public class OnlineDbConnection {
    private static OnlineDbConnection instance;
    private static Connection connection;
    private static final String CONNECTION_STRING = "jdbc:mysql://gateway01.eu-central-1.prod.aws.tidbcloud.com:4000/test?user=2xn9WQ6ma8aHYPp.root&password=6Tzop9pIbbE6dCbk&sslMode=VERIFY_IDENTITY&enabledTLSProtocols=TLSv1.2,TLSv1.3";

    /**
     * Constructor for the DBConnection class.
     * Used to initialise a connection to the database.
     */
    public OnlineDbConnection() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try {
            Log.i("DBConnection", "Initialising connection");

            setConnection(DriverManager.getConnection(CONNECTION_STRING));

            if (getConnection() == null) {
                Log.w("DBConnection", "Attention, DBConnection.conn is null");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static synchronized OnlineDbConnection getInstance() {
        if (instance == null) {
            instance = new OnlineDbConnection();
        }
        return instance;
    }

    protected static Connection getConnection() {
        return connection;
    }

    private static void setConnection(Connection connection) {
        OnlineDbConnection.connection = connection;
    }

    /**
     * Executes a query that returns no data
     */
    protected void executeStatement(String createStatement) {
        try {
            getConnection().createStatement().execute(createStatement);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Executes a query that returns no data
     */
    protected ResultSet executeQuery(String query) {
        try {
            Log.i("DBConnection.executeQuery", "Executing " + query);
            return getConnection().createStatement().executeQuery(query);
        } catch (Exception e) {
            Log.e("Error in DBConnection.executeQuery", e.toString());
            throw new RuntimeException(e);
        }
    }

    /**
     * Tests the connection to the database
     *
     * @return true if the connection is valid, false otherwise
     */
    public static boolean testConnection() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        try {
            String connectionString = "jdbc:mysql://gateway01.eu-central-1.prod.aws.tidbcloud.com:4000/test?user=2xn9WQ6ma8aHYPp.root&password=6Tzop9pIbbE6dCbk&sslMode=VERIFY_IDENTITY&enabledTLSProtocols=TLSv1.2,TLSv1.3";
            setConnection(DriverManager.getConnection(connectionString));
            return getConnection().isValid(1);
        } catch (Exception e) {
            return false;
        }
    }

    public void closeConnection() {
        try {
            getConnection().close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}