package com.firstapp.group10app.DB.LocalDB;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class LocalDB extends SQLiteOpenHelper {
    public static final String USER_TABLE = "USER_TABLE";

    public LocalDB(@Nullable Context context) {
        super(context, "users.db", null, 1);
    }

    // First time a DB is accessed. Should create a new table.
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + USER_TABLE + " (" +
                "UserID INTEGER PRIMARY KEY," +
                "Email TEXT NOT NULL," +
                "FirstName TEXT NOT NULL," +
                "SecondName TEXT NOT NULL," +
                "Password TEXT NOT NULL," +
                "DOB DATE NOT NULL," +
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

    public boolean addUser(WorkoutModel workoutModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("Email", workoutModel.getEmail());

        return true;
    }
}