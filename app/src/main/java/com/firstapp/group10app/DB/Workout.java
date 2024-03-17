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

    public static String workoutsToJsonString(List<Workout> workouts) {
        JSONArray jsonArray = new JSONArray();
        for (Workout workout : workouts) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("id", workout.getId());
                jsonObject.put("workoutName", workout.getWorkoutName());
                jsonObject.put("duration", workout.getDuration());
                jsonObject.put("targetMuscleGroup", workout.getTargetMuscleGroup());
                jsonObject.put("equipment", workout.getEquipment());
                jsonObject.put("difficulty", workout.getDifficulty());
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            jsonArray.put(jsonObject);
        }
        return jsonArray.toString();
    }
}