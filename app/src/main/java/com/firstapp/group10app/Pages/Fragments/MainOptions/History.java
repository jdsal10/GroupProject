package com.firstapp.group10app.Pages.Fragments.MainOptions;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.firstapp.group10app.DB.DbHelper;
import com.firstapp.group10app.Other.ItemVisualiser;
import com.firstapp.group10app.Other.Session;
import com.firstapp.group10app.Pages.HistoryContinued;
import com.firstapp.group10app.R;

public class History extends Fragment implements View.OnClickListener{
    LinearLayout historyLayout;
    public Button viewAll;

    public History() {
        super(R.layout.activity_history);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_history, container, false);

        ScrollView historyScrollView = rootView.findViewById(R.id.historyElements);

        historyLayout = new LinearLayout(getContext());
        historyLayout.setOrientation(LinearLayout.VERTICAL);

        //Initialise Buttons
        viewAll = rootView.findViewById(R.id.goToViewAll);

        viewAll.setOnClickListener(this);

        historyScrollView.addView(historyLayout);

        try {
            String HistoryJSON = DbHelper.getUserWorkoutsLimited(Session.getUserEmail());
            if (HistoryJSON == null) {
                ItemVisualiser.showEmpty(historyLayout);
            } else {
                ItemVisualiser.startWorkoutGenerationLimiting(HistoryJSON, getContext(), historyLayout, "null", R.layout.popup_history, R.id.popupHistory);
                Log.d("History.java HistoryJSON", HistoryJSON);
            }
        } catch (Exception e) {
            // TODO: Add proper error handling
            throw new RuntimeException(e);
        }

        return rootView;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.goToViewAll) {
            System.out.println("being clicked");
            startNewActivityonHistory(new HistoryContinued());
        }
    }

    public void startNewActivityonHistory(AppCompatActivity newActivity) {
        WorkoutOption parentFrag = ((WorkoutOption) History.this.getParentFragment());
        if (parentFrag != null) {
            parentFrag.tellParentToStartNewActivity(newActivity, R.anim.slide_down_in, R.anim.slide_down_out);
        } else {
            startActivity(new Intent(getContext(), newActivity.getClass()));
        }
    }
}