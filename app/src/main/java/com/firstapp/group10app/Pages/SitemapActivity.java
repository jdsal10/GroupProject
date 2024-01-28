package com.firstapp.group10app.Pages;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.firstapp.group10app.ChatGPT.ChatGPT_Client;
//import com.firstapp.group10app.DB.LocalDb.LocalDb;
import com.firstapp.group10app.R;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SitemapActivity extends AppCompatActivity {
    private LinearLayout layout;
//    private LocalDb localDB;
    private final boolean chatGPT_switch1 = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sitemap);

        layout = findViewById(R.id.sitemap_layout);

        addText("Sitemap");

        separateElements();

        // Sprint 1 pages
        addText("Pages");
        addButton("MainActivity", MainActivity.class);
        addButton("Registration Page", Registration.class);
        addButton("Login Page", Login.class);
        addButton("Forgot Password Page", ForgotPassword.class);
        addButton("Forgot Password Check Page", forgotpasswordcheck.class);
        addButton("Forgot Password Continued Page", ForgotPasswordContinued.class);
        addButton("Home Page", Home.class);
        addButton("Workouts Page", Workouts.class);
        addButton("Settings Page", Settings.class);

        separateElements();

        // ChatGPT
        if (chatGPT_switch1 && chatGPT_switch2) {
            addText("ChatGPT");
            addChatGPT();
        } else {
            addText("ChatGPT functionality is disabled to prevent loss of credits.");
        }

        // LocalDB
        addText("LocalDB Test");
        Button insertButton = new Button(this);
        Button printButton = new Button(this);
        insertButton.setText("Insert Sample Data");
        printButton.setText("Print Data");
        insertButton.setTextSize(15);
        printButton.setTextSize(15);
        insertButton.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0.5f));
        printButton.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0.5f));
        layout.addView(insertButton);
        layout.addView(printButton);

        insertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                localDBInsert();
            }
        });

        printButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                localDBPrint();
            }
        });
    }

    private void addText(String text) {
        TextView textView = new TextView(this);
        textView.setText(text);
        textView.setTextSize(20);
        layout.addView(textView);
    }

    private void addButton(String text, final Class<?> cls) {
        Button button = new Button(this);
        button.setText(text);
        button.setTextSize(15);
        button.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0.5f));
        layout.addView(button);

        // Set the click listener to navigate to the corresponding page
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SitemapActivity.this, cls));
            }
        });
    }

    private void addChatGPT() {
        Button button = new Button(this);
        button.setText("ChatGPT Client");
        layout.addView(button);

        // Set the click listener to navigate to the corresponding page
        button.setOnClickListener(v -> {
            try {
                StringBuilder output = new StringBuilder();

                ExecutorService executor = Executors.newSingleThreadExecutor();
                executor.execute(() -> {
                    try {
                        output.append(ChatGPT_Client.chatGPT("Hello"));
//                                addPopUp(ChatGPT_Client.chatGPT("Hello"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });

                executor.shutdown();
                while (!executor.isTerminated()) {
                    // Wait for the executor to finish
                }

//                addPopUp(output.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }


    // Commented due to new popup used
    /*private void addPopUp(String text) {
        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.delete_account_confirm, null);

        // set the text
        TextView textView = findViewById(R.id.popup_text);
        textView.setText(text);

        // create the popup window
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        // show the popup window
        popupWindow.showAtLocation(layout, Gravity.CENTER, 0, 0);

        // dismiss the popup window when touched
        popupView.setOnTouchListener((v, event) -> {
            popupWindow.dismiss();
            return true;
        });
    }
    */


    private void separateElements() {
        View view = new View(this);
        view.setMinimumHeight(70);
        layout.addView(view);
    }

    private void localDBInsert() {
//        localDB = new LocalDb(this);
//        localDB.insertSampleData();
    }

//    private void localDBPrint() {
//        localDB.printDataForDebugging();
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        localDB.close();
    }

    private final boolean chatGPT_switch2 = false;
}