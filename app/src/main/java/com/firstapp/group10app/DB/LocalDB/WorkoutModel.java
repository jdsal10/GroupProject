package com.firstapp.group10app.DB.LocalDB;

public class WorkoutModel {
    private int workoutID;
    private String workoutName;
    private int workoutDuration;
    private String targetMuscleGroup;
    private String equipment;
    private int difficulty;

    public WorkoutModel(int workoutID, String workoutName, int workoutDuration, String targetMuscleGroup, String equipment, int difficulty) {
        this.workoutID = workoutID;
        this.workoutName = workoutName;
        this.workoutDuration = workoutDuration;
        this.targetMuscleGroup = targetMuscleGroup;
        this.equipment = equipment;
        this.difficulty = difficulty;
    }

    public int getWorkoutID() {
        return workoutID;
    }

    public void setWorkoutID(int workoutID) {
        this.workoutID = workoutID;
    }

    public String getWorkoutName() {
        return workoutName;
    }

    public void setWorkoutName(String workoutName) {
        this.workoutName = workoutName;
    }

    public int getWorkoutDuration() {
        return workoutDuration;
    }

    public void setWorkoutDuration(int workoutDuration) {
        this.workoutDuration = workoutDuration;
    }

    public String getTargetMuscleGroup() {
        return targetMuscleGroup;
    }

    public void setTargetMuscleGroup(String targetMuscleGroup) {
        this.targetMuscleGroup = targetMuscleGroup;
    }

    public String getEquipment() {
        return equipment;
    }

    public void setEquipment(String equipment) {
        this.equipment = equipment;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    @Override
    public String toString() {
        return "WorkoutModel{" +
                "workoutID=" + workoutID +
                ", workoutName='" + workoutName + '\'' +
                ", workoutDuration=" + workoutDuration +
                ", targetMuscleGroup='" + targetMuscleGroup + '\'' +
                ", equipment='" + equipment + '\'' +
                ", difficulty=" + difficulty +
                '}';
    }
}