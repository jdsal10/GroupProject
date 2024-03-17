package com.firstapp.group10app.DB;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class Exercise {
    private long id;
    private String name;
    private String description;
    private String illustration;
    private String targetMuscleGroup;
    private String equipment;
    private String difficulty;
    private int sets;
    private int reps;
    private int time;

    public Exercise(long id, String name, String description, String illustration, String targetMuscleGroup, String equipment, String difficulty, int sets, int reps, int time) {
        this.setId(id);
        this.setName(name);
        this.setDescription(description);
        this.setIllustration(illustration);
        this.setTargetMuscleGroup(targetMuscleGroup);
        this.setEquipment(equipment);
        this.setDifficulty(difficulty);
        this.setSets(sets);
        this.setReps(reps);
        this.setTime(time);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public int getSets() {
        return sets;
    }

    public void setSets(int sets) {
        this.sets = sets;
    }

    public int getReps() {
        return reps;
    }

    public void setReps(int reps) {
        this.reps = reps;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public static String exercisesToJsonString(List<Exercise> exercises) {
        JSONArray jsonArray = new JSONArray();
        for (Exercise exercise : exercises) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("ExerciseID", exercise.getId());
                jsonObject.put("ExerciseName", exercise.getName());
                jsonObject.put("Description", exercise.getDescription());
                jsonObject.put("Illustration", exercise.getIllustration());
                jsonObject.put("TargetMuscleGroup", exercise.getTargetMuscleGroup());
                jsonObject.put("Equipment", exercise.getEquipment());
                jsonObject.put("Difficulty", exercise.getDifficulty());
                jsonObject.put("Sets", exercise.getSets());
                jsonObject.put("Reps", exercise.getReps());
                jsonObject.put("Time", exercise.getTime());
                jsonArray.put(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return jsonArray.toString();
    }
}