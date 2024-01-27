package com.firstapp.group10app.DB.LocalDb;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * This class is used to create the database and tables
 * It also handles upgrading and downgrading the database
 *
 * To create this class, I used the <a href="https://developer.android.com/training/data-storage/sqlite">Android Studio documentation</a>
 */
public class DbHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "AppDatabase.db";

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(ExerciseContract.SQL_CREATE_ENTRIES);
        db.execSQL(WorkoutContract.SQL_CREATE_ENTRIES);
        db.execSQL(ExerciseWorkoutPairContract.SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(ExerciseContract.SQL_DELETE_ENTRIES);
        db.execSQL(WorkoutContract.SQL_DELETE_ENTRIES);
        db.execSQL(ExerciseWorkoutPairContract.SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}