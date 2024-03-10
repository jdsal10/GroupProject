package com.firstapp.group10app.Other;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.MenuItem;
import android.widget.Toast;

import com.firstapp.group10app.DB.DbConnection;
import com.firstapp.group10app.Pages.ActivityContainer;
import com.firstapp.group10app.R;

public class NavBarBehaviour {
    public static boolean onNavigationItemSelected(MenuItem item, Context context, Activity activity) {
        int id = item.getItemId();
        if (id == R.id.goToHome) {
            Intent intent = new Intent(context, ActivityContainer.class);
            ActivityContainer.currentView = R.layout.activity_home;
            activity.startActivity(intent);

            return true;
        } else if (id == R.id.goToWorkouts) {
            Intent intent = new Intent(context, ActivityContainer.class);
            ActivityContainer.currentView = R.layout.activity_workout_option;
            activity.startActivity(intent);

            return true;
        } else if (id == R.id.goToHistory) {
            if (!DbConnection.testConnection()) {
                Toast.makeText(activity, "No connection!", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(context, ActivityContainer.class);
                ActivityContainer.currentView = R.layout.activity_history;
                activity.startActivity(intent);

                return true;
            }
        }
        return true;
    }
}
