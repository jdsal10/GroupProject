package com.firstapp.group10app.Pages;

import static com.firstapp.group10app.Other.ItemVisualiserText.showTextJSON;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;

import com.firstapp.group10app.Other.Session;
import com.firstapp.group10app.R;

public class EnhanceResults extends Dialog implements View.OnClickListener {
    public EnhanceResults(Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.enhance_result);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.enhance_result);

        Button regenerate = findViewById(R.id.regenerateEnhance);
        regenerate.setOnClickListener(this);

        Button use = findViewById(R.id.useEnhance);
        use.setOnClickListener(this);

        LinearLayout show = findViewById(R.id.resultHolder);

        showTextJSON(getContext(), show, Session.getSelectedWorkout());
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.regenerateEnhance) {
            this.hide();
            EnhanceInput enhance = new EnhanceInput(getContext());
            enhance.show();
        }
        else if (id == R.id.useEnhance) {
            // Goes to WorkoutHub
            Intent intent = new Intent(getContext(), ActivityContainer.class);
            intent.putExtra("workoutHub", WorkoutHub.class);
            getContext().startActivity(intent);
        }
    }
}
