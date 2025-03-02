package com.firstapp.group10app.DB;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

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

    public String[] getWorkoutDetails() {
        String[] workoutDetails = new String[5];
        workoutDetails[0] = this.getWorkoutName();
        workoutDetails[1] = Integer.toString(this.getDuration());
        workoutDetails[2] = this.getTargetMuscleGroup();
        workoutDetails[3] = this.getEquipment();
        workoutDetails[4] = this.getDifficulty();
        return workoutDetails;
    }

    public static String workoutsToJsonString(List<Workout> workouts) {
        JSONArray jsonArray = new JSONArray();
        for (Workout workout : workouts) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("WorkoutID", workout.getId());
                jsonObject.put("WorkoutName", workout.getWorkoutName());
                jsonObject.put("WorkoutDuration", workout.getDuration());
                jsonObject.put("TargetMuscleGroup", workout.getTargetMuscleGroup());
                jsonObject.put("Equipment", workout.getEquipment());
                jsonObject.put("Difficulty", workout.getDifficulty());
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            jsonArray.put(jsonObject);
        }
        return jsonArray.toString();
    }
}