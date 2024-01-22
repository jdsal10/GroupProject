package com.firstapp.group10app.Pages;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import com.firstapp.group10app.R;

public class workout_option extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    public RadioButton AISelect;
    public RadioButton manualSelect;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_option);

        // Initialize RadioButtons
        AISelect = findViewById(R.id.AI);
        manualSelect = findViewById(R.id.manual);

        // Set OnCheckedChangeListener
        AISelect.setOnCheckedChangeListener(this);
        manualSelect.setOnCheckedChangeListener(this);
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(isChecked) {
            if(buttonView.getId() == R.id.AI) {
                System.out.println("AI SELECT");
            }
            else if(buttonView.getId() == R.id.manual) {
            System.out.println("MAN SELECT");
        }}
    }


}