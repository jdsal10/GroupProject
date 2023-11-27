package com.firstapp.group10app.DB;

import java.sql.ResultSet;

public class DBHelper {
    public static void insertUser(String[] userDetails) {
        try {
            // Check that the user details are valid
            DataChecker.checkUserDetails(userDetails);

            // Format the user details
            // TODO: Finish DataFormatter
            userDetails = DataFormatter.formatUserDetails(userDetails);

            // Create and SQL query
            StringBuilder sql = new StringBuilder();
            sql.append("INSERT INTO HealthData.Users (Email, PreferredName, Password, DOB, Weight, Height, Sex, HealthCondition, ReasonForDownloading) VALUES (");
            for (String field : userDetails) {
                sql.append("'");
                sql.append(field);
                sql.append("', ");
            }
            sql.deleteCharAt(sql.length() - 2);
            sql.append(");");

            // Execute the SQL query
            DBConnection db = new DBConnection();
            db.executeStatement(sql.toString());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static ResultSet getUser(String email) {
        try {
            // Create and SQL query
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT * FROM HealthData.Users WHERE Email = '");
            sql.append(email);
            sql.append("';");

            // Execute the SQL query
            DBConnection db = new DBConnection();
            return db.executeQuery(sql.toString());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}