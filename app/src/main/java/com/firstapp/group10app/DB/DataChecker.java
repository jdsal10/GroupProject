package com.firstapp.group10app.DB;

import com.firstapp.group10app.Other.Index;
import com.firstapp.group10app.Other.Validator;

/**
 * DB.DataChecker contains the various tests that are used to check the validity of the input before
 * passing it to the database.
 */
public class DataChecker {
    public static boolean checkUserDetails(String[] userDetails) {
        // Check that the array is the correct length
        if (userDetails.length != Index.USER_DETAILS.length) {
            System.out.println("User details array is the wrong length");
            return false;
        }

        // Check that the required fields pass their tests
        else // return true if all tests pass
            if (!checkEmail(userDetails[Index.EMAIL])) {
                System.out.println("Email is invalid");
                return false;
            } else if (!checkPassword(userDetails[Index.PASSWORD])) {
                System.out.println("Password is invalid");
                return false;
            } else if (!checkDOB(userDetails[Index.DOB])) {
                System.out.println("DOB is invalid");
                return false;
            } else {
                System.out.println("According to DataChecker.checkUserDetails: User details are valid");
                return true;
            }
    }

    public static boolean checkEmail(String email) {
        if (email.length() < 5) {
            System.out.println("Email is too short");
            return false;
        } else if (!email.contains("@")) {
            System.out.println("Email does not contain @");
            return false;
        } else if (!email.contains(".")) {
            System.out.println("Email does not contain .");
            return false;
        }

        // Check that the email is not already in the database
        else {
            return !DBHelper.checkUserExists(email);
        }
    }

    public static boolean checkPassword(String password) {
        return Validator.passwordValidator(password) == null;
    }

    public static boolean checkDOB(String dob) {
        return Validator.dobValidator(dob) == null;
    }
}