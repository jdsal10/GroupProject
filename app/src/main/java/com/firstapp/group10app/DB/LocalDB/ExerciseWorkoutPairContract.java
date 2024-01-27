package com.firstapp.group10app.DB.LocalDB;

import android.provider.BaseColumns;

public final class ExerciseWorkoutPairContract {
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private ExerciseWorkoutPairContract() {
    }

    /* Inner class that defines the table contents */
    public static class ExerciseWorkoutPairEntry implements BaseColumns {
        public static final String TABLE_NAME = "exerciseWorkoutPair";
        public static final String COLUMN_NAME_EXERCISE_ID = "exerciseID";
        public static final String COLUMN_NAME_WORKOUT_ID = "workoutID";
    }

    protected static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + ExerciseWorkoutPairEntry.TABLE_NAME + " (" +
                    ExerciseWorkoutPairEntry._ID + " INTEGER PRIMARY KEY," +
                    ExerciseWorkoutPairEntry.COLUMN_NAME_EXERCISE_ID + " INTEGER," +
                    ExerciseWorkoutPairEntry.COLUMN_NAME_WORKOUT_ID + " INTEGER)";

    protected static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + ExerciseWorkoutPairEntry.TABLE_NAME;
}