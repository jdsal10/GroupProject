package com.firstapp.group10app.DB.OnlineDb;

import static com.firstapp.group10app.DB.OnlineDb.OnlineDbConnection.getConnection;
import static com.firstapp.group10app.Other.Encryption.getSHA;
import static com.firstapp.group10app.Other.Encryption.toHexString;

import android.util.Log;

import androidx.annotation.NonNull;

import com.firstapp.group10app.DB.Exercise;
import com.firstapp.group10app.DB.Workout;
import com.firstapp.group10app.Other.Index;
import com.firstapp.group10app.Other.Session;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * This class provides helper methods for interacting with the online database.
 * <p>
 * TODO (in the future): Mainly DatabaseManager should make calls to this class.
 *      Right now a lot of pages access this class directly.
 */
public class OnlineDbHelper {
    /**
     * Inserts a new user into the database.
     *
     * @param userDetails An array of user details to be inserted.
     */
    public static void insertUser(String[] userDetails) {
        try {
            Log.d("DBHelper.insertUser", "Beginning to go through the process of inserting a user");

            // Format the user details before passing them to the DataChecker
            DataFormatter.preCheckFormatUserDetails(userDetails);

            // Check that the user details are valid (THIS CRASHES THE APP)
//            if (!DataChecker.checkUserDetails(userDetails)) {
//                Log.d("DBHelper.insertUser", "Invalid user details");
//                throw new IllegalArgumentException("Invalid user details");
//            }

            // Format the user details (post-check)
            userDetails = DataFormatter.formatUserDetails(userDetails);

            // Create an SQL query
            StringBuilder sql = new StringBuilder();
            sql.append("INSERT INTO HealthData.Users (");
            for (int i = 0; i < userDetails.length; i++) {
                if (userDetails[i] != null && !userDetails[i].isEmpty() && !userDetails[i].equals("null") && !userDetails[i].equals(" ")) {
                    Log.d("DBHelper.insertUser", "Debugging -> userDetails[" + i + "] = " + userDetails[i]);
                    sql.append(Index.USER_DETAILS[i]);
                    sql.append(", ");
                }
            }
            sql.deleteCharAt(sql.length() - 2);
            sql.append(") VALUES (");

            for (int i = 0; i < userDetails.length; i++) {
                if (i == Index.PASSWORD) {
                    userDetails[i] = encryptPassword(userDetails[i]);
                }

                if (userDetails[i] != null && !userDetails[i].isEmpty() && !userDetails[i].equals("null") && !userDetails[i].equals(" ")) {
                    sql.append("'");
                    sql.append(userDetails[i]);
                    sql.append("', ");
                }

            }
            sql.deleteCharAt(sql.length() - 2);
            sql.append(");");

            // Execute the SQL query
            Log.d("DbHelper.class.getName()", ".insertUser: preparing to execute " + sql);

            // DO NOT CHANGE THIS LINE
            OnlineDbConnection.getInstance().executeStatement(sql.toString());
        } catch (Exception e) {
            Log.e("Error in DBHelper.insertUser()", e.toString());
            throw new RuntimeException(e);
        }
    }

    /**
     * Inserts a new workout into the database.
     *
     * @param values An array of workout details to be inserted.
     * @return The ID of the inserted workout.
     */
    public static Integer insertWorkout(String[] values) {
        try {
            StringBuilder sql = getStringBuilder("INSERT INTO HealthData.Workouts (", values, Index.WORKOUT_DETAILS);

            Integer id = null;

            // Specify that you want to retrieve generated keys
            Statement statement = getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY, Statement.RETURN_GENERATED_KEYS);

            // Execute the SQL query and get the generated keys
            statement.executeUpdate(sql.toString(), Statement.RETURN_GENERATED_KEYS);
            ResultSet resultSet = statement.getGeneratedKeys();

            if (resultSet.next()) {
                id = resultSet.getInt(1);
            }
            resultSet.close();

            return id;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Integer insertWorkout(Workout workout) {
        String[] values = workout.getWorkoutDetails();

        try {
            getStringBuilder("INSERT INTO HealthData.Workouts (", values, Index.WORKOUT_DETAILS);

            Integer id = null;

            ResultSet resultSet = getConnection().createStatement().getGeneratedKeys();
            if (resultSet.next()) {
                id = resultSet.getInt(1);
            }
            resultSet.close();

            return id;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Inserts a new exercise into the database.
     *
     * @param values    An array of exercise details to be inserted.
     * @param workoutID The ID of the workout the exercise belongs to.
     */
    public static void insertExercise(String[] values, int workoutID) {
        try {
            StringBuilder sql = getStringBuilder("INSERT INTO HealthData.Exercises (", values, Index.EXERCISE_DETAILS);

            Integer id = null;

            // Specify that you want to retrieve generated keys
            Statement statement = getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY, Statement.RETURN_GENERATED_KEYS);

            // Execute the SQL query and get the generated keys
            statement.executeUpdate(sql.toString(), Statement.RETURN_GENERATED_KEYS);
            ResultSet resultSet = statement.getGeneratedKeys();

            if (resultSet.next()) {
                id = resultSet.getInt(1);
            }
            resultSet.close();

            OnlineDbConnection.getInstance().executeStatement("INSERT INTO HealthData.ExerciseWorkoutPairs (WorkoutID, ExerciseID) VALUE (" + workoutID + ", " + id + ");");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @NonNull
    private static StringBuilder getStringBuilder(String str, String[] values, String[] exerciseDetails) {
        StringBuilder sql = new StringBuilder();
        sql.append(str);

        for (int i = 0; i < values.length; i++) {
            sql.append(exerciseDetails[i]);
            sql.append(", ");
        }

        sql.deleteCharAt(sql.length() - 2);
        sql.append(") VALUES (");

        for (String field : values) {
            sql.append("'");
            sql.append(field);
            sql.append("', ");
        }

        sql.deleteCharAt(sql.length() - 2);
        sql.append(");");
        return sql;
    }

    /**
     * Retrieves a user from the database based on their email.
     *
     * @param email The email of the user to retrieve.
     * @return A ResultSet containing the user's details.
     */
    public static ResultSet getUser(String email) {
        try {
            // Create and SQL query
            String sql = "SELECT * FROM HealthData.Users WHERE Email = '" +
                    email +
                    "';";

            // Execute the SQL query`
            return OnlineDbConnection.getInstance().executeQuery(sql);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String getUserName(String email) {
        try {
            // Create and SQL query
            String sql = "SELECT PreferredName FROM HealthData.Users WHERE Email = '" +
                    email +
                    "';";

            // Execute the SQL query`
            ResultSet resultSet = OnlineDbConnection.getInstance().executeQuery(sql);
            if (resultSet.next()) {
                return resultSet.getString("PreferredName");
            } else {
                return "";
            }
        } catch (Exception e) {
            Log.e("OnlineDBHelper.getUserName()", "An error occurred while trying to get the user's name. '' string is returned");
            Log.e("Error in DBHelper.getUserName()", e.toString());

            return "";
        }
    }

    /**
     * Checks if a user exists in the database based on their email.
     *
     * @param email The email of the user to check.
     * @return True if the user exists, false otherwise.
     * @throws SQLException If a database access error occurs.
     */
    public static boolean checkUserExists(String email) throws SQLException {
        String query = "SELECT * FROM HealthData.Users WHERE Email = '" +
                email +
                "';";

        return OnlineDbConnection.getInstance().executeQuery(query).next();
    }

    /**
     * Checks if a user with the following email and password exists.
     *
     * @param email    The email of the user to check.
     * @param password The password of the user to check.
     * @return True if the user exists, false otherwise.
     * @throws Exception If a database access error occurs.
     */
    public boolean checkUserExistsAndCorrectPassword(String email, String password) throws Exception {
        ResultSet result = OnlineDbConnection.getInstance().executeQuery("SELECT * FROM HealthData.Users WHERE Email = '" + email + "' AND Password = '" + encryptPassword(password) + "'");

        int size = 0;
        if (result.last()) {
            size++;
        }

        return size != 0;
    }

    /**
     * Updates a user's data in the database.
     *
     * @param toUpdate The field to update.
     * @param value    The new value for the field.
     */
    public static void updateUserData(String toUpdate, String value) {
        if (toUpdate.equals("Password")) {
            try {
                value = encryptPassword(value);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        OnlineDbConnection.getInstance().executeStatement("UPDATE HealthData.Users SET " + toUpdate + " = '" + value + "' WHERE Email = '" + Session.getUserEmail() + "'");
    }

    /**
     * Deletes a user from the database based on their email.
     *
     * @param email The email of the user to delete.
     */
    public static void deleteUser(String email) {
        OnlineDbConnection.getInstance().executeStatement("DELETE FROM HealthData.Users WHERE Email = '" + email + "'");
    }

    /**
     * Links an exercise to a workout in the database.
     *
     * @param workoutID  The ID of the workout.
     * @param exerciseID The ID of the exercise.
     */
    public static void linkExerciseToWorkout(int workoutID, int exerciseID) {
        OnlineDbConnection.getInstance().executeStatement("INSERT INTO HealthData.ExerciseWorkoutPairs (WorkoutID, ExerciseID) VALUES ('" + workoutID + "','" + exerciseID + "')");
    }

    /**
     * Retrieves all workouts and associate exercises from the database.
     * To achieve this, the method uses a complex SQL query to join the Workouts, ExerciseWorkoutPairs,
     * and Exercises tables.
     *
     * @param filter An optional filter to apply to the workouts.
     * @return A JSON string containing the workouts.
     */
    public static String getWorkoutsAsJsonArray(String filter) {
        String query = "SELECT\n" +
                "  JSON_ARRAYAGG(\n" +
                "    JSON_OBJECT(\n" +
                "      'WorkoutID', w.WorkoutID,\n" +
                "      'WorkoutName', w.WorkoutName,\n" +
                "      'WorkoutDuration', w.WorkoutDuration,\n" +
                "      'TargetMuscleGroup', w.TargetMuscleGroup,\n" +
                "      'Equipment', w.Equipment,\n" +
                "      'Difficulty', w.Difficulty,\n" +
                "      'Exercises', (\n" +
                "        SELECT JSON_ARRAYAGG(\n" +
                "          JSON_OBJECT(\n" +
                "            'ExerciseID', e.ExerciseID,\n" +
                "            'ExerciseName', e.ExerciseName,\n" +
                "            'Description', e.Description,\n" +
                "            'Illustration', e.Illustration,\n" +
                "            'TargetMuscleGroup', e.TargetMuscleGroup,\n" +
                "            'Equipment', e.Equipment,\n" +
                "            'Difficulty', e.Difficulty,\n" +
                "            'Sets', e.Sets,\n" +
                "            'Reps', e.Reps,\n" +
                "            'Time', e.Time\n" +
                "          )\n" +
                "        )\n" +
                "        FROM HealthData.ExerciseWorkoutPairs ewp\n" +
                "        JOIN HealthData.Exercises e ON ewp.ExerciseID = e.ExerciseID\n" +
                "        WHERE ewp.WorkoutID = w.WorkoutID\n" +
                "      )\n" +
                "    )\n" +
                "  ) AS Result\n" +
                "FROM\n" +
                "  HealthData.Workouts w ";

        if (filter != null) {
            query += filter;
        }
        query += ";";

        ResultSet resultSet = OnlineDbConnection.getInstance().executeQuery(query);

        try {
            if (resultSet.next()) {
                return resultSet.getString("Result");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error processing ResultSet", e);
        }

        return "";
    }

    /**
     * Retrieves all exercises from the database.
     *
     * @return A list of Exercise objects.
     */
    public static List<Exercise> getAllExercises() {
        ResultSet resultSet = getResultSet();

        List<Exercise> exercises = new ArrayList<>();

        try {
            if (resultSet.next()) {
                String result = resultSet.getString("Result");
                JSONArray jsonArray = new JSONArray(result);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    long id = jsonObject.getLong("ExerciseID");
                    String name = jsonObject.getString("ExerciseName");
                    String description = jsonObject.getString("Description");
                    String illustration = jsonObject.getString("Illustration");
                    String targetMuscleGroup = jsonObject.getString("TargetMuscleGroup");
                    String equipment = jsonObject.getString("Equipment");
                    String difficulty = jsonObject.getString("Difficulty");

                    Object sets = jsonObject.get("Sets");
                    Object reps = jsonObject.get("Reps");
                    Object time = jsonObject.get("Time");

                    Integer setsInt = null;
                    Integer repsInt = null;
                    Integer timeInt = null;

                    if (sets instanceof Integer) {
                        setsInt = (Integer) sets;
                    }
                    if (reps instanceof Integer) {
                        repsInt = (Integer) reps;
                    }
                    if (time instanceof Integer) {
                        timeInt = (Integer) time;
                    }

                    Exercise exercise = new Exercise(id, name, description, illustration, targetMuscleGroup, equipment, difficulty, setsInt, repsInt, timeInt);
                    exercises.add(exercise);
                }
            }
        } catch (SQLException | JSONException e) {
            throw new RuntimeException("Error processing ResultSet", e);
        }

        return exercises;
    }

    private static ResultSet getResultSet() {
        String query = "SELECT JSON_ARRAYAGG(\n" +
                "          JSON_OBJECT(\n" +
                "            'ExerciseID', e.ExerciseID,\n" +
                "            'ExerciseName', e.ExerciseName,\n" +
                "            'Description', e.Description,\n" +
                "            'Illustration', e.Illustration,\n" +
                "            'TargetMuscleGroup', e.TargetMuscleGroup,\n" +
                "            'Equipment', e.Equipment,\n" +
                "            'Difficulty', e.Difficulty,\n" +
                "            'Sets', e.Sets,\n" +
                "            'Reps', e.Reps,\n" +
                "            'Time', e.Time\n" +
                "          )\n" +
                "        ) AS Result\n" +
                "FROM HealthData.Exercises e";

        query += ";";

        return OnlineDbConnection.getInstance().executeQuery(query);
    }

    /**
     * Retrieves a user's workouts from the database.
     *
     * @param filter The email of the user to retrieve workouts for.
     * @return A JSON string containing the user's workouts.
     */
    public static String getUserWorkouts(String filter) {
        String query = "SELECT " +
                "JSON_ARRAYAGG(" +
                "  JSON_OBJECT(" +
                "    'WorkoutID', w.WorkoutID," +
                "    'WorkoutName', w.WorkoutName," +
                "    'WorkoutDuration', w.WorkoutDuration," +
                "    'TargetMuscleGroup', w.TargetMuscleGroup," +
                "    'Equipment', w.Equipment," +
                "    'Difficulty', w.Difficulty," +
                "    'Exercises', (" +
                "      SELECT JSON_ARRAYAGG(" +
                "        JSON_OBJECT(" +
                "          'ExerciseID', e.ExerciseID," +
                "          'ExerciseName', e.ExerciseName," +
                "          'Description', e.Description," +
                "          'Illustration', e.Illustration," +
                "          'TargetMuscleGroup', e.TargetMuscleGroup," +
                "          'Equipment', e.Equipment," +
                "          'Difficulty', e.Difficulty" +
                "        )" +
                "      )" +
                "      FROM HealthData.ExerciseWorkoutPairs ewp" +
                "      JOIN HealthData.Exercises e ON ewp.ExerciseID = e.ExerciseID" +
                "      WHERE ewp.WorkoutID = w.WorkoutID" +
                "    )" +
                "  )" +
                ") AS Result" +
                " FROM" +
                "   HealthData.Workouts w" +
                " JOIN HealthData.UserWorkoutHistory uwh ON w.WorkoutID = uwh.WorkoutID" +
                " WHERE uwh.Email = '" + filter + "'" +
                " ORDER BY uwh.Date DESC";

        ResultSet resultSet = OnlineDbConnection.getInstance().executeQuery(query);

        try {
            if (resultSet.next()) {
                return resultSet.getString("Result");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error processing ResultSet", e);
        }

        return "";
    }

    /**
     * Inserts a new workout history record into the database.
     */
    public static void insertHistory(int minutes) {
        String sqlHistory = "INSERT INTO HealthData.UserWorkoutHistory (Email, WorkoutID, Time, Date, Duration) VALUES (" +
                "'" + Session.getUserEmail() + "', " +
                "'" + Session.getWorkoutID() + "', " +
                "CURRENT_TIME(), " +
                "CURRENT_DATE(), " +
                minutes + ");";
        OnlineDbConnection.getInstance().executeStatement(sqlHistory);
    }

    /**
     * Obtain total amount of workouts completed by the user.
     */
    public static Integer getTotalinHistory(String email){
        String query = "SELECT COUNT(*) AS total FROM HealthData.UserWorkoutHistory uwh " +
                " WHERE uwh.Email = '" + email + "';";

        OnlineDbConnection db = new OnlineDbConnection();
        ResultSet out = db.executeQuery(query);

        Integer total = null;

        try {
            if (out.next()) {
                total = out.getInt("total");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error processing ResultSet", e);
        }
        return total;
    }

    /**
     * Obtain total amount of time spent doing exercise by the user
     */
    public static Integer getTotalMinutesFromHistory(String email){
        String query = "SELECT SUM(Duration) AS total_duration FROM HealthData.UserWorkoutHistory uwh " +
                " WHERE uwh.Email = '"+ email +"';";

        OnlineDbConnection db = new OnlineDbConnection();
        ResultSet out = db.executeQuery(query);

        Integer total = null;

        try {
            if(out.next()) {
                total = out.getInt("total_duration");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error processing ResultSet", e);
        }
        return total;
    }

    /**
     * Retrieves a limited (4) number of a user's workouts from the database.
     *
     * @param filter The email of the user to retrieve workouts for.
     * @return A JSON string containing the user's workouts.
     */
    public static String getUserWorkoutsLimited(String filter) {
        String query = "SELECT " +
                "JSON_ARRAYAGG(" +
                "  JSON_OBJECT(" +
                "    'WorkoutID', w.WorkoutID," +
                "    'WorkoutName', w.WorkoutName," +
                "    'WorkoutDuration', w.WorkoutDuration," +
                "    'TargetMuscleGroup', w.TargetMuscleGroup," +
                "    'Equipment', w.Equipment," +
                "    'Difficulty', w.Difficulty," +
                "    'Exercises', (" +
                "      SELECT JSON_ARRAYAGG(" +
                "        JSON_OBJECT(" +
                "          'ExerciseID', e.ExerciseID," +
                "          'ExerciseName', e.ExerciseName," +
                "          'Description', e.Description," +
                "          'Illustration', e.Illustration," +
                "          'TargetMuscleGroup', e.TargetMuscleGroup," +
                "          'Equipment', e.Equipment," +
                "          'Difficulty', e.Difficulty" +
                "        )" +
                "      )" +
                "      FROM HealthData.ExerciseWorkoutPairs ewp" +
                "      JOIN HealthData.Exercises e ON ewp.ExerciseID = e.ExerciseID" +
                "      WHERE ewp.WorkoutID = w.WorkoutID" +
                "    )" +
                "  )" +
                ") AS Result" +
                " FROM" +
                "   HealthData.Workouts w" +
                " JOIN HealthData.UserWorkoutHistory uwh ON w.WorkoutID = uwh.WorkoutID" +
                " WHERE uwh.Email = '" + filter + "'" +
                " ORDER BY uwh.Date DESC" +
                " LIMIT 4";

        ResultSet resultSet = OnlineDbConnection.getInstance().executeQuery(query);

        try {
            if (resultSet.next()) {
                return resultSet.getString("Result");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error processing ResultSet", e);
        }

        return "";
    }

    /**
     * Encrypts a password using SHA encryption.
     *
     * @param password The password to encrypt.
     * @return The encrypted password.
     * @throws Exception If an error occurs during encryption.
     */
    public static String encryptPassword(String password) throws Exception {
        return toHexString(getSHA(password));
    }

    public static void changeUserPasswordVerifyCode(String validate, String emailText) {
        String statement = "UPDATE HealthData.Users " +
                "SET VerifyCode = '" + validate + "' " +
                "WHERE Email = '" + emailText + "';";

        OnlineDbConnection.getInstance().executeStatement(statement);
    }

    public static String getVerifyCode(String email) {
        try {
            return OnlineDbConnection.getInstance().executeQuery("SELECT VerifyCode FROM HealthData.Users WHERE Email = '" + email + "'").getString("VerifyCode");
        } catch (SQLException e) {
            // TODO: Handle this exception better
            throw new RuntimeException(e);
        }
    }

    /**
     * Changes a user's password in the database.
     *
     * @param email    The email of the user to change the password for.
     * @param password The new password.
     */
    public static void changeUserPassword(String email, String password) {
        try {
            OnlineDbConnection.getInstance().executeStatement("UPDATE HealthData.Users SET Password = '" + encryptPassword(password) + "' WHERE Email = '" + email + "';");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}