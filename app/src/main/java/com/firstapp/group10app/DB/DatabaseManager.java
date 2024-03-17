package com.firstapp.group10app.DB;

import android.content.Context;

import com.firstapp.group10app.DB.LocalDb.LocalDb;
import com.firstapp.group10app.DB.OnlineDb.DbConnection;

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

    public void connectToLocalDb(Context context) {
        localDb = new LocalDb(context);
    }

    public void connectToOnlineDb() {
        onlineDb = new DbConnection();
    }

    public Object getDatabaseConnection(boolean isAnonymous) {
        if (isAnonymous) {
            return localDb;
        } else {
            return onlineDb;
        }
    }
}