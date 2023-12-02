package com.firstapp.group10app.Pages;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.firstapp.group10app.R;
public class SitemapActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sitemap);

        LinearLayout layout = findViewById(R.id.sitemap_layout);

        // Add a button for each page in your app
        addButton(layout, "Workouts Page", Registration.class);
    }

    private void addButton(LinearLayout layout, String text, final Class<?> cls) {
        Button button = new Button(this);
        button.setText(text);
        layout.addView(button);

        // Set the click listener to navigate to the corresponding page
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SitemapActivity.this, cls));
            }
        });
    }
}