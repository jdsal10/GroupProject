package com.firstapp.group10app.DB;

import com.firstapp.group10app.Other.Index;
import com.firstapp.group10app.Other.Session;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DBHelper {
    public static void insertUser(String[] userDetails) {
        try {
            // Format the user details before passing them to the DataChecker
            DataFormatter.preCheckFormatUserDetails(userDetails);

            // Check that the user details are valid
            if (!DataChecker.checkUserDetails(userDetails)) {
                throw new IllegalArgumentException("Invalid user details");
            }

            // Format the user details (post-check)
            userDetails = DataFormatter.formatUserDetails(userDetails);

            // Create and SQL query
            StringBuilder sql = new StringBuilder();
            sql.append("INSERT INTO HealthData.Users (");
            for (int i = 0; i < userDetails.length; i++) {
                if (userDetails[i] != null) {
                    sql.append(Index.USER_DETAILS[i]);
                    sql.append(", ");
                }
            }
            sql.deleteCharAt(sql.length() - 2);
            sql.append(") VALUES (");

            for (String field : userDetails) {
                sql.append("'");
                sql.append(field);
                sql.append("', ");
            }
            sql.deleteCharAt(sql.length() - 2);
            sql.append(");");

            // Execute the SQL query
            DBConnection.executeStatement(sql.toString());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static ResultSet getUser(String email) {
        try {
            // Create and SQL query
            String sql = "SELECT * FROM HealthData.Users WHERE Email = '" +
                    email +
                    "';";

            // Execute the SQL query
            return DBConnection.executeQuery(sql);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // Returns true if the user exists in the database
    public static boolean checkExists(String email) throws SQLException {
        return getUser(email).next();
    }

    public static void clearData(String toDelete) {
        DBConnection.executeStatement("UPDATE HealthData.Users SET " + toDelete + " = NULL WHERE Email = '" + Session.userEmail + "'");
    }

    public static void updateData(String toUpdate, String value) {
        DBConnection.executeStatement("UPDATE HealthData.Users SET " + toUpdate + " = '" + value + "' WHERE Email = '" + Session.userEmail + "'");
    }
}