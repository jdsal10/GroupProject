package com.firstapp.group10app.Pages.Fragments.MainOptions;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.firstapp.group10app.DB.OnlineDb.OnlineDbConnection;
import com.firstapp.group10app.DB.OnlineDb.OnlineDbHelper;
import com.firstapp.group10app.Other.Session;
import com.firstapp.group10app.Pages.ActivityContainer;
import com.firstapp.group10app.Pages.WorkoutSearch;
import com.firstapp.group10app.R;

public class Home extends Fragment implements View.OnClickListener {
    private TextView workoutsNum;
    private TextView totalTimeNum;
    private String CurrentUser;

    public Home() {
        super(R.layout.activity_home);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_home, container, false);

        LinearLayout signedInLayout = rootView.findViewById(R.id.signedInLayout);
        LinearLayout anonymousLayout = rootView.findViewById(R.id.anonymousLayout);

        // Behaviour if signed in
        if (Session.getSignedIn()) {
            signedInLayout.setVisibility(View.VISIBLE);
            anonymousLayout.setVisibility(View.GONE);

            workoutsNum = rootView.findViewById(R.id.workoutCountTextView);
            totalTimeNum = rootView.findViewById(R.id.timeTextView);

            new Thread(() -> {
                Session.setOnlineDbStatus(OnlineDbConnection.testConnection());

                CurrentUser = Session.getUserDetails()[6];

                String totalWorkouts = Integer.toString(OnlineDbHelper.getTotalinHistory(CurrentUser));
                String totalTime = Integer.toString(OnlineDbHelper.getTotalMinutesFromHistory(CurrentUser));

                // Update UI on the main thread after fetching data
                getActivity().runOnUiThread(() -> setWorkoutCount(totalWorkouts, totalTime));
            }).start();
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
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.searchButton) {
            tellParentToStartNewActivity(new WorkoutSearch());
        } else if (id == R.id.logoutButton) {
            Log.d("Home.onClick()", "Logout button clicked");
            showConfirmationLogout();
        }
    }

    // Method to dynamically edit workoutCountTextView
    public void setWorkoutCount(String val1, String val2) {
        //need to add check for other three values
        if (val1 != null) {
            workoutsNum.setText(val1);
            totalTimeNum.setText(val2 + " min");
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
        WorkoutOption parentFrag = (WorkoutOption) Home.this.getParentFragment();
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
}