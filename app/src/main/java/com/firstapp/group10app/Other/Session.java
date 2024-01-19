package com.firstapp.group10app.Other;

import org.json.JSONObject;

/**
 * The Session class stores valuable information about the user's session.
 */
public class Session {
    public static String userEmail;

    public static boolean dbStatus;

    public static JSONObject selectedWorkout;

    public static Boolean signedIn;

    public Session(String userEmail) {
        Session.userEmail = userEmail;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public boolean getDBStatus() {return dbStatus;}

    public JSONObject getSelectedWorkout() {return selectedWorkout;}

    public boolean isSignedIn() {return signedIn;}
}
