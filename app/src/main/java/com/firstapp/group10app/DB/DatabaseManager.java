package com.firstapp.group10app.DB;

import android.content.Context;

import com.firstapp.group10app.DB.LocalDb.LocalDb;
import com.firstapp.group10app.DB.OnlineDb.OnlineDbConnection;
import com.firstapp.group10app.DB.OnlineDb.OnlineDbHelper;
import com.firstapp.group10app.Other.Session;

import java.util.List;

public class DatabaseManager {
    private static DatabaseManager instance;
    private LocalDb localDb;
    private OnlineDbConnection onlineDb;

    private DatabaseManager() {
    }

    public static synchronized DatabaseManager getInstance() {
        if (instance == null) instance = new DatabaseManager();
        return instance;
    }

    public LocalDb getLocalDb() {
        if (localDb == null) throw new UnsupportedOperationException("LocalDb is not connected");
        return localDb;
    }

    private OnlineDbConnection getOnlineDb() {
        if (onlineDb == null) connectToOnlineDb();
        return onlineDb;
    }

    public void connectToLocalDb(Context context) {
        localDb = new LocalDb(context);
    }

    private void connectToOnlineDb() {
        onlineDb = OnlineDbConnection.getInstance();
    }

    public List<Exercise> getAllExercises() {
        if (Session.getSignedIn()) {
            return OnlineDbHelper.getAllExercises();
        } else {
            return getLocalDb().getAllExercises();
        }
    }

    public String getWorkoutsAsJsonArray(String filter) {
        if (Session.getSignedIn()) {
            return OnlineDbHelper.getWorkoutsAsJsonArray(filter);
        } else {
            getLocalDb().printDataForDebugging();

//            return OnlineDbHelper.getWorkoutsAsJsonArray(filter);
            return getLocalDb().getWorkoutsAsJsonArray(filter);
        }
    }

    public void updateUserData(String toUpdate, String value) {
        try {
            if (Session.getSignedIn()) {
                // Call the updateUserData method from the OnlineDbHelper class
                OnlineDbHelper.updateUserData(toUpdate, value);
            } else {
                throw new UnsupportedOperationException("Cannot update user data while anonymous because there is no user");
            }
        } catch (UnsupportedOperationException e) {
            // TODO: In the future we might want to handle this exception differently
            e.printStackTrace();
        }
    }

    public void deleteUser(String email) {
        try {
            if (Session.getSignedIn()) {
                // Call the deleteUser method from the OnlineDbHelper class
                OnlineDbHelper.deleteUser(email);
            } else {
                throw new UnsupportedOperationException("Cannot delete user while anonymous because there is no user");
            }
        } catch (UnsupportedOperationException e) {
            // TODO: In the future we might want to handle this exception differently
            e.printStackTrace();
        }
    }
}