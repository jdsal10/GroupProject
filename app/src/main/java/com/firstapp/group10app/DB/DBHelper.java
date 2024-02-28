package com.firstapp.group10app.DB;

import static com.firstapp.group10app.DB.DBConnection.conn;

import com.firstapp.group10app.Other.Index;
import com.firstapp.group10app.Other.Session;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;

public class DBHelper {
    public static void insertUser(String[] userDetails) {
        try {
            // Format the user details before passing them to the DataChecker
            DataFormatter.preCheckFormatUserDetails(userDetails);
            System.out.println(Arrays.toString(userDetails));

            // Check that the user details are valid
//            if (!DataChecker.checkUserDetails(userDetails)) {
//                throw new IllegalArgumentException("Invalid user details");
//            }

            // Format the user details (post-check)
            userDetails = DataFormatter.formatUserDetails(userDetails);

            // Create and SQL query
            StringBuilder sql = new StringBuilder();
            sql.append("INSERT INTO HealthData.Users (");
            for (int i = 0; i < userDetails.length; i++) {
//                if (userDetails[i] != null) {
                sql.append(Index.USER_DETAILS[i]);
                sql.append(", ");
//                }
            }
            sql.deleteCharAt(sql.length() - 2);
            sql.append(") VALUES (");

            for (String field : userDetails) {
                sql.append("'");
                sql.append(field);
                sql.append("', ");
            }
            sql.deleteCharAt(sql.length() - 2);
            sql.append(");");

            // Execute the SQL query
            System.out.println(sql);
            DBConnection d = new DBConnection();
            d.executeStatement(sql.toString());
        } catch (Exception e) {
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

            ResultSet rs = st.getGeneratedKeys();
            if (rs.next()) {
                id = rs.getInt(1);
            }
            rs.close();

            DBConnection db = new DBConnection();
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
            return DBConnection.executeQuery(sql);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // Returns true if the user exists in the database
    public static boolean checkExists(String email) throws SQLException {
        String st = "SELECT * FROM HealthData.Users WHERE Email = '" +
                email +
                "';";
        DBConnection con = new DBConnection();
        return con.executeQuery(st).next();
    }

    // Checks if a user exists
    public boolean checkUser(String email, String password) throws SQLException {
        DBConnection db = new DBConnection();
        ResultSet result = db.executeQuery("SELECT * FROM HealthData.Users WHERE Email = '" + email + "' AND Password = '" + password + "'");
        int size = 0;

        if (result.last()) {
            size++;
        }

        return size != 0;
    }

    public static void clearData(String toDelete) {
        DBConnection.executeStatement("UPDATE HealthData.Users SET " + toDelete + " = NULL WHERE Email = '" + Session.userEmail + "'");
    }

    public static void updateData(String toUpdate, String value) {
        DBConnection.executeStatement("UPDATE HealthData.Users SET " + toUpdate + " = '" + value + "' WHERE Email = '" + Session.userEmail + "'");
    }

    public void deleteUser(String email) {
        DBConnection.executeStatement("DELETE FROM HealthData.Users WHERE Email = '" + email + "'");
    }

    public static void linkExercise(int workoutID, int exerciseID) {
        DBConnection.executeStatement("INSERT INTO HealthData.ExerciseWorkoutPairs (WorkoutID, ExerciseID) VALUES ('" + workoutID + "','" + exerciseID + "')");
    }

    public static String getAllWorkouts(String filter) {
        DBConnection d = new DBConnection();

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

        ResultSet q = d.executeQuery(out);

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
        DBConnection d = new DBConnection();

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

        ResultSet q = d.executeQuery(out);

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
        DBConnection d = new DBConnection();
        String st = "SELECT\n" +
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
                "  HealthData.Workouts w\n" +
                "WHERE w.WorkoutID IN (\n" +
                "  SELECT uwh.WorkoutID\n" +
                "  FROM HealthData.UserWorkoutHistory uwh\n" +
                "  WHERE uwh.Email = '" + filter + "'\n" +
                ");\n";

        ResultSet out = d.executeQuery(st);
        try {
            if (out.next()) {
                return out.getString("Result");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error processing ResultSet", e);
        }

        return "";
    }

    public String encrypt(String password, int dif) {
        StringBuilder result = new StringBuilder();
        for (char character : password.toCharArray()) {
            if (character != ' ') {
                int originalAlphabetPosition = character - 'a';
                int newAlphabetPosition = (originalAlphabetPosition + dif) % 26;
                char newCharacter = (char) ('a' + newAlphabetPosition);
                result.append(newCharacter);
            } else {
                result.append(character);
            }
        }
        return String.valueOf(result);
    }

    public String decrypt(String password, int dif) {
        return encrypt(password, 26 - (dif % 26));
    }

    public static String getUserWorkoutsLimited(String filter) {
        DBConnection d = new DBConnection();
        String st = "SELECT\n" +
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
                "            'Difficulty', e.Difficulty\n" +
                "          )\n" +
                "        )\n" +
                "        FROM HealthData.ExerciseWorkoutPairs ewp\n" +
                "        JOIN HealthData.Exercises e ON ewp.ExerciseID = e.ExerciseID\n" +
                "        WHERE ewp.WorkoutID = w.WorkoutID\n" +
                "      )\n" +
                "    )\n" +
                "  ) AS Result\n" +
                "FROM\n" +
                "  HealthData.Workouts w\n" +
                "WHERE w.WorkoutID IN (\n" +
                "  SELECT uwh.WorkoutID\n" +
                "  FROM HealthData.UserWorkoutHistory uwh\n" +
                "  WHERE uwh.Email = '" + filter + "'\n" +
                "  ORDER BY uwh.WorkoutID DESC\n" +
                "  LIMIT 4" +
                ");\n";

        ResultSet out = d.executeQuery(st);
        try {
            if (out.next()) {
                return out.getString("Result");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error processing ResultSet", e);
        }

        return "";
    }
}



