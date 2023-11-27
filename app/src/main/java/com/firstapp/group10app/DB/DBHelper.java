package com.firstapp.group10app.DB;

public class DBHelper {
    public void insertUser(String[] userDetails) {
        try {
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
}