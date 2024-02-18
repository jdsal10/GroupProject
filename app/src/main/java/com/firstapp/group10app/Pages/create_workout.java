package com.firstapp.group10app.Pages;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.firstapp.group10app.DB.DBHelper;
import com.firstapp.group10app.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class create_workout extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener, View.OnClickListener {
    String selected = "easy";
    View easy, medium, hard;
    EditText name, duration, equipment;
    TextView durationTitle;
    Drawable border;
    Button cancel, continue1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_workout);

        durationTitle = findViewById(R.id.durationTitle);
        border = ContextCompat.getDrawable(this, R.drawable.selected_item);

        name = findViewById(R.id.workoutNameInput);
        duration = findViewById(R.id.workoutDurationInput);
        equipment = findViewById(R.id.workoutEquipmentInput);
        Spinner target;

        easy = findViewById(R.id.easySelect);
        medium = findViewById(R.id.mediumSelect);
        hard = findViewById(R.id.hardSelect);

        cancel = findViewById(R.id.createWorkoutCancel);
        continue1 = findViewById(R.id.createWorkoutContinue1);
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
        System.out.println("SIZXE + " + jsonArray.length());
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
            int correctHeight = exerciseBox.getHeight();

            LinearLayout.LayoutParams correctParam = new LinearLayout.LayoutParams(
                    100,
                    100
            );

            t.setLayoutParams(correctParam);

            System.out.println("HEHE: " + t.getHeight());

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


            exerciseCombo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (toggles[index]) {
                        t.setBackgroundColor(Color.TRANSPARENT);
                    } else {
                        t.setBackgroundColor(Color.BLACK);
                    }
                    toggles[index] = !toggles[index];
                }
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
                System.out.println("STOP IN LAW");
            } else {
                LinearLayout p1 = findViewById(R.id.page1);
                LinearLayout p2 = findViewById(R.id.page2);

                p1.setVisibility(GONE);
                p2.setVisibility(VISIBLE);

                createExerciseView();
            }
        } else if (id == R.id.createWorkoutCancel) {
            // Maybe add popup to prevent user losing progress.
            startActivity(new Intent(this, workout_option.class));
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