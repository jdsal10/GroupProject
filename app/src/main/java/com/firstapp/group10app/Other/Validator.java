package com.firstapp.group10app.Other;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validator {
    private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*" +
            "@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private static final String DOB_PATTERN = "^([0-9]{4})-([0-9]{2})-([0-9]{2})$";

    public static String emailValidator(String email) {
        if (email == null || email.length() == 0) return ("Email is required!");
        else {
            Pattern pattern = Pattern.compile(EMAIL_PATTERN);
            Matcher matcher = pattern.matcher(email);

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
        if (dob == null || dob.length() == 0) return null;

        Pattern pattern = Pattern.compile(DOB_PATTERN);
        Matcher matcher = pattern.matcher(dob);

        return matcher.matches() ? null : "Date of birth is invalid!";
    }

    public static boolean dobValid(String dob) {
        return dobValidator(dob) == null;
    }

    public static String weightValidator(String weightString, String units) {
        if (weightString == null) return null;

        try {
            double weight = Double.parseDouble(weightString);

            if (units.equals("kg")) {
                if (weight < 20) return "Weight must be more than 20kg!";
                else if (weight > 200) return "Weight must be less than 200kg!";
                else return null;
            } else if (units.equals("lbs")) {
                if (weight < 22) return "Weight must be more than 22lb!";
                else if (weight > 550) return "Weight must be less than 550lb!";
                else return null;
            } else return "Units are invalid!";
        } catch (NumberFormatException e) {
            return "Weight is invalid!";
        }
    }

    public static boolean weightValid(String weight, String units) {
        return weightValidator(weight, units) == null;
    }

    public static String heightValidator(String heightString, String units) {
        if (heightString == null || heightString.length() == 0) return null;

        try {
            double height = Double.parseDouble(heightString);

            if (units.equals("cm")) {
                if (height < 50) return "Height must be more than 50cm!";
                else if (height > 250) return "Height must be less than 250cm!";
                else return null;
            } else if (units.equals("ft")) {
                if (height < 1.5) return "Height must be more than 1.5ft!";
                else if (height > 8.2) return "Height must be less than 8.2ft!";
                else return null;
            } else return "Units are invalid!";
        } catch (NumberFormatException e) {
            return "Height is invalid!";
        }
    }

    public static boolean heightValid(String height, String units) {
        return heightValidator(height, units) == null;
    }

    public static String sexValidator(String sex) {
        if (sex == null || sex.length() == 0) return "Sex is required";
        else if (!sex.equals("M") && !sex.equals("F") && !sex.equals("O")) return "Sex is invalid!";
        else return null;
    }
}