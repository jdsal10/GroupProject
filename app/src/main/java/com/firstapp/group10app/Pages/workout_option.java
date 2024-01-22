package com.firstapp.group10app.Pages;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import com.firstapp.group10app.R;

public class workout_option extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    public RadioButton AISelect;
    public RadioButton manualSelect;
    public LinearLayout aiView, manualView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_option);

        // Initialize RadioButtons
        AISelect = findViewById(R.id.toggleAI);
        manualSelect = findViewById(R.id.toggleManual);

        // Set OnCheckedChangeListener
        AISelect.setOnCheckedChangeListener(this);
        manualSelect.setOnCheckedChangeListener(this);

        aiView = findViewById(R.id.aiView);
        manualView = findViewById(R.id.manualView);
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            if (buttonView.getId() == R.id.toggleAI) {
                System.out.println("AI SELECT");
                manualView.setVisibility(View.GONE);
                aiView.setVisibility(View.VISIBLE);

            } else if (buttonView.getId() == R.id.toggleManual) {
                System.out.println("MAN SELECT");
                aiView.setVisibility(View.GONE);
                manualView.setVisibility(View.VISIBLE);
            }
        }
    }


}