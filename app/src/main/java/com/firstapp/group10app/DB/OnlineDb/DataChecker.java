package com.firstapp.group10app.DB.OnlineDb;

import android.util.Log;

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
            Log.w("DataChecker.checkUserDetails", "User details array is the wrong length");
            return false;
        }

        // Check that the required fields pass their tests
        else // return true if all tests pass
            if (!checkEmail(userDetails[Index.EMAIL])) {
                Log.d("DataChecker.checkUserDetails", "Email is invalid");
                return false;
            } else if (!checkPassword(userDetails[Index.PASSWORD])) {
                Log.d("DataChecker.checkUserDetails", "Password is invalid");
                return false;
            } else if (!checkDOB(userDetails[Index.DOB])) {
                Log.d("DataChecker.checkUserDetails", "DOB is invalid");
                return false;
            }
        return true;
    }

    public static boolean checkEmail(String email) {
        if (email.length() < 5) {
            Log.d("DataChecker.checkEmail", "Email is too short");
            return false;
        } else if (!email.contains("@")) {
            Log.d("DataChecker.checkEmail", "Email does not contain @");
            return false;
        } else if (!email.contains(".")) {
            Log.d("DataChecker.checkEmail", "Email does not contain .");
            return false;
        }

        // Check that the email is not already in the database
        else {
            try {
                Log.i("DataChecker.checkEmail", "Checking if email is already in the database");
                return !OnlineDbHelper.checkExists(email);
            } catch (Exception e) {
                return false;
            }
        }
    }

    public static boolean checkPassword(String password) {
        return Validator.passwordValidator(password) == null;
    }

    public static boolean checkDOB(String dob) {
        return Validator.dobValidator(dob) == null;
    }
}