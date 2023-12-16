package com.firstapp.group10app.Other;

/**
 * The Session class stores valuable information about the user's session.
 */
public class Session {
    public static String userEmail;

    public Session(String userEmail) {
        Session.userEmail = userEmail;
    }

    public String getUserEmail() {
        return userEmail;
    }
}
