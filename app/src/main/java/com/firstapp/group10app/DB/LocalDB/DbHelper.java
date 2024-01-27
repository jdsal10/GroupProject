package com.firstapp.group10app.DB.LocalDB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;

    public DbHelper(Context context, String DATABASE_NAME) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        if (db.getPath().contains("Exercise.db")) {
            db.execSQL(ExerciseContract.SQL_CREATE_ENTRIES);
        } else if (db.getPath().contains("Workout.db")) {
            db.execSQL(WorkoutContract.SQL_CREATE_ENTRIES);
        } else if (db.getPath().contains("ExerciseWorkoutPair.db")) {
            db.execSQL(ExerciseWorkoutPairContract.SQL_CREATE_ENTRIES);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (db.getPath().contains("Exercise.db")) {
            db.execSQL(ExerciseContract.SQL_DELETE_ENTRIES);
        } else if (db.getPath().contains("Workout.db")) {
            db.execSQL(WorkoutContract.SQL_DELETE_ENTRIES);
        } else if (db.getPath().contains("ExerciseWorkoutPair.db")) {
            db.execSQL(ExerciseWorkoutPairContract.SQL_DELETE_ENTRIES);
        }
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}