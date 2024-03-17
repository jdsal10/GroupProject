package com.firstapp.group10app.DB;

import android.content.Context;

import com.firstapp.group10app.DB.LocalDb.LocalDb;
import com.firstapp.group10app.DB.OnlineDb.DbConnection;

import java.sql.SQLException;

public class DatabaseManager {
    private static DatabaseManager instance;
    private LocalDb localDb;
    private DbConnection onlineDb;

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
        onlineDb = new DbConnection();
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

    private DbConnection getOnlineDbConnection() {
        if (onlineDb == null) connectToOnlineDb();

        return onlineDb;
    }

    public QueryResult executeQuery(String query, boolean signedIn) throws SQLException {
        Object dbConnection = getDatabaseConnection(signedIn);

        if (signedIn) {
            DbConnection onlineDb = (DbConnection) dbConnection;
            return new QueryResult(onlineDb.executeQuery(query));
        } else {
            LocalDb localDb = (LocalDb) dbConnection;
            return new QueryResult(localDb.executeQuery(query));
        }
    }

    public QueryResult executeQueryInOnlineDb(String query) throws SQLException {
        DbConnection thisOnlineDb = getOnlineDbConnection();
        return new QueryResult(thisOnlineDb.executeQuery(query));
    }

    public void executeStatement(String statement, boolean signedIn) throws SQLException {
        Object dbConnection = getDatabaseConnection(signedIn);

        if (signedIn) {
            DbConnection onlineDb = (DbConnection) dbConnection;
            onlineDb.executeStatement(statement);
        } else {
            LocalDb localDb = (LocalDb) dbConnection;
            // Assuming LocalDb has a similar method to execute statements
            localDb.executeStatement(statement);
        }
    }

    public void executeStatementInOnlineDb(String statement) throws SQLException {
        DbConnection thisOnlineDb = getOnlineDbConnection();
        thisOnlineDb.executeStatement(statement);
    }
}