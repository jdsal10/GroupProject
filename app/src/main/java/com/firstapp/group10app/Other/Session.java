package com.firstapp.group10app.Other;

import android.database.sqlite.SQLiteDatabase;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;


/**
 * The Session class stores valuable information about the user's session.
 */
public class Session {
    private static String userEmail;
    private static boolean dbStatus;
    private static JSONObject selectedWorkout;
    private static Boolean signedIn;
    private static int workoutID;

    private static AppCompatActivity container;

    // Format for userDetails is [DOB, Weight, Height, Sex, Health Condition, Reason for downloading]
    private static String[] userDetails;
    private static SQLiteDatabase localDB;


    public Session(String userEmail) {
        Session.setUserEmail(userEmail);
    }

    public static String getUserEmail() {
        return userEmail;
    }

    public static void setUserEmail(String userEmail) {
        Session.userEmail = userEmail;
    }

    public static boolean isDbStatus() {
        return dbStatus;
    }

    public static void setDbStatus(boolean dbStatus) {
        Session.dbStatus = dbStatus;
    }

    public static JSONObject getSelectedWorkout() {
        return selectedWorkout;
    }

    public static void setSelectedWorkout(JSONObject selectedWorkout) {
        Session.selectedWorkout = selectedWorkout;
    }

    public static Boolean getSignedIn() {
        return signedIn;
    }

    public static void setSignedIn(Boolean signedIn) {
        Session.signedIn = signedIn;
    }

    public static int getWorkoutID() {
        return workoutID;
    }

    public static void setWorkoutID(int workoutID) {
        Session.workoutID = workoutID;
    }

    public static AppCompatActivity getContainer() {
        return container;
    }

    public static void setContainer(AppCompatActivity container) {
        Session.container = container;
    }

    public static String[] getUserDetails() {
        return userDetails;
    }

    public static void setUserDetails(String[] userDetails) {
        Session.userDetails = userDetails;
    }

    public static SQLiteDatabase getLocalDB() {
        return localDB;
    }

    public static void setLocalDB(SQLiteDatabase localDB) {
        Session.localDB = localDB;
    }
}