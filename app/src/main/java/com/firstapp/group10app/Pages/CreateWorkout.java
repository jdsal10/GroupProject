package com.firstapp.group10app.Pages;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.firstapp.group10app.DB.DbHelper;
import com.firstapp.group10app.Other.ItemVisualiserText;
import com.firstapp.group10app.Other.JsonToDb;
import com.firstapp.group10app.Other.Session;
import com.firstapp.group10app.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CreateWorkout extends AppCompatActivity implements View.OnClickListener {
    String selected = "easy";
    TextView easy, medium, hard;
    EditText name, duration, equipment;
    TextView durationTitle;
    Drawable border;
    Button cancelButton, continueButton;
    ImageButton backButton;
    LinearLayout p1, p2;
    int activePage = 1; // 1 = page 1, 2 = page 2
    Spinner target;
    ArrayList<JSONObject> addedExercises;
    ArrayList<String> addedExercisesID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_create);

        durationTitle = findViewById(R.id.durationTitle);
        border = ContextCompat.getDrawable(this, R.drawable.toggle_button_outline);

        name = findViewById(R.id.workoutNameInput);
        duration = findViewById(R.id.workoutDurationInput);
        equipment = findViewById(R.id.workoutEquipmentInput);

        easy = findViewById(R.id.easySelect);
        medium = findViewById(R.id.mediumSelect);
        hard = findViewById(R.id.hardSelect);

        cancelButton = findViewById(R.id.cancelBtn);
        continueButton = findViewById(R.id.continueBtn);
        backButton = findViewById(R.id.backButton);

        target = findViewById(R.id.workoutTargetInput);

        ArrayAdapter<CharSequence> adapterTarget = ArrayAdapter.createFromResource(
                this,
                R.array.targetMuscleGroup,
                android.R.layout.simple_spinner_item
        );

        adapterTarget.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        target.setAdapter(adapterTarget);
        target.setSelection(0);

        // Set the on click listeners for the difficulty buttons.
        easy.setOnClickListener(this);
        medium.setOnClickListener(this);
        hard.setOnClickListener(this);

        // Set the background color of the difficulty buttons.
        easy.setBackground(createColorDrawable(ContextCompat.getColor(this, R.color.pastel_green))); // Pastel green
        medium.setBackground(createColorDrawable(ContextCompat.getColor(this, R.color.pastel_yellow))); // Pastel yellow
        hard.setBackground(createColorDrawable(ContextCompat.getColor(this, R.color.pastel_red))); // Pastel red

        // Set the default difficulty buttons border.
        enableBorder(easy);

        // Set the on click listeners for the other buttons.
        cancelButton.setOnClickListener(this);
        continueButton.setOnClickListener(this);
        backButton.setOnClickListener(this);

        // get the linear layouts for the pages.
        p1 = findViewById(R.id.page1);
        p2 = findViewById(R.id.page2);

        p1.setVisibility(VISIBLE);
        p2.setVisibility(GONE);

        // Sets listeners for difficultly views.
        setListeners();
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
        String newData = DbHelper.getAllExercises();

        ScrollView exerciseScroll = findViewById(R.id.exerciseSelector);
        exerciseScroll.removeAllViews();

        // Linear layout to hold all layouts.
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

        // We use arrays to hold details of the chosen exercises.
        addedExercises = new ArrayList<>();
        addedExercisesID = new ArrayList<>();

        // For every exercise, we create a box containing the details.
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject workoutObject;

            try {
                workoutObject = jsonArray.getJSONObject(i);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            LinearLayout exerciseCombo = (LinearLayout) inflater.inflate(R.layout.element_exercise_combo, null);

            View selectedExerciseToggle = exerciseCombo.findViewById(R.id.checkBox);

            LinearLayout exerciseBox = exerciseCombo.findViewById(R.id.exerciseDetails);

            // Sets correct measurements for toggles.
            exerciseCombo.post(() -> {
                int height = exerciseCombo.getHeight();
                LinearLayout.LayoutParams correctParam = new LinearLayout.LayoutParams(100, height);
                correctParam.setMargins(0, 0, 10, 0);

                selectedExerciseToggle.setLayoutParams(correctParam);
            });

            ImageView exerciseImage = exerciseBox.findViewById(R.id.exerciseImage);
            View difficultyScale = exerciseBox.findViewById(R.id.difficulty);

            TextView exerciseNameView = exerciseBox.findViewById(R.id.exerciseNameView);
            TextView exerciseDescriptionView = exerciseBox.findViewById(R.id.exerciseDescriptionView);
            TextView exerciseTargetMuscleGroupView = exerciseBox.findViewById(R.id.exerciseTargetMuscleGroupView);
            TextView exerciseEquipmentView = exerciseBox.findViewById(R.id.exerciseEquipmentView);
            TextView exerciseSetsView = exerciseBox.findViewById(R.id.exerciseSetsView);
            TextView exerciseRepsView = exerciseBox.findViewById(R.id.exerciseRepsView);
            TextView exerciseTimeView = exerciseBox.findViewById(R.id.exerciseTimeView);


            exerciseNameView.setText(String.format(workoutObject.optString("ExerciseName", "")));
            exerciseDescriptionView.setText(workoutObject.optString("Description", ""));
            exerciseTargetMuscleGroupView.setText(String.format("Exercise Target Group: %s", workoutObject.optString("TargetMuscleGroup", "")));
            exerciseEquipmentView.setText(String.format("Exercise Equipment: %s", workoutObject.optString("Equipment", "")));
            exerciseSetsView.setText(String.format("Exercise Sets: %s", workoutObject.optString("Sets", "")));
            exerciseRepsView.setText(String.format("Exercise Reps: %s", workoutObject.optString("Reps", "")));
            exerciseTimeView.setText(String.format("Exercise Time: %s", workoutObject.optString("Time", "")));


            exerciseImage.setImageResource(R.drawable.icon_workout);

            String difficultyValue = workoutObject.optString("Difficulty", "");

            // Create a GradientDrawable with a corner radius
            GradientDrawable difficultyDrawable = new GradientDrawable();
            difficultyDrawable.setShape(GradientDrawable.RECTANGLE);
            difficultyDrawable.setCornerRadius(dpToPx(this, 8)); // set corner radius to 8dp

            // Declares background colours.
            switch (difficultyValue) {
                case "Easy":
                    difficultyDrawable.setColor(ContextCompat.getColor(this, R.color.pastel_green));
                    break;
                case "Medium":
                    difficultyDrawable.setColor(ContextCompat.getColor(this, R.color.pastel_yellow));
                    break;
                case "Hard":
                    difficultyDrawable.setColor(ContextCompat.getColor(this, R.color.pastel_red));
                    break;
            }

            // Set the GradientDrawable as the background for the difficultyScale View
            difficultyScale.setBackground(difficultyDrawable);

            final int index = i;

            // Allows toggle to update, as well as arrays.
            exerciseCombo.setOnClickListener(v -> {
                if (toggles[index]) {
                    selectedExerciseToggle.setBackgroundColor(Color.TRANSPARENT);
                    addedExercisesID.remove(workoutObject.optString("ExerciseID", ""));
                    addedExercises.remove(workoutObject);
                } else {
                    selectedExerciseToggle.setBackgroundColor(ContextCompat.getColor(this, R.color.pastel_green));
                    addedExercises.add(workoutObject);
                    addedExercisesID.add(workoutObject.optString("ExerciseID", ""));

                }
                toggles[index] = !toggles[index];
            });

            exerciseHolder.addView(exerciseCombo);

            // Adds space between the different exercises boxes
            View space = new View(this);

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, 1
            );

            layoutParams.setMargins(10, 10, 10, 10);
            space.setLayoutParams(layoutParams);
            exerciseHolder.addView(space);
        }

        // Adds the Linear layout containing all boxes to the scroll view.
        exerciseScroll.addView(exerciseHolder);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.backButton) {
            ActivityContainer.currentView = R.layout.activity_workout_option;
            startActivity(new Intent(getApplicationContext(), ActivityContainer.class));
            overridePendingTransition(R.anim.slide_up_in, R.anim.slide_up_out);
        }
        // If the user selects a difficulty, we adjust the border
        else if (id == R.id.easySelect && !selected.equals("easy")) {
            selected = "easy";
            enableBorder(easy);
            disableBorder(medium);
            disableBorder(hard);
        } else if (id == R.id.mediumSelect && !selected.equals("medium")) {
            selected = "medium";
            enableBorder(medium);
            disableBorder(easy);
            disableBorder(hard);
        } else if (id == R.id.hardSelect && !selected.equals("hard")) {
            selected = "hard";
            enableBorder(hard);
            disableBorder(easy);
            disableBorder(medium);
        }

        // More complex logic for the Continue and Cancel buttons
        else if (id == R.id.continueBtn && activePage == 1) {
            if ((name.getText().toString().length() == 0) || (duration.getText().toString().length() == 0) || (equipment.getText().toString().length() == 0)) {
                continueButton.setError("Some fields have no info");
            } else if ((Integer.parseInt(duration.getText().toString()) > 180) || (Integer.parseInt(duration.getText().toString()) == 0)) {
                duration.setError("Duration must be between 180 and 0!");
            } else {
                p1.setVisibility(GONE);
                p2.setVisibility(VISIBLE);
                activePage = 2;

                createExerciseView();
            }
        } else if (id == R.id.cancelBtn && activePage == 1) {
            // Maybe add popup to prevent user losing progress.
            startActivity(new Intent(getApplicationContext(), ActivityContainer.class));
            overridePendingTransition(R.anim.slide_up_in, R.anim.slide_up_out);
        } else if (id == R.id.cancelBtn && activePage == 2) {
            p2.setVisibility(GONE);
            p1.setVisibility(VISIBLE);
            activePage = 1;
        } else if (id == R.id.continueBtn && activePage == 2) {
            if (addedExercisesID.isEmpty()) {
                continueButton.setError("At least one workout is needed,");
            } else {
                finalCheck();
            }
        }
    }

    public void finalCheck() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();

        View dialogView = inflater.inflate(R.layout.popup_confirm_create_workout, null);
        builder.setView(dialogView);
        AlertDialog alertDialog = builder.create();

        LinearLayout finalVersionHolder = dialogView.findViewById(R.id.dataHolder);
        String correctDifficulty = selected.substring(0, 1).toUpperCase() + selected.substring(1).toLowerCase();

        String[] data = new String[5];
        data[0] = name.getText().toString();
        data[1] = duration.getText().toString();
        data[2] = target.getSelectedItem().toString();
        data[3] = equipment.getText().toString();
        data[4] = correctDifficulty;

        // Continues if there is data.
        if (finalVersionHolder != null) {
            ItemVisualiserText.showText(this, finalVersionHolder, data, addedExercises);
        }

        Button cancelCreation = dialogView.findViewById(R.id.cancelCreation);
        Button confirmCreation = dialogView.findViewById(R.id.confirmCreation);

        cancelCreation.setOnClickListener(v1 -> alertDialog.dismiss());

        confirmCreation.setOnClickListener(v12 -> {

            // Converts to a String of JSON.
            try {
                Session.selectedWorkout = createJSONString(data, addedExercises);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

            // Creates the workout.
            addWorkout(name.getText().toString(), duration.getText().toString(), target.getSelectedItem().toString(), equipment.getText().toString(), correctDifficulty, addedExercisesID);
        });
        alertDialog.show();
    }

    public JSONObject createJSONString(String[] data, ArrayList<JSONObject> addedExercises) throws JSONException {
        StringBuilder json = new StringBuilder("{");
        json.append("\"WorkoutName\": \"").append(data[0]).append("\",");
        json.append("\"WorkoutDuration\": \"").append(data[1]).append("\",");
        json.append("\"TargetMuscleGroup\": \"").append(data[2]).append("\",");
        json.append("\"Equipment\": \"").append(data[3]).append("\",");
        json.append("\"Difficulty\": \"").append(data[4]).append("\",");
        json.append("\"Exercises\": [");

        for (int i = 0; i < addedExercises.size() - 1; i++) {
            json.append("{");
            json.append("\"ExerciseName\": \"").append(addedExercises.get(i).optString("ExerciseName", "")).append("\",");
            json.append("\"Description\": \"").append(addedExercises.get(i).optString("Description", "")).append("\",");
            json.append("\"TargetMuscleGroup\": \"").append(addedExercises.get(i).optString("TargetMuscleGroup", "")).append("\",");
            json.append("\"Equipment\": \"").append(addedExercises.get(i).optString("Equipment", "")).append("\",");
            json.append("\"Difficulty\": \"").append(addedExercises.get(i).optString("Difficulty", "")).append("\",");
            json.append("\"Sets\": \"").append(addedExercises.get(i).optString("Sets", "")).append("\",");
            json.append("\"Reps\": \"").append(addedExercises.get(i).optString("Reps", "")).append("\",");
            json.append("\"Time\": \"").append(addedExercises.get(i).optString("Time", "")).append("\"},");
        }

        json.append("{");
        json.append("\"ExerciseName\": \"").append(addedExercises.get(addedExercises.size() - 1).optString("ExerciseName", "")).append("\",");
        json.append("\"Description\": \"").append(addedExercises.get(addedExercises.size() - 1).optString("Description", "")).append("\",");
        json.append("\"TargetMuscleGroup\": \"").append(addedExercises.get(addedExercises.size() - 1).optString("TargetMuscleGroup", "")).append("\",");
        json.append("\"Equipment\": \"").append(addedExercises.get(addedExercises.size() - 1).optString("Equipment", "")).append("\",");
        json.append("\"Difficulty\": \"").append(addedExercises.get(addedExercises.size() - 1).optString("Difficulty", "")).append("\",");
        json.append("\"Sets\": \"").append(addedExercises.get(addedExercises.size() - 1).optString("Sets", "")).append("\",");
        json.append("\"Reps\": \"").append(addedExercises.get(addedExercises.size() - 1).optString("Reps", "")).append("\",");
        json.append("\"Time\": \"").append(addedExercises.get(addedExercises.size() - 1).optString("Time", "")).append("\"}");

        json.append("]}");
        Log.d("CreateWorkout JSON", json.toString());
        return new JSONObject(String.valueOf(json));
    }

    public void addWorkout(String name, String duration, String target, String equipment, String difficulty, ArrayList<String> exercises) {
        JSONObject newWorkout = new JSONObject();

        try {
            newWorkout.put("WorkoutName", name);
            newWorkout.put("WorkoutDuration", duration);
            newWorkout.put("TargetMuscleGroup", target);
            newWorkout.put("Equipment", equipment);
            newWorkout.put("Difficulty", difficulty);

            Session.workoutID = JsonToDb.insertWorkout(newWorkout, exercises);

            startActivity(new Intent(this, WorkoutHub.class));
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    private void enableBorder(View v) {
        Drawable[] layers = new Drawable[2];

        if (v == easy) {
            layers[0] = createColorDrawable(ContextCompat.getColor(this, R.color.pastel_green));
        } else if (v == medium) {
            layers[0] = createColorDrawable(ContextCompat.getColor(this, R.color.pastel_yellow));
        } else if (v == hard) {
            layers[0] = createColorDrawable(ContextCompat.getColor(this, R.color.pastel_red));
        }

        layers[1] = border;

        LayerDrawable layerDrawable = new LayerDrawable(layers);
        v.setBackground(layerDrawable);
    }

    private void disableBorder(View v) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setCornerRadius(dpToPx(this, 8)); // set corner radius to 8dp

        if (v == easy) {
            drawable.setColor(ContextCompat.getColor(this, R.color.pastel_green));
        } else if (v == medium) {
            drawable.setColor(ContextCompat.getColor(this, R.color.pastel_yellow));
        } else if (v == hard) {
            drawable.setColor(ContextCompat.getColor(this, R.color.pastel_red));
        }

        v.setBackground(drawable);
    }

    public static int dpToPx(Context context, int dp) {
        float density = context.getResources().getDisplayMetrics().density;
        return Math.round((float) dp * density);
    }

    private Drawable createColorDrawable(int color) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setColor(color);
        drawable.setCornerRadius(dpToPx(this, 8)); // set corner radius to 8dp
        return drawable;
    }
}