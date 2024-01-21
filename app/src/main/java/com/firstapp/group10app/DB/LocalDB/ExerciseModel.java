package com.firstapp.group10app.DB.LocalDB;

public class ExerciseModel {
    private int exerciseID;
    private String exerciseName;
    private String description;
    private String illustration;
    private String targetMuscleGroup;
    private String equipment;
    private int difficulty;

    public ExerciseModel(int exerciseID, String exerciseName, String description, String illustration, String targetMuscleGroup, String equipment, int difficulty) {
        this.exerciseID = exerciseID;
        this.exerciseName = exerciseName;
        this.description = description;
        this.illustration = illustration;
        this.targetMuscleGroup = targetMuscleGroup;
        this.equipment = equipment;
        this.difficulty = difficulty;
    }

    public int getExerciseID() {
        return exerciseID;
    }

    public void setExerciseID(int exerciseID) {
        this.exerciseID = exerciseID;
    }

    public String getExerciseName() {
        return exerciseName;
    }

    public void setExerciseName(String exerciseName) {
        this.exerciseName = exerciseName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIllustration() {
        return illustration;
    }

    public void setIllustration(String illustration) {
        this.illustration = illustration;
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
        return "ExerciseModel{" +
                "exerciseID=" + exerciseID +
                ", exerciseName='" + exerciseName + '\'' +
                ", description='" + description + '\'' +
                ", illustration='" + illustration + '\'' +
                ", targetMuscleGroup='" + targetMuscleGroup + '\'' +
                ", equipment='" + equipment + '\'' +
                ", difficulty=" + difficulty +
                '}';
    }
}