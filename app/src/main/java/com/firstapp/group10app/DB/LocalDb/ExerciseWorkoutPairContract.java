package com.firstapp.group10app.DB.LocalDb;

import android.provider.BaseColumns;

/**
 * This class is used to define the ExerciseWorkoutPair table
 * <p>
 * To create this class, I used the <a href="https://developer.android.com/training/data-storage/sqlite">Android Studio documentation</a>
 */
public final class ExerciseWorkoutPairContract {
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private ExerciseWorkoutPairContract() {
    }

    /* Inner class that defines the table contents */
    public static class ExerciseWorkoutPairEntry implements BaseColumns {
        public static final String TABLE_NAME = "ExerciseWorkoutPairs";
        public static final String COLUMN_NAME_EXERCISE_ID = "ExerciseID";
        public static final String COLUMN_NAME_WORKOUT_ID = "WorkoutID";
    }

    static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + ExerciseWorkoutPairEntry.TABLE_NAME + " (" +
                    ExerciseWorkoutPairEntry.COLUMN_NAME_EXERCISE_ID + " INTEGER," +
                    ExerciseWorkoutPairEntry.COLUMN_NAME_WORKOUT_ID + " INTEGER," +
                    "PRIMARY KEY (" + ExerciseWorkoutPairEntry.COLUMN_NAME_EXERCISE_ID + ", " + ExerciseWorkoutPairEntry.COLUMN_NAME_WORKOUT_ID + ")," +
                    "FOREIGN KEY (" + ExerciseWorkoutPairEntry.COLUMN_NAME_EXERCISE_ID + ") REFERENCES Exercise(_ID)," +
                    "FOREIGN KEY (" + ExerciseWorkoutPairEntry.COLUMN_NAME_WORKOUT_ID + ") REFERENCES Workout(_ID))";

    static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + ExerciseWorkoutPairEntry.TABLE_NAME;
}