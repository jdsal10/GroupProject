package com.firstapp.group10app.Other;

/**
 * The Session class stores valuable information about the user's session.
 */
public class Session {
    public static String userEmail;

    public static boolean dbStatus;

    public Session(String userEmail) {
        Session.userEmail = userEmail;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public boolean getDBStatus() {return dbStatus;}
}
