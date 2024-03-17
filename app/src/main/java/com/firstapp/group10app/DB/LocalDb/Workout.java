package com.firstapp.group10app.DB.LocalDb;

public class Workout {
    private long id;
    private String workoutName;
    private int duration;
    private String targetMuscleGroup;
    private String equipment;
    private String difficulty;

    public Workout(long id, String workoutName, int duration, String targetMuscleGroup, String equipment, String difficulty) {
        this.setId(id);
        this.setWorkoutName(workoutName);
        this.setDuration(duration);
        this.setTargetMuscleGroup(targetMuscleGroup);
        this.setEquipment(equipment);
        this.setDifficulty(difficulty);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getWorkoutName() {
        return workoutName;
    }

    public void setWorkoutName(String workoutName) {
        this.workoutName = workoutName;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
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

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }
}
