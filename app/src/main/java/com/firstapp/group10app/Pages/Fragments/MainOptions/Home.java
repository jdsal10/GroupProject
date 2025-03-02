package com.firstapp.group10app.Pages.Fragments.MainOptions;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.firstapp.group10app.DB.OnlineDb.OnlineDbConnection;
import com.firstapp.group10app.DB.OnlineDb.OnlineDbHelper;

import android.Manifest;

import com.firstapp.group10app.Other.Session;
import com.firstapp.group10app.Pages.ActivityContainer;
import com.firstapp.group10app.Pages.WorkoutSearch;
import com.firstapp.group10app.R;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class Home extends Fragment implements View.OnClickListener, SensorEventListener {
    private TextView workoutsNum;
    private TextView totalTimeNum;
    private TextView favMuscleTarget;
    private float initialStepCount = -1;
    private String CurrentUser, userIdKey;
    private SensorManager sensorManager;
    private View rootView;
    private SharedPreferences sharedPreferences;


    public Home() {
        super(R.layout.activity_home);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_home, container, false);

        ScrollView signedInLayout = rootView.findViewById(R.id.signedInLayout);
        LinearLayout anonymousLayout = rootView.findViewById(R.id.anonymousLayout);


        // Behaviour if signed in
        if (Session.getSignedIn()) {
            userIdKey = Session.getUserDetails()[6];
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

            signedInLayout.setVisibility(View.VISIBLE);
            anonymousLayout.setVisibility(View.GONE);

            //declare variables to edit
            workoutsNum = rootView.findViewById(R.id.workoutCountTextView);
            totalTimeNum = rootView.findViewById(R.id.timeTextView);
            favMuscleTarget = rootView.findViewById(R.id.favMuscleTextView);

            //Set click listeners for quick link buttons
            LinearLayout FullBodyLink = rootView.findViewById(R.id.fullBody);
            FullBodyLink.setOnClickListener(this);

            LinearLayout AbsLink = rootView.findViewById(R.id.abs);
            AbsLink.setOnClickListener(this);

            LinearLayout ArmsLink = rootView.findViewById(R.id.arms);
            ArmsLink.setOnClickListener(this);

            LinearLayout statsLayout = rootView.findViewById(R.id.statisticsLayout);
            statsLayout.setAlpha(.5f);
            new Thread(() -> {
                Session.setOnlineDbStatus(OnlineDbConnection.testConnection());

                CurrentUser = Session.getUserDetails()[6];

                String totalWorkouts = Integer.toString(OnlineDbHelper.getTotalinHistory(CurrentUser));
                String totalTime = Integer.toString(OnlineDbHelper.getTotalMinutesFromHistory(CurrentUser));
                String favMuscleTargeted = OnlineDbHelper.getFavMuscle(CurrentUser);
                // Update UI on the main thread after fetching data
                requireActivity().runOnUiThread(() -> setWorkoutCount(totalWorkouts, totalTime, favMuscleTargeted));
                statsLayout.setAlpha(1f);
            }).start();

            sensorManager = (SensorManager) requireActivity().getSystemService(Context.SENSOR_SERVICE);
            TextView steps = rootView.findViewById(R.id.StepCounter);
            steps.setText("0");
        }

        // Behaviour if anonymous
        else {
            signedInLayout.setVisibility(View.GONE);
            anonymousLayout.setVisibility(View.VISIBLE);

            rootView.findViewById(R.id.searchButton).setOnClickListener(this);
            rootView.findViewById(R.id.logoutButton).setOnClickListener(this);
        }
        super.onCreate(savedInstanceState);

        return rootView;
    }
    @Override
    public void onResume() {
        super.onResume();

        if (!Session.getSignedIn()) return;

        if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACTIVITY_RECOGNITION) == PackageManager.PERMISSION_GRANTED) {             // Permission granted
                LinearLayout stepLayout = rootView.findViewById(R.id.stepCounterLayout);
                TextView stepText = rootView.findViewById(R.id.stepTitle);
                stepText.setVisibility(View.VISIBLE);
                stepLayout.setVisibility(View.VISIBLE);
            } else {
                // Permission denied
                LinearLayout stepLayout = rootView.findViewById(R.id.stepCounterLayout);
                TextView stepText = rootView.findViewById(R.id.stepTitle);
                stepText.setVisibility(View.GONE);
                stepLayout.setVisibility(View.GONE);
            }

        registerStepCounter();
    }

    private void registerStepCounter() {
        if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACTIVITY_RECOGNITION) == PackageManager.PERMISSION_GRANTED) {
            Sensor countSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
            if (countSensor != null) {
                sensorManager.registerListener(this, countSensor, SensorManager.SENSOR_DELAY_UI);
                initialStepCount = sharedPreferences.getFloat(userIdKey + "_initialStepCount", -1);
            } else {
                Log.e("Home.onResume()", "Count sensor not available!");
            }

            float stepsTaken = sharedPreferences.getFloat(userIdKey + "_stepsTaken", 0);
            TextView steps = rootView.findViewById(R.id.StepCounter);
            steps.setText(String.valueOf(stepsTaken));
        }
    }
    @Override
    public void onPause() {
        super.onPause();

        if (!Session.getSignedIn()) return;

        sharedPreferences.edit().putFloat(userIdKey + "_initialStepCount", initialStepCount).apply();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.searchButton) {
            tellParentToStartNewActivity(new WorkoutSearch());
        } else if (id == R.id.logoutButton) {
            Log.d("Home.onClick()", "Logout button clicked");
            showConfirmationLogout();
        } else if (id == R.id.fullBody) {
            Log.d("Home.onClick()", "fullbody was clicked");
            Intent intent = new Intent(getActivity(), WorkoutSearch.class);
            intent.putExtra("targetMuscle", "Full-body");
            startActivity(intent);
        } else if (id == R.id.abs) {
            Log.d("Home.onClick()", "fullbody was clicked");
            Intent intent = new Intent(getActivity(), WorkoutSearch.class);
            intent.putExtra("targetMuscle", "Abs");
            startActivity(intent);
        } else if (id == R.id.arms) {
            Log.d("Home.onClick()", "fullbody was clicked");
            Intent intent = new Intent(getActivity(), WorkoutSearch.class);
            intent.putExtra("targetMuscle", "Arms");
            startActivity(intent);
        }
    }

    // Method to dynamically edit workoutCountTextView
    public void setWorkoutCount(String val1, String val2, String val3) {
        //need to add check for other three values
        if (val1 != null) {
            workoutsNum.setText(val1);
            totalTimeNum.setText(String.format("%s min", val2));
            favMuscleTarget.setText(String.format("'%s'", val3));
        }
    }

    public void showConfirmationLogout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        LayoutInflater inflater = getLayoutInflater();

        View dialogView = inflater.inflate(R.layout.popup_confirm_logout, null);
        builder.setView(dialogView);
        AlertDialog alertDialog = builder.create();

        Button confirm = dialogView.findViewById(R.id.confirm);

        alertDialog.show();

        confirm.setOnClickListener(v -> {
            alertDialog.dismiss();
            tellParentToFinish();
        });
    }

    public void tellParentToStartNewActivity(AppCompatActivity newActivity) {
        Workout parentFrag = (Workout) Home.this.getParentFragment();
        if (parentFrag != null) {
            parentFrag.tellParentToStartNewActivity(newActivity, R.anim.slide_down_in, R.anim.slide_down_out);
        } else {
            startActivity(new Intent(getContext(), newActivity.getClass()));
        }
    }

    private void tellParentToFinish() {
        ActivityContainer parent = (ActivityContainer) getActivity();
        if (parent != null) {
            parent.logOut();
        } else {
            Session.logout(getContext());
        }
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        if (initialStepCount == -1) {
            initialStepCount = event.values[0];
        }

        float stepsTaken = event.values[0] - initialStepCount;

        sharedPreferences.edit().putFloat(userIdKey + "_stepsTaken", stepsTaken).apply();

        long lastResetTime = sharedPreferences.getLong(userIdKey + "_lastResetTime", System.currentTimeMillis());
        if (System.currentTimeMillis() - lastResetTime >= 24 * 60 * 60 * 1000) {
            // Reset steps
            initialStepCount = event.values[0];
            stepsTaken = 0;
            sharedPreferences.edit().putFloat(userIdKey + "_stepsTaken", stepsTaken).apply();

            sharedPreferences.edit().putLong(userIdKey + "_lastResetTime", System.currentTimeMillis()).apply();
        }

        TextView steps = rootView.findViewById(R.id.StepCounter);
        steps.setText(String.valueOf(stepsTaken));
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
