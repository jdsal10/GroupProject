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
import android.os.Handler;
import android.os.Looper;
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

import com.firstapp.group10app.DB.DatabaseManager;
import com.firstapp.group10app.DB.Exercise;
import com.firstapp.group10app.Other.ItemVisualiserText;
import com.firstapp.group10app.Other.JsonToDb;
import com.firstapp.group10app.Other.Session;
import com.firstapp.group10app.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * This class represents the CreateWorkout activity in the application.
 * It allows the user to create a new workout by selecting exercises and setting their details.
 */
public class CreateWorkout extends AppCompatActivity implements View.OnClickListener {
    private String selected = "easy";
    private TextView easy, medium, hard;
    private EditText name, duration, equipment;
    private Drawable border;
    private Button continueButton;
    private LinearLayout p1, p2;
    private int activePage = 1; // 1 = page 1, 2 = page 2
    private Spinner target;
    private ArrayList<Exercise> addedExercisesNew;
    private ArrayList<String> addedExercisesID;
    private final ExecutorService executor;
    private final Handler handler;

    public CreateWorkout() {
        super(R.layout.activity_workout_create);

        executor = Executors.newSingleThreadExecutor();
        handler = new Handler(Looper.getMainLooper());
    }

    /**
     * This method is called when the activity is starting.
     * It initializes the activity and sets up the UI elements.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Behaviour when the user is signed in
        if (Session.getSignedIn()) {
            setContentView(R.layout.activity_workout_create);

            border = ContextCompat.getDrawable(this, R.drawable.toggle_button_outline);

            name = findViewById(R.id.workoutNameInput);
            duration = findViewById(R.id.workoutDurationInput);
            equipment = findViewById(R.id.workoutEquipmentInput);

            easy = findViewById(R.id.easySelect);
            medium = findViewById(R.id.mediumSelect);
            hard = findViewById(R.id.hardSelect);

            Button cancelButton = findViewById(R.id.cancelBtn);
            continueButton = findViewById(R.id.continueBtn);
            ImageButton backButton = findViewById(R.id.backButton);

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

        // Behaviour when the user is anonymous
        else {
            Log.e("CreateWorkout", "User is not signed in. This page should not be accessible.");
            Session.logout(this, "This page should not be accessible. You are being logged out");
            finish();
        }
    }

    /**
     * This method sets up the listeners for the difficulty views.
     * It checks if the fields are empty and sets an error message if they are.
     */
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

    /**
     * This method creates the exercise view.
     * It gets all the exercises from the database and displays them in a scroll view.
     */
    public void createExerciseView() {
        List<Exercise> exercises = DatabaseManager.getInstance().getAllExercises();

        ScrollView exerciseScroll = findViewById(R.id.exerciseSelector);
        exerciseScroll.removeAllViews();

        // Linear layout to hold all layouts.
        LinearLayout exerciseHolder = new LinearLayout(this);
        exerciseHolder.setOrientation(LinearLayout.VERTICAL);

        // Creates a layout containing the exercise boxes.
        boolean[] toggles = new boolean[exercises.size()];

        // We use arrays to hold details of the chosen exercises.
        addedExercisesNew = new ArrayList<>();
        addedExercisesID = new ArrayList<>();

        // For every exercise, we create a box containing the details.
        for (int i = 0; i < exercises.size(); i++) {
            Exercise exercise = exercises.get(i);

            LinearLayout exerciseCombo = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.element_exercise_combo, null);
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

            setExerciseDetails(exerciseBox, exercise);
            exerciseImage.setImageResource(R.drawable.icon_workout);

            String difficultyValue = exercise.getDifficulty();
            difficultyScale.setBackground(createDifficultyDrawable(difficultyValue));

            final int index = i;

            // Allows toggle to update, as well as arrays.
            exerciseCombo.setOnClickListener(v -> {
                if (toggles[index]) {
                    selectedExerciseToggle.setBackgroundColor(Color.TRANSPARENT);
                    addedExercisesID.remove(String.valueOf(exercise.getId()));
                    addedExercisesNew.remove(exercise);
                } else {
                    selectedExerciseToggle.setBackgroundColor(ContextCompat.getColor(this, R.color.pastel_green));
                    addedExercisesNew.add(exercise);
                    addedExercisesID.add(String.valueOf(exercise.getId()));
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

    private void setExerciseDetails(LinearLayout exerciseBox, Exercise exercise) {
        ((TextView) exerciseBox.findViewById(R.id.exerciseNameView)).setText(exercise.getName());
        ((TextView) exerciseBox.findViewById(R.id.exerciseDescriptionView)).setText(exercise.getDescription());
        ((TextView) exerciseBox.findViewById(R.id.exerciseTargetMuscleGroupView)).setText(String.format("Exercise Target Group: %s", exercise.getTargetMuscleGroup()));
        ((TextView) exerciseBox.findViewById(R.id.exerciseEquipmentView)).setText(String.format("Exercise Equipment: %s", exercise.getEquipment()));
        ((TextView) exerciseBox.findViewById(R.id.exerciseSetsView)).setText(String.format("Exercise Sets: %s", exercise.getSets()));
        ((TextView) exerciseBox.findViewById(R.id.exerciseRepsView)).setText(String.format("Exercise Reps: %s", exercise.getReps()));
        ((TextView) exerciseBox.findViewById(R.id.exerciseTimeView)).setText(String.format("Exercise Time: %s", exercise.getTime()));
    }

    private GradientDrawable createDifficultyDrawable(String difficultyValue) {
        GradientDrawable difficultyDrawable = new GradientDrawable();
        difficultyDrawable.setShape(GradientDrawable.RECTANGLE);
        difficultyDrawable.setCornerRadius(dpToPx(this, 8));
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
        return difficultyDrawable;
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
            if ((name.getText().toString().isEmpty()) || (duration.getText().toString().isEmpty()) || (equipment.getText().toString().isEmpty())) {
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

    /**
     * This method is called when the user has finished selecting exercises and is ready to create the workout.
     * It displays a final check dialog where the user can review their workout before creating it.
     */
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
            ItemVisualiserText.showText(this, finalVersionHolder, data, addedExercisesNew);
        }

        Button cancelCreation = dialogView.findViewById(R.id.cancelCreation);
        Button confirmCreation = dialogView.findViewById(R.id.confirmCreation);

        cancelCreation.setOnClickListener(v1 -> alertDialog.dismiss());

        confirmCreation.setOnClickListener(v12 -> {

            // Converts to a String of JSON.
            try {
                Session.setSelectedWorkout(createJSONStringNew(data, addedExercisesNew));
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

            // Creates the workout.
            addWorkout(name.getText().toString(), duration.getText().toString(), target.getSelectedItem().toString(), equipment.getText().toString(), correctDifficulty, addedExercisesID);
        });
        alertDialog.show();
    }

    /**
     * This method creates a JSON string that represents a workout.
     * The workout consists of a name, duration, target muscle group, equipment, difficulty, and a list of exercises.
     * Each exercise is a JSONObject that contains details about the exercise.
     *
     * @param data           An array of Strings where:
     *                       data[0] is the workout name,
     *                       data[1] is the workout duration,
     *                       data[2] is the target muscle group,
     *                       data[3] is the equipment, and
     *                       data[4] is the difficulty.
     * @param addedExercises An ArrayList of Exercise objects that represent the exercises in the workout.
     * @return A JSONObject that represents the workout.
     * @throws JSONException If there is a problem with the JSON syntax.
     */
    public JSONObject createJSONStringNew(String[] data, ArrayList<Exercise> addedExercises) throws JSONException {
        StringBuilder json = new StringBuilder("{");
        json.append("\"WorkoutName\": \"").append(data[0]).append("\",");
        json.append("\"WorkoutDuration\": \"").append(data[1]).append("\",");
        json.append("\"TargetMuscleGroup\": \"").append(data[2]).append("\",");
        json.append("\"Equipment\": \"").append(data[3]).append("\",");
        json.append("\"Difficulty\": \"").append(data[4]).append("\",");
        json.append("\"Exercises\": [");

        for (int i = 0; i <= addedExercises.size() - 1; i++) {
            json.append("{");

            json.append("\"ExerciseName\": \"").append(addedExercises.get(i).getName()).append("\",");
            json.append("\"Description\": \"").append(addedExercises.get(i).getDescription()).append("\",");
            json.append("\"TargetMuscleGroup\": \"").append(addedExercises.get(i).getTargetMuscleGroup()).append("\",");
            json.append("\"Equipment\": \"").append(addedExercises.get(i).getEquipment()).append("\",");
            json.append("\"Difficulty\": \"").append(addedExercises.get(i).getDifficulty()).append("\",");
            json.append("\"Sets\": \"").append(addedExercises.get(i).getSets()).append("\",");
            json.append("\"Reps\": \"").append(addedExercises.get(i).getReps()).append("\",");

            if (i == addedExercises.size() - 1) {
                json.append("\"Time\": \"").append(addedExercises.get(i).getTime()).append("\"}");
            } else {
                json.append("\"Time\": \"").append(addedExercises.get(i).getTime()).append("\"},");
            }
        }

        json.append("]}");
        Log.d("CreateWorkout JSON", json.toString());
        return new JSONObject(String.valueOf(json));
    }

    /**
     * This method adds a new workout to the database.
     * It creates a JSONObject that represents the workout and then calls the insertWorkout method to add it to the database.
     * After the workout is added, it starts the ActivityContainer activity.
     *
     * @param name       The name of the workout.
     * @param duration   The duration of the workout.
     * @param target     The target muscle group of the workout.
     * @param equipment  The equipment needed for the workout.
     * @param difficulty The difficulty level of the workout.
     * @param exercises  An ArrayList of Strings where each String is the ID of an exercise in the workout.
     */
    public void addWorkout(String name, String duration, String target, String equipment, String difficulty, ArrayList<String> exercises) {
        JSONObject newWorkout = new JSONObject();

        try {
            newWorkout.put("WorkoutName", name);
            newWorkout.put("WorkoutDuration", duration);
            newWorkout.put("TargetMuscleGroup", target);
            newWorkout.put("Equipment", equipment);
            newWorkout.put("Difficulty", difficulty);

            executor.execute(() -> {
                try {
                    final int workoutId = JsonToDb.insertWorkout(newWorkout, exercises);

                    handler.post(() -> {
                        Session.setWorkoutID(workoutId);

                        Intent intent = new Intent(CreateWorkout.this, ActivityContainer.class);
                        intent.putExtra("workoutHub", WorkoutHub.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    });
                } catch (Exception e) {
                    // TODO: Add proper error handling
                    throw new RuntimeException(e);
                }
            });
        } catch (JSONException e) {
            // TODO: Add proper error handling
            throw new RuntimeException(e);
        }
    }

    /**
     * This method enables the border of a View.
     * It creates a LayerDrawable with two layers: the first layer is a color drawable and the second layer is a border.
     * The color of the first layer depends on the View that is passed as a parameter.
     * The border is a Drawable that is stored as a field in the class.
     *
     * @param v The View that needs to have its border enabled.
     */
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

    /**
     * This method disables the border of a View.
     * It creates a GradientDrawable with a color that depends on the View that is passed as a parameter.
     * The GradientDrawable is then set as the background of the View.
     *
     * @param v The View that needs to have its border disabled.
     */
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

    /**
     * This method converts dp (density-independent pixels) to px (pixels).
     * It uses the display metrics to get the density of the screen and then multiplies the dp value by the density to get the px value.
     *
     * @param context The context of the current state of the application.
     * @param dp      The value in dp that needs to be converted to px.
     * @return The value in px.
     */
    public static int dpToPx(Context context, int dp) {
        float density = context.getResources().getDisplayMetrics().density;
        return Math.round((float) dp * density);
    }

    /**
     * This method creates a color drawable with a specific color.
     * It creates a GradientDrawable, sets its shape to a rectangle, sets its color to the color that is passed as a parameter, and sets its corner radius to 8dp.
     *
     * @param color The color of the drawable.
     * @return A Drawable that is a rectangle with a specific color and a corner radius of 8dp.
     */
    private Drawable createColorDrawable(int color) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setColor(color);
        drawable.setCornerRadius(dpToPx(this, 8)); // set corner radius to 8dp
        return drawable;
    }
}