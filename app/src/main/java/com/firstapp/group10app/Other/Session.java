package com.firstapp.group10app.Other;

/**
 * The Session class stores valuable information about the user's session.
 */
public class Session {
    private final String userEmail;

    public Session(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserEmail() {
        return userEmail;
    }
}
