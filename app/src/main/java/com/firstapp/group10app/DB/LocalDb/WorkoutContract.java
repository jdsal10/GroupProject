package com.firstapp.group10app.DB.LocalDb;

import android.provider.BaseColumns;

/**
 * This class is used to define the ExerciseWorkoutPair table
 * <p>
 * To create this class, I used the <a href="https://developer.android.com/training/data-storage/sqlite">Android Studio documentation</a>
 */
public final class WorkoutContract {
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private WorkoutContract() {
    }

    /* Inner class that defines the table contents */
    public static class WorkoutEntry implements BaseColumns {
        public static final String TABLE_NAME = "workout";
        public static final String COLUMN_NAME_DURATION = "workoutDuration";
        public static final String COLUMN_NAME_MUSCLE_GROUP = "targetMuscleGroup";
        public static final String COLUMN_NAME_EQUIPMENT = "equipment";
        public static final String COLUMN_NAME_DIFFICULTY = "difficulty";
    }

    static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + WorkoutEntry.TABLE_NAME + " (" +
                    WorkoutEntry._ID + " INTEGER PRIMARY KEY," +
                    WorkoutEntry.COLUMN_NAME_DURATION + " INTEGER," +
                    WorkoutEntry.COLUMN_NAME_MUSCLE_GROUP + " TEXT," +
                    WorkoutEntry.COLUMN_NAME_EQUIPMENT + " TEXT," +
                    WorkoutEntry.COLUMN_NAME_DIFFICULTY + " INTEGER)";
    static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + WorkoutEntry.TABLE_NAME;
}