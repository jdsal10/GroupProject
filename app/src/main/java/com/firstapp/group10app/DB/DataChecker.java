package com.firstapp.group10app.DB;

import com.firstapp.group10app.Other.Validator;

/**
 * DB.DataChecker contains the various tests that are used to check the validity of the input before
 * passing it to the database.
 */
public class DataChecker {
    public static boolean checkUserDetails(String[] userDetails) {
        // Check that the array is the correct length
        if (userDetails.length != 9) {
            return false;
        }

        // Check that the required fields pass their tests
        else if (!checkEmail(userDetails[0])) {
            return false;
        } else if (!checkPassword(userDetails[2])) {
            return false;
        }

        // return true if all tests pass
        else return true;
    }

    public static boolean checkEmail(String email) {
        if (email.length() < 5) {
            return false;
        } else if (!email.contains("@")) {
            return false;
        } else if (!email.contains(".")) {
            return false;
        }

        // Check that the email is not already in the database
        else {
            try {
                return DBHelper.getUser(email).next();
            } catch (Exception e) {
                return false;
            }
        }
    }

    public static boolean checkPassword(String password) {
        return Validator.passwordValidator(password) == null;
    }
}