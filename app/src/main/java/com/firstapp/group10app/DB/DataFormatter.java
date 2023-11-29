package com.firstapp.group10app.DB;

import com.firstapp.group10app.Other.Index;

import java.util.ArrayList;

/**
 * DB.DataFormatter contains methods to format the data before passing it to the database.
 */
public class DataFormatter {
    public static String[] preCheckFormatUserDetails(String[] userDetails) {
        // Format the user details before passing them to the DataChecker
//        userDetails[Index.NAME] = formatName(userDetails[Index.NAME]);
//        userDetails[Index.DOB] = formatDOB(userDetails[Index.DOB]);
        userDetails[Index.WEIGHT] = preCheckFormatWeight(userDetails[Index.WEIGHT]);
        userDetails[Index.HEIGHT] = preCheckFormatHeight(userDetails[Index.HEIGHT]);

        return userDetails;
    }

    public static String[] formatUserDetails(String[] userDetails) {
        ArrayList<String> formattedDetails = new ArrayList<>();

        formattedDetails.add(userDetails[Index.EMAIL]);
        formattedDetails.add(formatName(userDetails[Index.NAME]));
        formattedDetails.add(userDetails[Index.PASSWORD]);

        // TODO: Implement the following methods
        formattedDetails.add(formatDOB(userDetails[Index.DOB]));
        formattedDetails.add(userDetails[Index.HEIGHT]);
        formattedDetails.add(userDetails[Index.WEIGHT]);
        formattedDetails.add(userDetails[Index.SEX]);
        formattedDetails.add(userDetails[Index.CONDITIONS]);
        formattedDetails.add(userDetails[Index.REASONS]);

        return formattedDetails.toArray(new String[0]);
    }

    /**
     * Formats jOhn dOe -> John Doe
     * Formats "" -> null
     */
    public static String formatName(String name) {
        if (name.equals("")) {
            return null;
        }

        // Capitalise the first letter of each word in the name
        String[] nameArray = name.split(" ");
        StringBuilder formattedName = new StringBuilder();
        for (String word : nameArray) {
            formattedName.append(word.substring(0, 1).toUpperCase());
            formattedName.append(word.substring(1).toLowerCase());
            formattedName.append(" ");
        }

        return formattedName.toString().trim();
    }

    /**
     * Formats 01-01-2000 -> 2000-01-01
     * Formats "" -> null
     */
    public static String formatDOB(String dob) {
        if (dob.equals("")) {
            return null;
        }

        // Format the date of birth
        String[] dobArray = dob.split("-");
        StringBuilder formattedDOB = new StringBuilder();
        formattedDOB.append(dobArray[2]);
        formattedDOB.append("-");
        formattedDOB.append(dobArray[1]);
        formattedDOB.append("-");
        formattedDOB.append(dobArray[0]);

        return formattedDOB.toString();
    }

    /**
     * Converts 100 kg -> 100
     * Converts 100 lbs -> 45.3592
     * Converts "" -> null
     */
    public static String preCheckFormatWeight(String weight) {
        if (weight.equals("")) {
            return null;
        }

        String units;

        String[] weightArray = weight.split(" ");
        if (weightArray.length != 2) {
            return null; // That should not be reachable
        } else {
            weight = weightArray[0];
            units = weightArray[1];
        }

        // Convert the weight to kg
        if (units.equals("kg")) {
            return weight;
        } else if (units.equals("lbs")) {
            return String.valueOf(Double.parseDouble(weight) / 2.20462);
        } else {
            throw new RuntimeException("Invalid units!");
        }
    }

    /**
     * Converts 100 cm -> 100
     * Converts 100 inch -> 254
     * Converts "" -> null
     */
    public static String preCheckFormatHeight(String height) {
        if (height.equals("")) {
            return null;
        }

        String units;

        String[] heightArray = height.split(" ");
        if (heightArray.length != 2) {
            return null; // That should not be reachable
        } else {
            height = heightArray[0];
            units = heightArray[1];
        }

        // Convert the height to cm
        if (units.equals("cm")) {
            return height;
        } else if (units.equals("inch")) {
            return String.valueOf(Double.parseDouble(height) * 2.54);
        } else {
            throw new RuntimeException("Invalid units!");
        }
    }
}
