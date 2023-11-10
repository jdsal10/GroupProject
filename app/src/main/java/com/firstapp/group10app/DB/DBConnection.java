package com.firstapp.group10app.DB;

import android.os.StrictMode;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class DBConnection {
    public Connection connection;
    public Statement statement;

    public DBConnection() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try {
            String connectionString = "jdbc:mysql://gateway01.eu-central-1.prod.aws.tidbcloud.com:4000/test?user=2xn9WQ6ma8aHYPp.root&password=6Tzop9pIbbE6dCbk&sslMode=VERIFY_IDENTITY&enabledTLSProtocols=TLSv1.2,TLSv1.3";
            connection = DriverManager.getConnection(connectionString);
            statement = connection.createStatement();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void executeStatement(String createStatement) {
        try {
            statement.execute(createStatement);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public ResultSet executeQuery(String statement) {
        try {
            return this.statement.executeQuery(statement);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}