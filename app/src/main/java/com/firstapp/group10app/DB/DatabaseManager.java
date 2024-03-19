package com.firstapp.group10app.DB;

import android.content.Context;

import com.firstapp.group10app.DB.LocalDb.LocalDb;
import com.firstapp.group10app.DB.OnlineDb.OnlineDbConnection;
import com.firstapp.group10app.DB.OnlineDb.OnlineDbHelper;
import com.firstapp.group10app.Other.Session;

import java.sql.SQLException;
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

    private LocalDb getLocalDb() {
        if (localDb == null) connectToLocalDb(null);
        return localDb;
    }

    private OnlineDbConnection getOnlineDb() {
        if (onlineDb == null) connectToOnlineDb();
        return onlineDb;
    }

    private void connectToLocalDb(Context context) {
        localDb = new LocalDb(context);
    }

    private void connectToOnlineDb() {
        onlineDb = OnlineDbConnection.getInstance();
    }

    public Object getDatabaseConnection(boolean signedIn) {
        if (signedIn) {
            return getOnlineDb();
        } else {
            return getLocalDb();
        }
    }

    public QueryResult executeQuery(String query, boolean signedIn) {
        if (signedIn) {
            return new QueryResult(getOnlineDb().executeQuery(query));
        } else {
            return new QueryResult(getLocalDb().executeQuery(query));
        }
    }

    public QueryResult executeQueryInOnlineDb(String query) {
        return new QueryResult(getOnlineDb().executeQuery(query));
    }

    public void executeStatement(String statement, boolean signedIn) {
        if (signedIn) {
            getOnlineDb().executeStatement(statement);
        } else {
            getLocalDb().executeStatement(statement);
        }
    }

    public void executeStatementInOnlineDb(String statement) throws SQLException {
        getOnlineDb().executeStatement(statement);
    }

    public List<Exercise> getAllExercises() {
        if (Session.getSignedIn()) {
            return OnlineDbHelper.getAllExercises();
        } else {
            return getLocalDb().getAllExercises();
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