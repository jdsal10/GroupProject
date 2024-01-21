package com.firstapp.group10app.DB.LocalDB;

public class ExerciseWorkoutPairModel {
    private int exerciseID;
    private int workoutID;

    public ExerciseWorkoutPairModel(int exerciseID, int workoutID) {
        this.exerciseID = exerciseID;
        this.workoutID = workoutID;
    }

    public int getExerciseID() {
        return exerciseID;
    }

    public void setExerciseID(int exerciseID) {
        this.exerciseID = exerciseID;
    }

    public int getWorkoutID() {
        return workoutID;
    }

    public void setWorkoutID(int workoutID) {
        this.workoutID = workoutID;
    }

    @Override
    public String toString() {
        return "ExerciseWorkoutPairModel{" +
                "exerciseID=" + exerciseID +
                ", workoutID=" + workoutID +
                '}';
    }
}