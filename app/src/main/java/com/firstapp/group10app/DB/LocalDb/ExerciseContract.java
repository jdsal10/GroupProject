package com.firstapp.group10app.DB.LocalDb;

import android.provider.BaseColumns;

/**
 * This class is used to define the Exercise table
 * <p>
 * To create this class, I used the <a href="https://developer.android.com/training/data-storage/sqlite">Android Studio documentation</a>
 */
public final class ExerciseContract {
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private ExerciseContract() {
    }

    /* Inner class that defines the table contents */
    public static class ExerciseEntry implements BaseColumns {
        public static final String TABLE_NAME = "Exercises";
        public static final String COLUMN_NAME_EXERCISE_NAME = "ExerciseName";
        public static final String COLUMN_NAME_DESCRIPTION = "Description";
        public static final String COLUMN_NAME_ILLUSTRATION = "Illustration";
        public static final String COLUMN_NAME_TARGET_MUSCLE_GROUP = "TargetMuscleGroup";
        public static final String COLUMN_NAME_EQUIPMENT = "Equipment";
        public static final String COLUMN_NAME_DIFFICULTY = "Difficulty";
        public static final String COLUMN_NAME_SETS = "Sets";
        public static final String COLUMN_NAME_REPS = "Reps";
        public static final String COLUMN_NAME_TIME = "Time";
    }

    static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + ExerciseEntry.TABLE_NAME + " (" +
                    ExerciseEntry._ID + " INTEGER PRIMARY KEY," +
                    ExerciseEntry.COLUMN_NAME_EXERCISE_NAME + " TEXT," +
                    ExerciseEntry.COLUMN_NAME_DESCRIPTION + " TEXT," +
                    ExerciseEntry.COLUMN_NAME_ILLUSTRATION + " TEXT," +
                    ExerciseEntry.COLUMN_NAME_TARGET_MUSCLE_GROUP + " TEXT," +
                    ExerciseEntry.COLUMN_NAME_EQUIPMENT + " TEXT," +
                    ExerciseEntry.COLUMN_NAME_DIFFICULTY + " TEXT," +
                    ExerciseEntry.COLUMN_NAME_SETS + " INTEGER," +
                    ExerciseEntry.COLUMN_NAME_REPS + " INTEGER," +
                    ExerciseEntry.COLUMN_NAME_TIME + " INTEGER)";

    static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + ExerciseEntry.TABLE_NAME;
}