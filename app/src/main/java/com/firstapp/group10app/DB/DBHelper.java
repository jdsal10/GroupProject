package com.firstapp.group10app.DB;

import static com.firstapp.group10app.DB.DBConnection.conn;
import static com.firstapp.group10app.Other.Encryption.getSHA;
import static com.firstapp.group10app.Other.Encryption.toHexString;

import com.firstapp.group10app.Other.Index;
import com.firstapp.group10app.Other.Session;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;

public class DBHelper {
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
            System.out.println(DBHelper.class.getName() + ".insertUser: preparing to execute " + sql);

            // DO NOT CHANGE THIS LINE
            DBConnection db = new DBConnection();
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

            ResultSet rs = st.getGeneratedKeys();
            if (rs.next()) {
                id = rs.getInt(1);
            }
            rs.close();

            DBConnection db = new DBConnection();
            DBConnection.executeStatement("INSERT INTO HealthData.ExerciseWorkoutPairs (WorkoutID, ExerciseID) VALUE (" + workoutID + ", " + id + ");");
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
        return DBConnection.executeQuery(st).next();
    }

    // Checks if a user exists
    public boolean checkUser(String email, String password) throws Exception {
        ResultSet result = DBConnection.executeQuery("SELECT * FROM HealthData.Users WHERE Email = '" + email + "' AND Password = '" + encryptPassword(password) + "'");
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
        if (toUpdate.equals("Password")) {
            try {
                value = encryptPassword(value);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

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

        ResultSet q = DBConnection.executeQuery(out);

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

        ResultSet q = DBConnection.executeQuery(out);

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

        ResultSet out = DBConnection.executeQuery(st);
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

    public static void insertHistory() {
        DBConnection d = new DBConnection();
        StringBuilder sqlHistory = new StringBuilder();
        sqlHistory.append("INSERT INTO HealthData.UserWorkoutHistory (Email, WorkoutID, Date, Duration) VALUES (");
        sqlHistory.append("'" + Session.userEmail + "', ");
        sqlHistory.append("'" + Session.workoutID + "', ");
        sqlHistory.append("CURRENT_DATE(), ");
        sqlHistory.append(40 + ");");
        System.out.println(sqlHistory.toString());
        d.executeStatement(sqlHistory.toString());
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

        ResultSet out = DBConnection.executeQuery(st);
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
            DBConnection.executeStatement("UPDATE HealthData.Users SET Password = '" + encryptPassword(password) + "' WHERE Email = '" + email + "';");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}



