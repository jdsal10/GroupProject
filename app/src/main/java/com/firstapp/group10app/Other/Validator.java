package com.firstapp.group10app.Other;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validator {
    public static String emailValidator(String email) {
        if (email == null || email.length() == 0) return ("Email is required!");
        else {
            Pattern pattern;
            Matcher matcher;

            final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

            pattern = Pattern.compile(EMAIL_PATTERN);
            matcher = pattern.matcher(email);

            return matcher.matches() ? null : "Email is invalid!";
        }
    }

    public static String passwordValidator(String password) {
        if (password == null || password.length() == 0) return "Password is required!";
        else if (password.length() < 8) return "Password must be at least 8 characters long!";
        else if (!password.matches(".*[0-9].*"))
            return "Password must contain at least one number!";
        else if (!password.matches(".*[A-Z].*"))
            return "Password must contain at least one capital letter!";
        else if (!password.matches(".*[a-z].*"))
            return "Password must contain at least one lowercase letter!";
        else if (!password.matches(".*[!@#$%^&*+=?-].*"))
            return "Password must contain at least one special character!";
        else return null;
    }
}
