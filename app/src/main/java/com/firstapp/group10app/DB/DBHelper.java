package com.firstapp.group10app.DB;

import com.firstapp.group10app.Other.Index;

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

//            System.out.println(sql);
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

    public static boolean checkExists(String email) throws SQLException {
        return DBHelper.getUser(email).next();
    }
}