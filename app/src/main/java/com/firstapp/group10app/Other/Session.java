package com.firstapp.group10app.Other;

import androidx.appcompat.app.AppCompatActivity;

import com.firstapp.group10app.Pages.ActivityContainer;

import org.json.JSONObject;

/**
 * The Session class stores valuable information about the user's session.
 */
public class Session {
    public static String userEmail;
    public static boolean dbStatus;
    public static JSONObject selectedWorkout;
    public static Boolean signedIn;
    public static int workoutID;

    public static AppCompatActivity container;

    // Format for userDetails is [DOB, Weight, Height, Sex, Health Condition, Reason for downloading]
    public static String[] userDetails;

    public Session(String userEmail) {
        Session.userEmail = userEmail;
    }
}
