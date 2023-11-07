package com.firstapp.group10app.DB;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {
    public static final String USER_TABLE = "USER_TABLE";

    public DBHelper(@Nullable Context context) {
        super(context, "users.db", null, 1);
    }

    // First time a DB is accessed. Should create a new table.
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + USER_TABLE + " (" +
                "UserID INTEGER PRIMARY KEY," +
                "Email TEXT," +
                "FirstName TEXT," +
                "SecondName TEXT," +
                "Password TEXT," +
                "DOB TEXT," +
                "Weight REAL," +
                "Height REAL," +
                "Sex TEXT," +
                "Allergies TEXT," +
                "ReasonForDownloading TEXT" +
                ");";
        db.execSQL(createTable);
    }

    // Called if DB version number changes. Prevents loss of information if the DB design changes.
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
