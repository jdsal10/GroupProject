package com.firstapp.group10app.DB.LocalDb;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.firstapp.group10app.DB.LocalDb.WorkoutContract.WorkoutEntry;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is used to interact with the database (This may be combined with DbHelper in the future)
 * <p>
 * To create this class, I used the <a href="https://developer.android.com/training/data-storage/sqlite">Android Studio documentation</a>
 */
public class LocalDb {
    DbHelper dbHelper;
    SQLiteDatabase db;

    public LocalDb(Context context) {
        dbHelper = new DbHelper(context);

        // Gets the data repository in write mode
        db = dbHelper.getWritableDatabase();
    }

    public void insertExercise(String exerciseName, String description, String illustration, String targetMuscleGroup, String equipment, int difficulty) {
        ContentValues values = new ContentValues();
        values.put(ExerciseContract.ExerciseEntry.COLUMN_NAME_EXERCISE_NAME, exerciseName);
        values.put(ExerciseContract.ExerciseEntry.COLUMN_NAME_DESCRIPTION, description);
        values.put(ExerciseContract.ExerciseEntry.COLUMN_NAME_ILLUSTRATION, illustration);
        values.put(ExerciseContract.ExerciseEntry.COLUMN_NAME_TARGET_MUSCLE_GROUP, targetMuscleGroup);
        values.put(ExerciseContract.ExerciseEntry.COLUMN_NAME_EQUIPMENT, equipment);
        values.put(ExerciseContract.ExerciseEntry.COLUMN_NAME_DIFFICULTY, difficulty);

        long newRowId = db.insert(ExerciseContract.ExerciseEntry.TABLE_NAME, null, values);
    }

    public List<Long> readExercise(String targetMuscleGroup) {
        String[] projection = {
                ExerciseContract.ExerciseEntry._ID,
                ExerciseContract.ExerciseEntry.COLUMN_NAME_EXERCISE_NAME,
                ExerciseContract.ExerciseEntry.COLUMN_NAME_DESCRIPTION,
                ExerciseContract.ExerciseEntry.COLUMN_NAME_ILLUSTRATION,
                ExerciseContract.ExerciseEntry.COLUMN_NAME_TARGET_MUSCLE_GROUP,
                ExerciseContract.ExerciseEntry.COLUMN_NAME_EQUIPMENT,
                ExerciseContract.ExerciseEntry.COLUMN_NAME_DIFFICULTY
        };

        String selection = ExerciseContract.ExerciseEntry.COLUMN_NAME_TARGET_MUSCLE_GROUP + " = ?";
        String[] selectionArgs = {targetMuscleGroup};

        String sortOrder = ExerciseContract.ExerciseEntry.COLUMN_NAME_EXERCISE_NAME + " DESC";

        Cursor cursor = db.query(
                ExerciseContract.ExerciseEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );

        List<Long> itemIds = new ArrayList<>();
        while (cursor.moveToNext()) {
            long itemId = cursor.getLong(cursor.getColumnIndexOrThrow(ExerciseContract.ExerciseEntry._ID));
            itemIds.add(itemId);
        }
        cursor.close();

        return itemIds;
    }

    public void updateExercise(int exerciseID, String exerciseName, String description, String illustration, String targetMuscleGroup, String equipment, int difficulty) {
        ContentValues values = new ContentValues();
        values.put(ExerciseContract.ExerciseEntry.COLUMN_NAME_EXERCISE_NAME, exerciseName);
        values.put(ExerciseContract.ExerciseEntry.COLUMN_NAME_DESCRIPTION, description);
        values.put(ExerciseContract.ExerciseEntry.COLUMN_NAME_ILLUSTRATION, illustration);
        values.put(ExerciseContract.ExerciseEntry.COLUMN_NAME_TARGET_MUSCLE_GROUP, targetMuscleGroup);
        values.put(ExerciseContract.ExerciseEntry.COLUMN_NAME_EQUIPMENT, equipment);
        values.put(ExerciseContract.ExerciseEntry.COLUMN_NAME_DIFFICULTY, difficulty);

        String selection = ExerciseContract.ExerciseEntry._ID + " = ?";
        String[] selectionArgs = {String.valueOf(exerciseID)};

        int count = db.update(
                ExerciseContract.ExerciseEntry.TABLE_NAME,
                values,
                selection,
                selectionArgs
        );
    }

    public void insertWorkout(int duration, String muscleGroup, String equipment, int difficulty) {
        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(WorkoutEntry.COLUMN_NAME_DURATION, duration);
        values.put(WorkoutEntry.COLUMN_NAME_MUSCLE_GROUP, muscleGroup);
        values.put(WorkoutEntry.COLUMN_NAME_EQUIPMENT, equipment);
        values.put(WorkoutEntry.COLUMN_NAME_DIFFICULTY, difficulty);

        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(WorkoutEntry.TABLE_NAME, null, values);
    }

    public List<Long> readWorkout(String muscleGroup) {
        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                WorkoutEntry._ID,
                WorkoutEntry.COLUMN_NAME_DURATION,
                WorkoutEntry.COLUMN_NAME_MUSCLE_GROUP,
                WorkoutEntry.COLUMN_NAME_EQUIPMENT,
                WorkoutEntry.COLUMN_NAME_DIFFICULTY
        };

        // Filter results WHERE "title" = 'My Title'
        String selection = WorkoutEntry.COLUMN_NAME_MUSCLE_GROUP + " = ?";
        String[] selectionArgs = {muscleGroup};

        // How you want the results sorted in the resulting Cursor
        String sortOrder =
                WorkoutEntry.COLUMN_NAME_DURATION + " DESC";

        Cursor cursor = db.query(
                WorkoutEntry.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                selectionArgs,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                sortOrder               // The sort order
        );

        List<Long> itemIds = new ArrayList<>();
        while (cursor.moveToNext()) {
            long itemId = cursor.getLong(
                    cursor.getColumnIndexOrThrow(WorkoutEntry._ID));
            itemIds.add(itemId);
        }
        cursor.close();

        return itemIds;
    }

    public void insertExerciseWorkoutPair(int exerciseID, int workoutID) {
        ContentValues values = new ContentValues();
        values.put(ExerciseWorkoutPairContract.ExerciseWorkoutPairEntry.COLUMN_NAME_EXERCISE_ID, exerciseID);
        values.put(ExerciseWorkoutPairContract.ExerciseWorkoutPairEntry.COLUMN_NAME_WORKOUT_ID, workoutID);

        long newRowId = db.insert(ExerciseWorkoutPairContract.ExerciseWorkoutPairEntry.TABLE_NAME, null, values);
    }

    public List<Long> readExerciseWorkoutPair(int workoutID) {
        String[] projection = {
                ExerciseWorkoutPairContract.ExerciseWorkoutPairEntry._ID,
                ExerciseWorkoutPairContract.ExerciseWorkoutPairEntry.COLUMN_NAME_EXERCISE_ID,
                ExerciseWorkoutPairContract.ExerciseWorkoutPairEntry.COLUMN_NAME_WORKOUT_ID
        };

        String selection = ExerciseWorkoutPairContract.ExerciseWorkoutPairEntry.COLUMN_NAME_WORKOUT_ID + " = ?";
        String[] selectionArgs = {String.valueOf(workoutID)};

        String sortOrder = ExerciseWorkoutPairContract.ExerciseWorkoutPairEntry._ID + " DESC";

        Cursor cursor = db.query(
                ExerciseWorkoutPairContract.ExerciseWorkoutPairEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );

        List<Long> itemIds = new ArrayList<>();
        while (cursor.moveToNext()) {
            long itemId = cursor.getLong(cursor.getColumnIndexOrThrow(ExerciseWorkoutPairContract.ExerciseWorkoutPairEntry._ID));
            itemIds.add(itemId);
        }
        cursor.close();

        return itemIds;
    }

    public void insertSampleData() {
        // Insert a sample row into the Exercise table
        insertExercise("Sample Exercise", "This is a sample exercise.", "Sample illustration", "Sample muscle group", "Sample equipment", 1);

        // Insert a sample row into the Workout table
        insertWorkout(30, "Sample muscle group", "Sample equipment", 1);

        // Get the IDs of the inserted Exercise and Workout
        List<Long> exerciseIds = readExercise("Sample muscle group");
        List<Long> workoutIds = readWorkout("Sample muscle group");

        // Check if the IDs were retrieved successfully
        if (!exerciseIds.isEmpty() && !workoutIds.isEmpty()) {
            long exerciseId = exerciseIds.get(0);
            long workoutId = workoutIds.get(0);

            int exerciseIdInt = (int) exerciseId;
            int workoutIdInt = (int) workoutId;

            // Insert a sample row into the ExerciseWorkoutPair table
            insertExerciseWorkoutPair(exerciseIdInt, workoutIdInt);
        }
    }

    public void printDataForDebugging() {
        // Print data from Exercise table
        List<Long> exerciseIds = readExercise("Sample muscle group");
        for (Object id : exerciseIds) {
            Log.d("Exercise Data", "ID: " + id);
        }

        // Print data from Workout table
        List<Long> workoutIds = readWorkout("Sample muscle group");
        for (Object id : workoutIds) {
            Log.d("Workout Data", "ID: " + id);
        }

        long workoutId = workoutIds.get(0);
        int workoutIdInt = (int) workoutId;

        // Print data from ExerciseWorkoutPair table
        List<Long> exerciseWorkoutPairIds = readExerciseWorkoutPair(workoutIdInt);
        for (Object id : exerciseWorkoutPairIds) {
            Log.d("ExerciseWorkoutPair Data", "ID: " + id);
        }
    }

    public void close() {
        dbHelper.close();
    }
}