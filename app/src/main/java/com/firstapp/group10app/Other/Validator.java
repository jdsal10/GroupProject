package com.firstapp.group10app.Other;

import java.time.Year;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The Validator class contains methods to validate the user's input upon registration.
 */
public class Validator {
    private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*" +
            "@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private static final String DOB_PATTERN = "^([0-9]{4})-([0-9]{2})-([0-9]{2})$";
    private static final int PASSWORD_LENGTH = 6;

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
        else if (password.length() < PASSWORD_LENGTH)
            return "Password must be at least " + PASSWORD_LENGTH + " characters long!";
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

        if (!matcher.matches()) {
            return "Date of birth is invalid!";
        }

        String[] date = dob.split("-");
        int year = Integer.parseInt(date[0]);
        int month = Integer.parseInt(date[1]);
        int day = Integer.parseInt(date[2]);

        if (!yearValid(year)) {
            return "Year is invalid!";
        } else if (!monthValid(month)) {
            return "Month is invalid!";
        } else if (!dayValid(day, month, year)) {
            return "Day is invalid!";
        } else return null;
    }

    private static boolean yearValid(int year) {
        return year >= 1900 && year <= Year.now().getValue();
    }

    private static boolean monthValid(int month) {
        return month >= 1 && month <= 12;
    }

    private static boolean dayValid(int day, int month, int year) {
        if (day < 1 || day > 31) {
            return false;
        } else if (month == 2) {
            if (isLeapYear(year)) return day <= 29;
            else return day <= 28;
        } else if (month == 4 || month == 6 || month == 9 || month == 11) {
            return day <= 30;
        } else return true;
    }

    private static boolean isLeapYear(int year) {
        return (year % 4 == 0 && year % 100 != 0) || year % 400 == 0;
    }


    public static boolean dobValid(String dob) {
        return dobValidator(dob) == null;
    }

    public static String weightValidator(String weightString, String units) {
        if (weightString == null || weightString.length() == 0) return null;

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
            } else if (units.equals("inch")) {
                if (height < 20) return "Height must be more than 20in!";
                else if (height > 100) return "Height must be less than 100in!";
                else return null;
            } else return "Units are invalid!";
        } catch (NumberFormatException e) {
            return "Height is invalid!";
        }
    }

    public static boolean heightValid(String height, String units) {
        return heightValidator(height, units) == null;
    }
}