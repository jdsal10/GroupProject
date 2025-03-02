package com.firstapp.group10app.Pages.Fragments.MainOptions;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.firstapp.group10app.DB.OnlineDb.OnlineDbHelper;
import com.firstapp.group10app.Other.ItemVisualiser;
import com.firstapp.group10app.Other.Session;
import com.firstapp.group10app.Pages.ActivityContainer;
import com.firstapp.group10app.Pages.HistoryContinued;
import com.firstapp.group10app.R;

import org.json.JSONException;

public class History extends Fragment implements View.OnClickListener {
    private LinearLayout historyLayout;
    private ExecutorService executor;
    private Handler handler;

    public History() {
        super(R.layout.activity_history);
        executor = Executors.newSingleThreadExecutor();
        handler = new Handler(Looper.getMainLooper());
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Behaviour when the user is anonymous
        if (!Session.getSignedIn()) {
            Log.d("History.java", "User is not signed in. This page should not be accessible.");
            tellParentToFinish();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_history, container, false);

        ScrollView historyScrollView = rootView.findViewById(R.id.historyElements);

        historyLayout = new LinearLayout(getContext());
        historyLayout.setOrientation(LinearLayout.VERTICAL);

        //Initialise Buttons
        Button viewAll = rootView.findViewById(R.id.goToViewAll);

        viewAll.setOnClickListener(this);

        historyScrollView.addView(historyLayout);

        // Get the user's workout history (done in a separate thread)

        return rootView;
    }

    private void refreshUI() {
        historyLayout.removeAllViews();
        executor.execute(() -> {
            try {
                final String result = OnlineDbHelper.getUserWorkoutsLimited(Session.getUserEmail());
                System.out.println("fixing history: " + result);
                handler.post(() -> {
                    if (result == null) {
                        ItemVisualiser.showEmpty(historyLayout);
                    } else {
                        try {
                            ItemVisualiser.startWorkoutGenerationLimiting(result, getContext(), historyLayout, "history", R.layout.popup_history, R.id.popupHistory);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                        Log.d("History.java HistoryJSON", result);
                    }
                });
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        // Refresh UI when the activity is resumed
        refreshUI();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.goToViewAll) {
            System.out.println("being clicked");
            startNewActivityOnHistory(new HistoryContinued());
        }
    }

    public void startNewActivityOnHistory(AppCompatActivity newActivity) {
        Workout parentFrag = ((Workout) History.this.getParentFragment());
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