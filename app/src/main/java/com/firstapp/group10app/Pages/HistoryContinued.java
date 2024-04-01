package com.firstapp.group10app.Pages;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.firstapp.group10app.DB.DatabaseManager;
import com.firstapp.group10app.DB.OnlineDb.OnlineDbHelper;
import com.firstapp.group10app.Other.ItemVisualiser;
import com.firstapp.group10app.Other.Session;
import com.firstapp.group10app.R;

public class HistoryContinued extends AppCompatActivity implements View.OnClickListener {
    private ImageButton backButton;
    private Button clearHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Behaviour when the user is signed in
        if (Session.getSignedIn()) {
            setContentView(R.layout.activity_history_continued);

            backButton = findViewById(R.id.backButton);
            backButton.setOnClickListener(this);

            clearHistory = findViewById(R.id.clearHistorybtn);
            clearHistory.setOnClickListener(this);

            ScrollView historyContinuedScrollView = findViewById(R.id.historyElementsContinued);
            LinearLayout continuedLayout = new LinearLayout(this);
            continuedLayout.setOrientation(LinearLayout.VERTICAL);

            historyContinuedScrollView.addView(continuedLayout);

            try {
                // gets the workouts user has done, specific to the user
                String HistoryContinuedJSON = OnlineDbHelper.getUserWorkouts(Session.getUserEmail());
                ItemVisualiser.startWorkoutGeneration(HistoryContinuedJSON, this, continuedLayout, "tt", R.layout.popup_history, R.id.popupHistory);
            } catch (Exception e) {
                // TODO: handle exception better
                throw new RuntimeException(e);
            }
        }

        // Behaviour when the user is anonymous
        else {
            Log.e("HistoryContinued", "User is not signed in. This page should not be accessible.");
            Session.logout(this, "This page should not be accessible. You are being logged out");
            finish();
        }
    }

    private void clearHistoryPopUp(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.popup_confirm_delete_history, null);
        builder.setView(dialogView);

        Button btnConfirm = dialogView.findViewById(R.id.btn_confim);
        EditText passwordClear = dialogView.findViewById(R.id.PasswordDeleteHistory);

        AlertDialog dialog = builder.create();

        btnConfirm.setOnClickListener(v ->  {
            String password = passwordClear.getText().toString();
            OnlineDbHelper db = new OnlineDbHelper();
            try{
                if (db.checkUserExistsAndCorrectPassword(Session.getUserEmail(), password)){
                    DatabaseManager.getInstance().deleteHistory(Session.getUserEmail());
                    System.out.println("clearing history of " + Session.getUserEmail());
                    Toast.makeText(HistoryContinued.this, "History cleared", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    recreate();
                }else {
                    passwordClear.setError("Incorrect password used.");
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
//
        });

        dialog.show();

    }

    @Override
    public void onClick(View v) {
        if (v == backButton) {
            finish();
        } else if(v == clearHistory){
            System.out.println("hello");
            clearHistoryPopUp();
        }
    }
}