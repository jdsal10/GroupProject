package com.firstapp.group10app.Other;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.firstapp.group10app.DB.DatabaseManager;
import com.firstapp.group10app.DB.Exercise;
import com.firstapp.group10app.Pages.ActivityContainer;
import com.firstapp.group10app.Pages.MainActivity;

import org.json.JSONObject;


/**
 * The Session class stores valuable information about the user's session.
 */
public class Session {
    private static String userEmail;
    private static boolean onlineDbStatus;
    private static boolean internetStatus; // is connected to the internet? true or false
    private static JSONObject selectedWorkout; // It looks like it should be "selectedExercise" instead of "selectedWorkout"
    private static Exercise selectedExercise;
    private static boolean signedIn; // is the user signed in? true or false (if false, the user is anonymous)
    private static int workoutID;
    private static AppCompatActivity container;

    // Format for userDetails is [DOB, Weight, Height, Sex, Health Condition, Reason for downloading]
    private static String[] userDetails;


    public Session(String userEmail) {
        Session.setUserEmail(userEmail);
    }

    public static String getUserEmail() {
        return userEmail;
    }

    public static void setUserEmail(String userEmail) {
        Session.userEmail = userEmail;
    }

    public static boolean getOnlineDbStatus() {
        return onlineDbStatus;
    }

    public static void setOnlineDbStatus(boolean onlineDbStatus) {
        Session.onlineDbStatus = onlineDbStatus;
    }

    public static boolean getInternetStatus() {
        return internetStatus;
    }

    public static void setInternetStatus(boolean internetStatus) {
        Session.internetStatus = internetStatus;
    }

    public static JSONObject getSelectedWorkout() {
        return selectedWorkout;
    }

    @Nullable
    public static void setSelectedWorkout(JSONObject selectedWorkout) {
        Session.selectedWorkout = selectedWorkout;
    }

    @Nullable
    public static void setSelectedExercise(Exercise selectedExercise) {
        Session.selectedExercise = selectedExercise;
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

    @Nullable
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

    public static void logout(Context context) {
        clearSessionData();

        // Navigate back to MainActivity
        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static void logout(Context context, String message) {
        clearSessionData();

        // Navigate back to MainActivity
        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);

        // If context is an Activity, finish all previous activities
        if (context instanceof Activity) {
            ((Activity) context).finishAffinity();
        }

        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    private static void clearSessionData() {
        // Clear session data
        userEmail = null;
        onlineDbStatus = false;
        selectedWorkout = null;
        signedIn = false;
        workoutID = 0;
        container = null;
        userDetails = null;

        // Clear the ActivityContainer's current view
        ActivityContainer.currentView = 1;

        // Close the localDb connection
        if (DatabaseManager.getInstance().getLocalDbIfConnected() != null) {
            DatabaseManager.getInstance().getLocalDbIfConnected().close();
            DatabaseManager.getInstance().setLocalDb(null);
        }

        // Close the onlineDb connection
        if (DatabaseManager.getInstance().getOnlineDbIfConnected() != null) {
            DatabaseManager.getInstance().getOnlineDbIfConnected().closeConnection();
            DatabaseManager.getInstance().setOnlineDb(null);
        }

        DatabaseManager.setInstance(null);
    }
}