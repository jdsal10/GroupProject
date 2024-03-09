package com.firstapp.group10app.DB;

import static com.firstapp.group10app.DB.DbConnection.conn;
import static com.firstapp.group10app.Other.Encryption.getSHA;
import static com.firstapp.group10app.Other.Encryption.toHexString;

import com.firstapp.group10app.Other.Index;
import com.firstapp.group10app.Other.Session;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DbHelper {
    public static void insertUser(String[] userDetails) {
        try {
            System.out.println("DBHelper.insertUser: Beginning to go through the process of inserting a user");

            // Format the user details before passing them to the DataChecker
            DataFormatter.preCheckFormatUserDetails(userDetails);

            // Check that the user details are valid (THIS CRASHES THE APP)
//            if (!DataChecker.checkUserDetails(userDetails)) {
//                System.out.println("DBHelper.insertUser: Invalid user details");
//                throw new IllegalArgumentException("Invalid user details");
//            }

            // Format the user details (post-check)
            userDetails = DataFormatter.formatUserDetails(userDetails);

            // Create an SQL query
            StringBuilder sql = new StringBuilder();
            sql.append("INSERT INTO HealthData.Users (");
            for (int i = 0; i < userDetails.length; i++) {
                if (userDetails[i] != null && !userDetails[i].equals("") && !userDetails[i].equals("null") && !userDetails[i].equals(" ")) {
                    System.out.println("DBHelper.insertUser: Debugging -> userDetails[" + i + "] = " + userDetails[i]);
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

                if (userDetails[i] != null && !userDetails[i].equals("") && !userDetails[i].equals("null") && !userDetails[i].equals(" ")) {
                    sql.append("'");
                    sql.append(userDetails[i]);
                    sql.append("', ");
                }

            }
            sql.deleteCharAt(sql.length() - 2);
            sql.append(");");

            // Execute the SQL query
            System.out.println(DbHelper.class.getName() + ".insertUser: preparing to execute " + sql);

            // DO NOT CHANGE THIS LINE
            DbConnection db = new DbConnection();
            db.executeStatement(sql.toString());
        } catch (Exception e) {
            System.out.println("Error in DBHelper.insertUser(): " + e);
            throw new RuntimeException(e);
        }
    }

    public static Integer insertWorkout(String[] values) {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("INSERT INTO HealthData.Workouts (");
            for (int i = 0; i < values.length; i++) {
                sql.append(Index.WORKOUT_DETAILS[i]);
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

            Integer id = null;
            Statement st = conn.createStatement();
            Integer test = st.executeUpdate(sql.toString(), Statement.RETURN_GENERATED_KEYS);

            ResultSet rs = st.getGeneratedKeys();
            if (rs.next()) {
                id = rs.getInt(1);
            }
            rs.close();

            return id;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void insertExercise(String[] values, int workoutID) {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("INSERT INTO HealthData.Exercises (");

            for (int i = 0; i < values.length; i++) {
                sql.append(Index.EXERCISE_DETAILS[i]);
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

            Integer id = null;
            Statement st = conn.createStatement();

            DbConnection db = new DbConnection();
            System.out.println("INSERTING!!!");
            db.executeStatement(String.valueOf(sql));

            Integer test = st.executeUpdate(sql.toString(), Statement.RETURN_GENERATED_KEYS);

            ResultSet rs = st.getGeneratedKeys();
            if (rs.next()) {
                id = rs.getInt(1);
            }
            rs.close();

            db.executeStatement("INSERT INTO HealthData.ExerciseWorkoutPairs (WorkoutID, ExerciseID) VALUE (" + workoutID + ", " + id + ");");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static ResultSet getUser(String email) {
        try {
            // Create and SQL query
            String sql = "SELECT * FROM HealthData.Users WHERE Email = '" +
                    email +
                    "';";

            // Execute the SQL query`
            DbConnection db = new DbConnection();
            return db.executeQuery(sql);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // Returns true if the user exists in the database
    public static boolean checkExists(String email) throws SQLException {
        String st = "SELECT * FROM HealthData.Users WHERE Email = '" +
                email +
                "';";
        DbConnection db = new DbConnection();
        return db.executeQuery(st).next();
    }

    // Checks if a user exists
    public boolean checkUser(String email, String password) throws Exception {
        DbConnection db = new DbConnection();
        ResultSet result = db.executeQuery("SELECT * FROM HealthData.Users WHERE Email = '" + email + "' AND Password = '" + encryptPassword(password) + "'");
        int size = 0;

        if (result.last()) {
            size++;
        }

        return size != 0;
    }


    public static void updateData(String toUpdate, String value) {
        if (toUpdate.equals("Password")) {
            try {
                value = encryptPassword(value);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        DbConnection db = new DbConnection();
        db.executeStatement("UPDATE HealthData.Users SET " + toUpdate + " = '" + value + "' WHERE Email = '" + Session.userEmail + "'");
    }

    public void deleteUser(String email) {
        DbConnection db = new DbConnection();
        db.executeStatement("DELETE FROM HealthData.Users WHERE Email = '" + email + "'");
    }

    public static void linkExercise(int workoutID, int exerciseID) {
        DbConnection db = new DbConnection();
        db.executeStatement("INSERT INTO HealthData.ExerciseWorkoutPairs (WorkoutID, ExerciseID) VALUES ('" + workoutID + "','" + exerciseID + "')");
    }

    public static String getAllWorkouts(String filter) {
        String out = "SELECT\n" +
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
            out += filter;
        }
        out += ";";

        DbConnection db = new DbConnection();
        ResultSet q = db.executeQuery(out);

        try {
            if (q.next()) {
                return q.getString("Result");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error processing ResultSet", e);
        }

        return "";
    }

    public static String getAllExercises() {
        String out = "SELECT JSON_ARRAYAGG(\n" +
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

        out += ";";

        DbConnection db = new DbConnection();
        ResultSet q = db.executeQuery(out);

        try {
            if (q.next()) {
                return q.getString("Result");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error processing ResultSet", e);
        }

        return "";
    }

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
                " WHERE uwh.Email = '" + filter + "'";

        DbConnection db = new DbConnection();
        ResultSet out = db.executeQuery(query);

        try {
            if (out.next()) {
                return out.getString("Result");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error processing ResultSet", e);
        }

        return "";
    }


    public static void insertHistory() {
        DbConnection d = new DbConnection();
        String sqlHistory = "INSERT INTO HealthData.UserWorkoutHistory (Email, WorkoutID, Time, Date, Duration) VALUES (" +
                "'" + Session.userEmail + "', " +
                "'" + Session.workoutID + "', " +
                "CURRENT_TIME(), " +
                "CURRENT_DATE(), " +
                // Add with correct duration.
                40 + ");";
        d.executeStatement(sqlHistory);
    }

    public static String getUserWorkoutsLimited(String filter) {
        String query = "SELECT " +
                "JSON_ARRAYAGG(" +
//                "JSON_ARRAY(" +
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
                " LIMIT 4";

        DbConnection db = new DbConnection();
        ResultSet out = db.executeQuery(query);

        try {
            if (out.next()) {
                return out.getString("Result");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error processing ResultSet", e);
        }

        return "";
    }

    public static String encryptPassword(String password) throws Exception {
        return toHexString(getSHA(password));
    }

    public static void changeUserPassword(String email, String password) {
        try {
            DbConnection.executeStatement("UPDATE HealthData.Users SET Password = '" + encryptPassword(password) + "' WHERE Email = '" + email + "';");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}



