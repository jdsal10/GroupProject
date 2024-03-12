package com.firstapp.group10app.Other;

/**
 * This class contains values that other classes often reference.
 */
public class Index {
    // Indexes for the user details array (sent from the registration page)
    public static final int EMAIL = 0, NAME = 1, PASSWORD = 2, DOB = 3, WEIGHT = 4, HEIGHT = 5, SEX = 6, CONDITIONS = 7, REASONS = 8;
    public static final String[] USER_DETAILS = {"Email", "PreferredName", "Password", "DOB", "Weight", "Height", "Sex", "HealthCondition", "ReasonForDownloading"};
    public static final String[] WORKOUT_DETAILS = {"WorkoutName", "WorkoutDuration", "TargetMuscleGroup", "Equipment", "Difficulty"};
    public static final String[] EXERCISE_DETAILS = {"ExerciseName", "Description", "TargetMuscleGroup", "Equipment", "Difficulty"};
}