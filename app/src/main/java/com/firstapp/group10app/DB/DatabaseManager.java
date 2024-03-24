package com.firstapp.group10app.DB;

import android.content.Context;
import android.util.Log;

import com.firstapp.group10app.DB.LocalDb.LocalDbConnection;
import com.firstapp.group10app.DB.OnlineDb.OnlineDbConnection;
import com.firstapp.group10app.DB.OnlineDb.OnlineDbHelper;
import com.firstapp.group10app.Other.Session;

import java.util.List;

public class DatabaseManager {
    private static DatabaseManager instance;
    private LocalDbConnection localDb;
    private OnlineDbConnection onlineDb;

    private DatabaseManager() {
    }

    public static synchronized DatabaseManager getInstance() {
        if (instance == null) instance = new DatabaseManager();
        return instance;
    }

    public static void setInstance(DatabaseManager newInstance) {
        instance = newInstance;
    }

    public LocalDbConnection getLocalDb() {
        if (localDb == null) throw new UnsupportedOperationException("LocalDb is not connected");
        return localDb;
    }

    public LocalDbConnection getLocalDbIfConnected() {
        return localDb;
    }

    public void setLocalDb(LocalDbConnection newLocalDb) {
        localDb = newLocalDb;
    }

    private OnlineDbConnection getOnlineDb() {
        if (onlineDb == null) connectToOnlineDb();
        return onlineDb;
    }

    public OnlineDbConnection getOnlineDbIfConnected() {
        return onlineDb;
    }

    public void setOnlineDb(OnlineDbConnection newOnlineDb) {
        onlineDb = newOnlineDb;
    }

    public void connectToLocalDb(Context context) {
        localDb = new LocalDbConnection(context);
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

    public void insertHistory(int minutes) {
        if (Session.getSignedIn()) {
            OnlineDbHelper.insertHistory(minutes);
        } else {
            Log.i("DatabaseManager", "Cannot insert history while anonymous because there is no user");
        }
    }
}