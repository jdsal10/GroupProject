package com.firstapp.group10app.Other;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ExerciseAdapter extends FragmentStateAdapter {

    private final JSONArray exerciseArray;

    public ExerciseAdapter(FragmentActivity fragmentActivity, JSONArray exerciseArray) {
        super(fragmentActivity);
        this.exerciseArray = exerciseArray;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        try {
            JSONObject exercise = exerciseArray.getJSONObject(position);
            return ActiveExerciseUpdate.newInstance(
                    exercise.getString("Description"),
                    exercise.getString("Sets"),
                    exercise.getString("Reps"),
                    exercise.getString("Time")
            );
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int getItemCount() {
        return exerciseArray.length();
    }
}
