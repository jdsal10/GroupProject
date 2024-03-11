package com.firstapp.group10app.Pages.Fragments.MainOptions;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import androidx.fragment.app.Fragment;

import com.firstapp.group10app.DB.DbHelper;
import com.firstapp.group10app.Other.ItemVisualiser;
import com.firstapp.group10app.Other.Session;
import com.firstapp.group10app.R;

public class History extends Fragment {
    LinearLayout historyLayout;

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

        historyScrollView.addView(historyLayout);

        try {
            String HistoryJSON = DbHelper.getUserWorkoutsLimited(Session.userEmail);
            if (HistoryJSON == null) {
                ItemVisualiser.showEmpty(historyLayout);
            } else {
                ItemVisualiser.startWorkoutGenerationLimiting(HistoryJSON, getContext(), historyLayout, "null", R.layout.popup_history, R.id.popupHistory);
                System.out.println(HistoryJSON);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return rootView;
    }
}