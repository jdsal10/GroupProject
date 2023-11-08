package com.firstapp.group10app.Other;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validator {

    public Validator() {
    }

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

    public static boolean emailValid(String email) {
        return emailValidator(email) == null;
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

    public static boolean passwordValid(String password) {
        return passwordValidator(password) == null;
    }

    public static String dobValidator(String dob) {
        if (dob == null || dob.length() == 0) return "Date of birth is required!";
        else {
            Pattern pattern;
            Matcher matcher;

            final String DOB_PATTERN = "^(0[1-9]|[12][0-9]|3[01])\\/(0[1-9]|1[012])\\/((19|20)\\d\\d)$";

            pattern = Pattern.compile(DOB_PATTERN);
            matcher = pattern.matcher(dob);

            return matcher.matches() ? null : "Date of birth is invalid!";
        }
    }

    public static boolean dobValid(String dob) {
        return dobValidator(dob) == null;
    }

    public static String weightValidator(String weight) {
        if (weight == null || weight.length() == 0) return "Weight is required!";
        else {
            Pattern pattern;
            Matcher matcher;

            final String WEIGHT_PATTERN = "^[0-9]+(\\.[0-9]{1,2})?$";

            pattern = Pattern.compile(WEIGHT_PATTERN);
            matcher = pattern.matcher(weight);

            return matcher.matches() ? null : "Weight is invalid!";
        }
    }

    public static boolean weightValid(String weight) {
        return weightValidator(weight) == null;
    }

    public static String heightValidator(String height) {
        if (height == null || height.length() == 0) return "Height is required!";
        else {
            Pattern pattern;
            Matcher matcher;

            final String HEIGHT_PATTERN = "^[0-9]+(\\.[0-9]{1,2})?$";

            pattern = Pattern.compile(HEIGHT_PATTERN);
            matcher = pattern.matcher(height);

            return matcher.matches() ? null : "Height is invalid!";
        }
    }

    public static boolean heightValid(String height) {
        return heightValidator(height) == null;
    }

    public static String sexValidator(String sex) {
        if (sex == null || sex.length() == 0) return "Sex is required";
        else if (!sex.equals("M") && !sex.equals("F") && !sex.equals("O")) return "Sex is invalid!";
        else return null;
    }
}