package com.firstapp.group10app.Pages;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.firstapp.group10app.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

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
                System.out.println("CON!!!");
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