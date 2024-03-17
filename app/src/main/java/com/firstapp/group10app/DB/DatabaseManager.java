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
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    private void connectToLocalDb(Context context) {
        localDb = new LocalDb(context);
    }

    private void connectToOnlineDb() {
        onlineDb = new OnlineDbConnection();
    }

    public Object getDatabaseConnection(boolean signedIn) {
        if (signedIn) {
            if (onlineDb == null) connectToOnlineDb();

            return onlineDb;
        } else {
            if (localDb == null) connectToLocalDb(null);

            return localDb;
        }
    }

    private OnlineDbConnection getOnlineDbConnection() {
        if (onlineDb == null) connectToOnlineDb();

        return onlineDb;
    }

    public QueryResult executeQuery(String query, boolean signedIn) throws SQLException {
        Object dbConnection = getDatabaseConnection(signedIn);

        if (signedIn) {
            OnlineDbConnection onlineDb = (OnlineDbConnection) dbConnection;
            return new QueryResult(onlineDb.executeQuery(query));
        } else {
            LocalDb localDb = (LocalDb) dbConnection;
            return new QueryResult(localDb.executeQuery(query));
        }
    }

    public QueryResult executeQueryInOnlineDb(String query) throws SQLException {
        OnlineDbConnection thisOnlineDb = getOnlineDbConnection();
        return new QueryResult(thisOnlineDb.executeQuery(query));
    }

    public void executeStatement(String statement, boolean signedIn) throws SQLException {
        Object dbConnection = getDatabaseConnection(signedIn);

        if (signedIn) {
            OnlineDbConnection onlineDb = (OnlineDbConnection) dbConnection;
            onlineDb.executeStatement(statement);
        } else {
            LocalDb localDb = (LocalDb) dbConnection;
            // Assuming LocalDb has a similar method to execute statements
            localDb.executeStatement(statement);
        }
    }

    public void executeStatementInOnlineDb(String statement) throws SQLException {
        OnlineDbConnection thisOnlineDb = getOnlineDbConnection();
        thisOnlineDb.executeStatement(statement);
    }

    public List<Exercise> getAllExercises() {
        if (Session.getSignedIn()) {
            // Call the getAllExercises method from the OnlineDbHelper class
            return OnlineDbHelper.getAllExercises();
        } else {
            // Call the getAllExercises method from the LocalDb class
            return localDb.getAllExercises();
        }
    }

//    public List<Workout> getAllWorkouts(String filter) {
//        if (Session.getSignedIn()) {
//            // Call the getAllExercises method from the OnlineDbHelper class
////            return OnlineDbHelper.getAllWorkouts(filter);
//            return null;
//        } else {
//            // Call the getAllExercises method from the LocalDb class
//            return localDb.getAllWorkouts();
//        }
//    }
}