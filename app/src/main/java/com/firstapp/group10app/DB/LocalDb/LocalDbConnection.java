package com.firstapp.group10app.DB.LocalDb;


import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.firstapp.group10app.DB.Exercise;
import com.firstapp.group10app.DB.LocalDb.WorkoutContract.WorkoutEntry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is used to interact with the database (This may be combined with DbHelper in the future)
 * <p>
 * To create this class, I used the <a href="https://developer.android.com/training/data-storage/sqlite">Android Studio documentation</a>
 */
public class LocalDbConnection {
    LocalDbHelper localDbHelper;
    private SQLiteDatabase sqLiteDatabase;

    public LocalDbConnection(Context context) {
        localDbHelper = new LocalDbHelper(context);

        // Gets the data repository in write mode
        sqLiteDatabase = localDbHelper.getWritableDatabase();
    }

    public void insertExercise(String exerciseName, String description, String illustration, String targetMuscleGroup, String equipment, String difficulty, int sets, int reps, int time) {
        ContentValues values = new ContentValues();
        values.put(ExerciseContract.ExerciseEntry.COLUMN_NAME_EXERCISE_NAME, exerciseName);
        values.put(ExerciseContract.ExerciseEntry.COLUMN_NAME_DESCRIPTION, description);
        values.put(ExerciseContract.ExerciseEntry.COLUMN_NAME_ILLUSTRATION, illustration);
        values.put(ExerciseContract.ExerciseEntry.COLUMN_NAME_TARGET_MUSCLE_GROUP, targetMuscleGroup);
        values.put(ExerciseContract.ExerciseEntry.COLUMN_NAME_EQUIPMENT, equipment);
        values.put(ExerciseContract.ExerciseEntry.COLUMN_NAME_DIFFICULTY, difficulty);
        values.put(ExerciseContract.ExerciseEntry.COLUMN_NAME_SETS, sets);
        values.put(ExerciseContract.ExerciseEntry.COLUMN_NAME_REPS, reps);
        values.put(ExerciseContract.ExerciseEntry.COLUMN_NAME_TIME, time);

        long newRowId = sqLiteDatabase.insert(ExerciseContract.ExerciseEntry.TABLE_NAME, null, values);
    }

    public List<Long> readExercise(String targetMuscleGroup) {
        String[] projection = {
                ExerciseContract.ExerciseEntry._ID,
                ExerciseContract.ExerciseEntry.COLUMN_NAME_EXERCISE_NAME,
                ExerciseContract.ExerciseEntry.COLUMN_NAME_DESCRIPTION,
                ExerciseContract.ExerciseEntry.COLUMN_NAME_ILLUSTRATION,
                ExerciseContract.ExerciseEntry.COLUMN_NAME_TARGET_MUSCLE_GROUP,
                ExerciseContract.ExerciseEntry.COLUMN_NAME_EQUIPMENT,
                ExerciseContract.ExerciseEntry.COLUMN_NAME_DIFFICULTY,
                ExerciseContract.ExerciseEntry.COLUMN_NAME_SETS,
                ExerciseContract.ExerciseEntry.COLUMN_NAME_REPS,
                ExerciseContract.ExerciseEntry.COLUMN_NAME_TIME
        };

        String selection = ExerciseContract.ExerciseEntry.COLUMN_NAME_TARGET_MUSCLE_GROUP + " = ?";
        String[] selectionArgs = {targetMuscleGroup};

        String sortOrder = ExerciseContract.ExerciseEntry.COLUMN_NAME_EXERCISE_NAME + " DESC";

        Cursor cursor = sqLiteDatabase.query(
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

    public List<Long> readExercise(int exerciseID) {
        String[] projection = {
                ExerciseContract.ExerciseEntry._ID,
                ExerciseContract.ExerciseEntry.COLUMN_NAME_EXERCISE_NAME,
                ExerciseContract.ExerciseEntry.COLUMN_NAME_DESCRIPTION,
                ExerciseContract.ExerciseEntry.COLUMN_NAME_ILLUSTRATION,
                ExerciseContract.ExerciseEntry.COLUMN_NAME_TARGET_MUSCLE_GROUP,
                ExerciseContract.ExerciseEntry.COLUMN_NAME_EQUIPMENT,
                ExerciseContract.ExerciseEntry.COLUMN_NAME_DIFFICULTY,
                ExerciseContract.ExerciseEntry.COLUMN_NAME_SETS,
                ExerciseContract.ExerciseEntry.COLUMN_NAME_REPS,
                ExerciseContract.ExerciseEntry.COLUMN_NAME_TIME
        };

        String selection = ExerciseContract.ExerciseEntry._ID + " = ?";
        String[] selectionArgs = {String.valueOf(exerciseID)};

        String sortOrder = ExerciseContract.ExerciseEntry.COLUMN_NAME_EXERCISE_NAME + " DESC";

        Cursor cursor = sqLiteDatabase.query(
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

    public List<Exercise> getAllExercises() {
        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                ExerciseContract.ExerciseEntry._ID,
                ExerciseContract.ExerciseEntry.COLUMN_NAME_EXERCISE_NAME,
                ExerciseContract.ExerciseEntry.COLUMN_NAME_DESCRIPTION,
                ExerciseContract.ExerciseEntry.COLUMN_NAME_ILLUSTRATION,
                ExerciseContract.ExerciseEntry.COLUMN_NAME_TARGET_MUSCLE_GROUP,
                ExerciseContract.ExerciseEntry.COLUMN_NAME_EQUIPMENT,
                ExerciseContract.ExerciseEntry.COLUMN_NAME_DIFFICULTY,
                ExerciseContract.ExerciseEntry.COLUMN_NAME_SETS,
                ExerciseContract.ExerciseEntry.COLUMN_NAME_REPS,
                ExerciseContract.ExerciseEntry.COLUMN_NAME_TIME
        };

        // How you want the results sorted in the resulting Cursor
        String sortOrder = ExerciseContract.ExerciseEntry.COLUMN_NAME_EXERCISE_NAME + " ASC";

        Cursor cursor = sqLiteDatabase.query(
                ExerciseContract.ExerciseEntry.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                null,              // The columns for the WHERE clause
                null,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                sortOrder               // The sort order
        );

        List<Exercise> exercises = new ArrayList<>();
        while (cursor.moveToNext()) {
            long id = cursor.getLong(cursor.getColumnIndexOrThrow(ExerciseContract.ExerciseEntry._ID));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(ExerciseContract.ExerciseEntry.COLUMN_NAME_EXERCISE_NAME));
            String description = cursor.getString(cursor.getColumnIndexOrThrow(ExerciseContract.ExerciseEntry.COLUMN_NAME_DESCRIPTION));
            String illustration = cursor.getString(cursor.getColumnIndexOrThrow(ExerciseContract.ExerciseEntry.COLUMN_NAME_ILLUSTRATION));
            String targetMuscleGroup = cursor.getString(cursor.getColumnIndexOrThrow(ExerciseContract.ExerciseEntry.COLUMN_NAME_TARGET_MUSCLE_GROUP));
            String equipment = cursor.getString(cursor.getColumnIndexOrThrow(ExerciseContract.ExerciseEntry.COLUMN_NAME_EQUIPMENT));
            String difficulty = cursor.getString(cursor.getColumnIndexOrThrow(ExerciseContract.ExerciseEntry.COLUMN_NAME_DIFFICULTY));
            int sets = cursor.getInt(cursor.getColumnIndexOrThrow(ExerciseContract.ExerciseEntry.COLUMN_NAME_SETS));
            int reps = cursor.getInt(cursor.getColumnIndexOrThrow(ExerciseContract.ExerciseEntry.COLUMN_NAME_REPS));
            int time = cursor.getInt(cursor.getColumnIndexOrThrow(ExerciseContract.ExerciseEntry.COLUMN_NAME_TIME));

            Exercise exercise = new Exercise(id, name, description, illustration, targetMuscleGroup, equipment, difficulty, sets, reps, time);
            exercises.add(exercise);
        }
        cursor.close();

        return exercises;
    }

    public void updateExercise(int exerciseID, String exerciseName, String description, String illustration, String targetMuscleGroup, String equipment, String difficulty, int sets, int reps, int time) {
        ContentValues values = new ContentValues();
        values.put(ExerciseContract.ExerciseEntry.COLUMN_NAME_EXERCISE_NAME, exerciseName);
        values.put(ExerciseContract.ExerciseEntry.COLUMN_NAME_DESCRIPTION, description);
        values.put(ExerciseContract.ExerciseEntry.COLUMN_NAME_ILLUSTRATION, illustration);
        values.put(ExerciseContract.ExerciseEntry.COLUMN_NAME_TARGET_MUSCLE_GROUP, targetMuscleGroup);
        values.put(ExerciseContract.ExerciseEntry.COLUMN_NAME_EQUIPMENT, equipment);
        values.put(ExerciseContract.ExerciseEntry.COLUMN_NAME_DIFFICULTY, difficulty);
        values.put(ExerciseContract.ExerciseEntry.COLUMN_NAME_SETS, sets);
        values.put(ExerciseContract.ExerciseEntry.COLUMN_NAME_REPS, reps);
        values.put(ExerciseContract.ExerciseEntry.COLUMN_NAME_TIME, time);

        String selection = ExerciseContract.ExerciseEntry._ID + " = ?";
        String[] selectionArgs = {String.valueOf(exerciseID)};

        int count = sqLiteDatabase.update(
                ExerciseContract.ExerciseEntry.TABLE_NAME,
                values,
                selection,
                selectionArgs
        );
    }

    public void insertWorkout(String workoutName, int duration, String muscleGroup, String equipment, int difficulty) {
        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(WorkoutEntry.COLUMN_NAME_WORKOUT_NAME, workoutName);
        values.put(WorkoutEntry.COLUMN_NAME_DURATION, duration);
        values.put(WorkoutEntry.COLUMN_NAME_MUSCLE_GROUP, muscleGroup);
        values.put(WorkoutEntry.COLUMN_NAME_EQUIPMENT, equipment);
        values.put(WorkoutEntry.COLUMN_NAME_DIFFICULTY, difficulty);

        // Insert the new row, returning the primary key value of the new row
        long newRowId = sqLiteDatabase.insert(WorkoutEntry.TABLE_NAME, null, values);
    }

    public List<Long> readWorkout(int workoutId) {
        String[] projection = {
                WorkoutEntry._ID,
                WorkoutEntry.COLUMN_NAME_WORKOUT_NAME,
                WorkoutEntry.COLUMN_NAME_DURATION,
                WorkoutEntry.COLUMN_NAME_MUSCLE_GROUP,
                WorkoutEntry.COLUMN_NAME_EQUIPMENT,
                WorkoutEntry.COLUMN_NAME_DIFFICULTY
        };

        String selection = WorkoutEntry._ID + " = ?";
        String[] selectionArgs = {String.valueOf(workoutId)};

        String sortOrder = WorkoutEntry.COLUMN_NAME_DURATION + " DESC";

        Cursor cursor = sqLiteDatabase.query(
                WorkoutEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
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

    public List<Long> readWorkout(String muscleGroup) {
        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                WorkoutEntry._ID,
                WorkoutEntry.COLUMN_NAME_WORKOUT_NAME,
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

        Cursor cursor = sqLiteDatabase.query(
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

    /**
     * Retrieves all workouts and associate exercises from the database.
     * To achieve this, the method uses a complex SQL query to join the Workouts, ExerciseWorkoutPairs,
     * and Exercises tables.
     *
     * @param filter An optional filter to apply to the workouts.
     * @return A JSON string containing the workouts.
     */
    @SuppressLint("Range")
    public String getWorkoutsAsJsonArray(String filter) {
        try {
            JSONArray workoutsArray = new JSONArray();

            String workoutQuery = "SELECT * FROM " + WorkoutContract.WorkoutEntry.TABLE_NAME + " " + WorkoutContract.WorkoutEntry.TABLE_NAME.toLowerCase().charAt(0);
            if (filter != null && !filter.isEmpty()) {
                if (filter.contains("WHERE")) workoutQuery += " " + filter;
                else workoutQuery += " WHERE " + filter;
            }

            Cursor workoutCursor = sqLiteDatabase.rawQuery(workoutQuery, null);
            while (workoutCursor.moveToNext()) {
                JSONObject workoutObject = new JSONObject();
                workoutObject.put("WorkoutID", workoutCursor.getInt(workoutCursor.getColumnIndex(WorkoutContract.WorkoutEntry._ID)));
                workoutObject.put("WorkoutName", workoutCursor.getString(workoutCursor.getColumnIndex(WorkoutContract.WorkoutEntry.COLUMN_NAME_WORKOUT_NAME)));
                workoutObject.put("WorkoutDuration", workoutCursor.getInt(workoutCursor.getColumnIndex(WorkoutContract.WorkoutEntry.COLUMN_NAME_DURATION)));
                workoutObject.put("TargetMuscleGroup", workoutCursor.getString(workoutCursor.getColumnIndex(WorkoutContract.WorkoutEntry.COLUMN_NAME_MUSCLE_GROUP)));
                workoutObject.put("Equipment", workoutCursor.getString(workoutCursor.getColumnIndex(WorkoutContract.WorkoutEntry.COLUMN_NAME_EQUIPMENT)));
                workoutObject.put("Difficulty", workoutCursor.getInt(workoutCursor.getColumnIndex(WorkoutContract.WorkoutEntry.COLUMN_NAME_DIFFICULTY)));

                int workoutId = workoutCursor.getInt(workoutCursor.getColumnIndex(WorkoutContract.WorkoutEntry._ID));
                String exerciseQuery = "SELECT * FROM " + ExerciseContract.ExerciseEntry.TABLE_NAME +
                        " WHERE " + ExerciseContract.ExerciseEntry._ID + " IN (" +
                        "SELECT " + ExerciseWorkoutPairContract.ExerciseWorkoutPairEntry.COLUMN_NAME_EXERCISE_ID +
                        " FROM " + ExerciseWorkoutPairContract.ExerciseWorkoutPairEntry.TABLE_NAME +
                        " WHERE " + ExerciseWorkoutPairContract.ExerciseWorkoutPairEntry.COLUMN_NAME_WORKOUT_ID + " = " + workoutId + ")";

                Cursor exerciseCursor = sqLiteDatabase.rawQuery(exerciseQuery, null);
                JSONArray exercisesArray = new JSONArray();
                while (exerciseCursor.moveToNext()) {
                    JSONObject exerciseObject = new JSONObject();
                    exerciseObject.put("ExerciseID", exerciseCursor.getInt(exerciseCursor.getColumnIndex(ExerciseContract.ExerciseEntry._ID)));
                    exerciseObject.put("ExerciseName", exerciseCursor.getString(exerciseCursor.getColumnIndex(ExerciseContract.ExerciseEntry.COLUMN_NAME_EXERCISE_NAME)));
                    exerciseObject.put("Description", exerciseCursor.getString(exerciseCursor.getColumnIndex(ExerciseContract.ExerciseEntry.COLUMN_NAME_DESCRIPTION)));
                    exerciseObject.put("Illustration", exerciseCursor.getString(exerciseCursor.getColumnIndex(ExerciseContract.ExerciseEntry.COLUMN_NAME_ILLUSTRATION)));
                    exerciseObject.put("TargetMuscleGroup", exerciseCursor.getString(exerciseCursor.getColumnIndex(ExerciseContract.ExerciseEntry.COLUMN_NAME_TARGET_MUSCLE_GROUP)));
                    exerciseObject.put("Equipment", exerciseCursor.getString(exerciseCursor.getColumnIndex(ExerciseContract.ExerciseEntry.COLUMN_NAME_EQUIPMENT)));
                    exerciseObject.put("Difficulty", exerciseCursor.getString(exerciseCursor.getColumnIndex(ExerciseContract.ExerciseEntry.COLUMN_NAME_DIFFICULTY)));
                    exerciseObject.put("Sets", exerciseCursor.getInt(exerciseCursor.getColumnIndex(ExerciseContract.ExerciseEntry.COLUMN_NAME_SETS)));
                    exerciseObject.put("Reps", exerciseCursor.getInt(exerciseCursor.getColumnIndex(ExerciseContract.ExerciseEntry.COLUMN_NAME_REPS)));
                    exerciseObject.put("Time", exerciseCursor.getInt(exerciseCursor.getColumnIndex(ExerciseContract.ExerciseEntry.COLUMN_NAME_TIME)));
                    exercisesArray.put(exerciseObject);
                }
                exerciseCursor.close();

                workoutObject.put("Exercises", exercisesArray);
                workoutsArray.put(workoutObject);
            }
            workoutCursor.close();

            return workoutsArray.toString();
        } catch (JSONException e) {
            throw new RuntimeException("Error creating JSON", e);
        }
    }

    public void insertExerciseWorkoutPair(int exerciseID, int workoutID) {
        ContentValues values = new ContentValues();
        values.put(ExerciseWorkoutPairContract.ExerciseWorkoutPairEntry.COLUMN_NAME_EXERCISE_ID, exerciseID);
        values.put(ExerciseWorkoutPairContract.ExerciseWorkoutPairEntry.COLUMN_NAME_WORKOUT_ID, workoutID);

        long newRowId = sqLiteDatabase.insert(ExerciseWorkoutPairContract.ExerciseWorkoutPairEntry.TABLE_NAME, null, values);
    }

    public List<Long> readExerciseWorkoutPair(int workoutID) {
        String[] projection = {
                ExerciseWorkoutPairContract.ExerciseWorkoutPairEntry.COLUMN_NAME_EXERCISE_ID,
                ExerciseWorkoutPairContract.ExerciseWorkoutPairEntry.COLUMN_NAME_WORKOUT_ID
        };

        String selection = ExerciseWorkoutPairContract.ExerciseWorkoutPairEntry.COLUMN_NAME_WORKOUT_ID + " = ?";
        String[] selectionArgs = {String.valueOf(workoutID)};

        String sortOrder = ExerciseWorkoutPairContract.ExerciseWorkoutPairEntry.COLUMN_NAME_WORKOUT_ID + " DESC";

        Cursor cursor = sqLiteDatabase.query(
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
            long itemId = cursor.getLong(cursor.getColumnIndexOrThrow(ExerciseWorkoutPairContract.ExerciseWorkoutPairEntry.COLUMN_NAME_WORKOUT_ID));
            itemIds.add(itemId);
        }
        cursor.close();

        return itemIds;
    }

    public void insertSampleData() {
        // Check if the sample data already exists in the Exercise table
        List<Long> existingExerciseIds = readExercise("Sample muscle group");
        if (existingExerciseIds.isEmpty()) {
            // If not, insert a sample row into the Exercise table
            insertExercise("Sample Exercise", "This is a sample exercise.", "Sample illustration", "Sample muscle group", "Sample equipment", "Sample difficulty", 5, 10, 20);
        }

        // Check if the sample data already exists in the Workout table
        List<Long> existingWorkoutIds = readWorkout("Sample muscle group");
        if (existingWorkoutIds.isEmpty()) {
            insertWorkout("Sample workout name", 30, "Sample muscle group", "Sample equipment", 1);
        }

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
            List<Long> exerciseWorkoutPairWorkoutId = readExerciseWorkoutPair(workoutIdInt);
            if (exerciseWorkoutPairWorkoutId.isEmpty()) {
                insertExerciseWorkoutPair(exerciseIdInt, workoutIdInt);
            }
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
        List<Long> exerciseWorkoutPairWorkoutIds = readExerciseWorkoutPair(workoutIdInt);
        for (Object id : exerciseWorkoutPairWorkoutIds) {
            Log.d("ExerciseWorkoutPair Data", "Workout ID: " + id);
        }

        // Print the exercise name and workout difficulty from a pair (if it exists)
        if (!exerciseWorkoutPairWorkoutIds.isEmpty()) {
            long exerciseWorkoutPairWorkoutId = exerciseWorkoutPairWorkoutIds.get(0);
            int exerciseWorkoutPairWorkoutIdInt = (int) exerciseWorkoutPairWorkoutId;

            Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM ExerciseWorkoutPairs WHERE WorkoutID = " + exerciseWorkoutPairWorkoutIdInt, null);

            if (cursor.moveToFirst()) {
                @SuppressLint("Range") int exerciseID = cursor.getInt(cursor.getColumnIndex(ExerciseWorkoutPairContract.ExerciseWorkoutPairEntry.COLUMN_NAME_EXERCISE_ID));

                Log.d("ExerciseWorkoutPair Data", "Exercise ID: " + exerciseID + ", Workout ID: " + exerciseWorkoutPairWorkoutId);
            }
            cursor.close();
        }
    }

    public void close() {
        localDbHelper.close();
    }

    public SQLiteDatabase getReadableDatabase() {
        return this.sqLiteDatabase;
    }

    public Cursor executeQuery(String query) {
        return sqLiteDatabase.rawQuery(query, null);
    }

    public void executeStatement(String statement) {
        sqLiteDatabase.execSQL(statement);
    }
}