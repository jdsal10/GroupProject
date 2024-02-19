package com.firstapp.group10app.Pages;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.firstapp.group10app.DB.DBHelper;
import com.firstapp.group10app.Other.JSONToDB;
import com.firstapp.group10app.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class create_workout extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener, View.OnClickListener {
    String selected = "easy";
    View easy, medium, hard;
    EditText name, duration, equipment;
    TextView durationTitle;
    Drawable border;
    Button cancel, continue1, continue2, back;
    LinearLayout p1, p2;
    Spinner target;
    ArrayList<JSONObject> addedExercises;
    ArrayList<String> addedExercisesID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_workout);

        durationTitle = findViewById(R.id.durationTitle);
        border = ContextCompat.getDrawable(this, R.drawable.selected_item);

        name = findViewById(R.id.workoutNameInput);
        duration = findViewById(R.id.workoutDurationInput);
        equipment = findViewById(R.id.workoutEquipmentInput);

        easy = findViewById(R.id.easySelect);
        medium = findViewById(R.id.mediumSelect);
        hard = findViewById(R.id.hardSelect);

        cancel = findViewById(R.id.createWorkoutCancel);
        continue1 = findViewById(R.id.createWorkoutContinue1);
        back = findViewById(R.id.backWorkout);
        continue2 = findViewById(R.id.createWorkoutContinue2);

        target = findViewById(R.id.workoutTargetInput);
        ArrayAdapter<CharSequence> adapterTarget = ArrayAdapter.createFromResource(
                this,
                R.array.targetMuscleGroup,
                android.R.layout.simple_spinner_item
        );

        adapterTarget.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        target.setAdapter(adapterTarget);
        target.setSelection(0);

        medium.setBackgroundColor(Color.parseColor("#FFFF00"));
        hard.setBackgroundColor(Color.parseColor("#FF0000"));

        easy.setOnClickListener(this);
        medium.setOnClickListener(this);
        hard.setOnClickListener(this);

        cancel.setOnClickListener(this);
        continue1.setOnClickListener(this);
        back.setOnClickListener(this);
        continue2.setOnClickListener(this);

        p1 = findViewById(R.id.page1);
        p2 = findViewById(R.id.page2);

        setListeners();

        enableBorder(easy);
        border = ContextCompat.getDrawable(this, R.drawable.selected_item);

        BottomNavigationView bottomNavigationView = findViewById(R.id.mainNavigation);
        bottomNavigationView.setOnItemSelectedListener(this);
        bottomNavigationView.getMenu().findItem(R.id.goToWorkouts).setChecked(true);
    }

    public void setListeners() {
        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    name.setError("Name required");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        duration.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    duration.setError("Duration required");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        equipment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    equipment.setError("Equipment required");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    public void createExerciseView() {
        String newData = DBHelper.getAllExercises();

        ScrollView s = findViewById(R.id.exerciseSelector);
        s.removeAllViews();

        // Linear layout to hold all layouts.;
        LinearLayout exerciseHolder = new LinearLayout(this);
        exerciseHolder.setOrientation(LinearLayout.VERTICAL);

        // Creates a layout containing the exercise boxes.
        JSONArray jsonArray;
        try {
            jsonArray = new JSONArray(newData);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        boolean[] toggles = new boolean[jsonArray.length()];

        // For every exercise, we create a box containing the details.
        addedExercises = new ArrayList<>();
        addedExercisesID = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject workoutObject;

            try {
                workoutObject = jsonArray.getJSONObject(i);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            LinearLayout exerciseCombo = (LinearLayout) inflater.inflate(R.layout.exercise_combo, null);

            View t = exerciseCombo.findViewById(R.id.checkBox);

            LinearLayout exerciseBox = exerciseCombo.findViewById(R.id.exerciseDetails);

            LinearLayout.LayoutParams correctParam = new LinearLayout.LayoutParams(
                    100,
                    100
            );

            t.setLayoutParams(correctParam);

            TextView exerciseNameView = exerciseBox.findViewById(R.id.exerciseNameView);
            TextView exerciseDescriptionView = exerciseBox.findViewById(R.id.exerciseDescriptionView);
            TextView exerciseTargetMuscleGroupView = exerciseBox.findViewById(R.id.exerciseTargetMuscleGroupView);
            TextView exerciseEquipmentView = exerciseBox.findViewById(R.id.exerciseEquipmentView);

            ImageView exerciseImage = exerciseBox.findViewById(R.id.exerciseImage);
            View difficultyScale = exerciseBox.findViewById(R.id.difficulty);

            exerciseNameView.setText(String.format(workoutObject.optString("ExerciseName", "")));
            exerciseDescriptionView.setText(workoutObject.optString("Description", ""));
            exerciseTargetMuscleGroupView.setText(String.format("Exercise Target Group: %s", workoutObject.optString("TargetMuscleGroup", "")));
            exerciseEquipmentView.setText(String.format("Exercise Equipment: %s", workoutObject.optString("Equipment", "")));

            exerciseImage.setImageResource(R.drawable.workout);
            String difficultyValue = workoutObject.optString("Difficulty", "");

            switch (difficultyValue) {
                case "Easy":
                    difficultyScale.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#00FF00")));
                    break;
                case "Medium":
                    difficultyScale.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFFF00")));
                    break;
                case "Hard":
                    difficultyScale.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF0000")));
                    break;
            }
            final int index = i;


            exerciseCombo.setOnClickListener(v -> {
                if (toggles[index]) {
                    t.setBackgroundColor(Color.TRANSPARENT);
                    addedExercisesID.remove(workoutObject.optString("ExerciseID", ""));
                    addedExercises.remove(workoutObject);
                } else {
                    t.setBackgroundColor(Color.BLACK);
                    addedExercises.add(workoutObject);
                    addedExercisesID.add(workoutObject.optString("ExerciseID", ""));

                }
                toggles[index] = !toggles[index];
            });

            exerciseHolder.addView(exerciseCombo);
        }

        // Adds the Linear layout containing all boxes to the scroll view.
        exerciseHolder.setPadding(0, 0, 0, 10);
        s.addView(exerciseHolder);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.easySelect) {
            if (selected == null) {
                selected = "easy";
                enableBorder(easy);
            } else if (!selected.equals("easy")) {
                selected = "easy";
                enableBorder(easy);
                disableBorder(medium);
                disableBorder(hard);
            }
        } else if (id == R.id.mediumSelect) {
            if (selected == null) {
                selected = "medium";
                enableBorder(medium);
            } else if (!selected.equals("medium")) {
                selected = "medium";
                disableBorder(easy);
                enableBorder(medium);
                disableBorder(hard);
            }
        } else if (id == R.id.hardSelect) {
            if (selected == null) {
                selected = "hard";
                enableBorder(hard);
            } else if (!selected.equals("hard")) {
                selected = "hard";
                disableBorder(easy);
                disableBorder(medium);
                enableBorder(hard);
            }
        } else if (id == R.id.createWorkoutContinue1) {
            if ((name.getText().toString().length() == 0) || (duration.getText().toString().length() == 0) || (equipment.getText().toString().length() == 0)) {
                continue1.setError("Some fields have no info");
            } else if ((Integer.parseInt(duration.getText().toString()) > 180) || (Integer.parseInt(duration.getText().toString()) == 0)) {
                duration.setError("Duration must be between 180 and 0!");
            } else {
                p1.setVisibility(GONE);
                p2.setVisibility(VISIBLE);

                createExerciseView();
            }
        } else if (id == R.id.createWorkoutCancel) {
            // Maybe add popup to prevent user losing progress.
            startActivity(new Intent(this, workout_option.class));
        } else if (id == R.id.backWorkout) {
            p2.setVisibility(GONE);
            p1.setVisibility(VISIBLE);
        } else if (id == R.id.createWorkoutContinue2) {
            finalCheck();
        }
    }

    public void finalCheck() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();

        View dialogView = inflater.inflate(R.layout.confirm_create_workout, null);
        builder.setView(dialogView);
        AlertDialog alertDialog = builder.create();

        LinearLayout v = dialogView.findViewById(R.id.dataHolder);
        if (v != null) {
            LinearLayout l = new LinearLayout(this);
            l.setOrientation(LinearLayout.VERTICAL);

            TextView workoutName = new TextView(this);
            TextView workoutDuration = new TextView(this);
            TextView workoutTarget = new TextView(this);
            TextView workoutEquipment = new TextView(this);
            TextView workoutDifficulty = new TextView(this);

            workoutName.setText(String.format("Workout Name: %s", String.format(name.getText().toString())));
            workoutDuration.setText(String.format("Workout Duration: %s", String.format(duration.getText().toString())));
            workoutTarget.setText(String.format("Workout Target: %s", String.format(target.getSelectedItem().toString())));
            workoutEquipment.setText(String.format("Workout Equipment: %s", String.format(equipment.getText().toString())));
            workoutDifficulty.setText(String.format("Workout Difficulty: %s", String.format(selected)));

            l.addView(workoutName);
            l.addView(workoutDuration);
            l.addView(workoutTarget);
            l.addView(workoutEquipment);
            l.addView(workoutDifficulty);

            v.addView(l);

            View view = new View(this);

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    1
            );

            layoutParams.setMargins(10, 10, 10, 10);

            view.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));

            view.setLayoutParams(layoutParams);

            v.addView(view);

            showExerciseText(dialogView);
        }

        Button cancelCreation = dialogView.findViewById(R.id.cancelCreation);
        Button confirmCreation = dialogView.findViewById(R.id.confirmCreation);

        cancelCreation.setOnClickListener(v1 -> alertDialog.dismiss());

        confirmCreation.setOnClickListener(v12 -> {
            String correctDifficulty = selected.substring(0, 1).toUpperCase() + selected.substring(1).toLowerCase();
            createJSON(name.getText().toString(), duration.getText().toString(), target.getSelectedItem().toString(), equipment.getText().toString(), correctDifficulty, addedExercisesID);
        });
        alertDialog.show();
    }

    public void createJSON(String name, String duration, String target, String equipment, String difficulty, ArrayList<String> exercises) {
        JSONObject newWorkout = new JSONObject();

        try {
            newWorkout.put("WorkoutName", name);
            newWorkout.put("WorkoutDuration", duration);
            newWorkout.put("TargetMuscleGroup", target);
            newWorkout.put("Equipment", equipment);
            newWorkout.put("Difficulty", difficulty);

            System.out.println(newWorkout);

            JSONToDB.insertWorkout(newWorkout, exercises);

            // Will take user to currentWorkout page when done!
            startActivity(new Intent(this, workout_option.class));

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    private void enableBorder(View v) {
        Drawable[] layers = new Drawable[2];

        if (v == easy) {
            layers[0] = createColorDrawable(Color.parseColor("#00FF00"));
        } else if (v == medium) {
            layers[0] = createColorDrawable(Color.parseColor("#FFFF00"));
        } else if (v == hard) {
            layers[0] = createColorDrawable(Color.parseColor("#FF0000"));
        }

        layers[1] = border;

        LayerDrawable layerDrawable = new LayerDrawable(layers);
        v.setBackground(layerDrawable);
    }

    private void disableBorder(View v) {
        if (v == easy) {
            v.setBackgroundColor(Color.parseColor("#00FF00"));
        } else if (v == medium) {
            v.setBackgroundColor(Color.parseColor("#FFFF00"));
        } else if (v == hard) {
            v.setBackgroundColor(Color.parseColor("#FF0000"));
        }
    }

    private Drawable createColorDrawable(int color) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setColor(color);
        return drawable;
    }

    public void showExerciseText(View d) {
        LinearLayout s = d.findViewById(R.id.dataHolder);

        // Linear layout to hold all layouts.;
        LinearLayout exerciseHolder = new LinearLayout(this);
        exerciseHolder.setOrientation(LinearLayout.VERTICAL);

        // Creates a layout containing the exercise boxes.
        JSONArray jsonArray;
        jsonArray = new JSONArray();
        for (JSONObject exercise : addedExercises) {
            jsonArray.put(exercise);
        }

        // For every exercise, we create a box containing the details.
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject workoutObject;

            try {
                workoutObject = jsonArray.getJSONObject(i);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

            LinearLayout textHolder = new LinearLayout(this);
            textHolder.setOrientation(LinearLayout.VERTICAL);

            TextView exerciseNameText = new TextView(this);
            TextView exerciseDescriptionText = new TextView(this);
            TextView exerciseTargetMuscleGroupText = new TextView(this);
            TextView exerciseEquipmentText = new TextView(this);
            TextView exerciseDifficultyText = new TextView(this);

            exerciseNameText.setText(String.format(String.format("Exercise Name: %s", workoutObject.optString("ExerciseName", ""))));
            exerciseDescriptionText.setText(String.format("Exercise Description: %s", workoutObject.optString("Description", "")));
            exerciseTargetMuscleGroupText.setText(String.format("Exercise Target Group: %s", workoutObject.optString("TargetMuscleGroup", "")));
            exerciseEquipmentText.setText(String.format("Exercise Equipment: %s", workoutObject.optString("Equipment", "")));
            exerciseDifficultyText.setText(String.format("Exercise Difficulty: %s", workoutObject.optString("Difficulty")));

            textHolder.addView(exerciseNameText);
            textHolder.addView(exerciseDescriptionText);
            textHolder.addView(exerciseTargetMuscleGroupText);
            textHolder.addView(exerciseEquipmentText);
            textHolder.addView(exerciseDifficultyText);

            exerciseHolder.addView(textHolder);

            View view = new View(this);

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    1
            );

            layoutParams.setMargins(10, 10, 10, 10);

            view.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));

            view.setLayoutParams(layoutParams);

            exerciseHolder.addView(view);
        }

        exerciseHolder.setPadding(10, 10, 10, 10);
        s.addView(exerciseHolder);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.goToHome) {
            startActivity(new Intent(getApplicationContext(), Home.class));
            return true;
        } else if (id == R.id.goToWorkouts) {
            startActivity(new Intent(getApplicationContext(), workout_option.class));
            return true;
        } else if (id == R.id.goToHistory) {
            startActivity(new Intent(getApplicationContext(), History.class));
            return true;
        }
        return true;
    }
}