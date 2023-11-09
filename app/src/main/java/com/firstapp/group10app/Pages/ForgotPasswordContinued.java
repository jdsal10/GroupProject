package com.firstapp.group10app.Pages;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.firstapp.group10app.DB.DBConnection;
import com.firstapp.group10app.R;

public class ForgotPasswordContinued extends AppCompatActivity implements View.OnClickListener {
    private Uri uri;
    private EditText password1;
    private EditText password2;

    private Button passwordchangeconfirm;

    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password_continued);


//        Uri uri = getIntent().getData();
//        if (uri != null) {
//            String host = uri.getHost();
//            String path = uri.getPath();
//            System.out.println(host + " " + path);
//
//            // Handle the deep link based on the host and path
//        }
//        // ATTENTION: This was auto-generated to handle app links.
//        Intent appLinkIntent = getIntent();
//        String appLinkAction = appLinkIntent.getAction();
//        Uri appLinkData = appLinkIntent.getData();

        password1 = findViewById(R.id.newpassword1);
        password1.setOnClickListener(this);

        password2 = findViewById(R.id.newpassword2);
        password2.setOnClickListener(this);

        passwordchangeconfirm = findViewById(R.id.passwordchange);
        passwordchangeconfirm.setOnClickListener(this);

        Bundle data = getIntent().getExtras();
        if (data != null) {
            email = data.getString("email");
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.passwordchange) {
            if (!password2.getText().toString().equals(password1.getText().toString())) {
                passwordchangeconfirm.setError("The passwords do not match");
            }

            DBConnection d = new DBConnection();
            d.executeStatement("UPDATE HealthData.USER_TABLE SET Password = '" + password1.getText().toString() + "' WHERE Email = '" + email + "';");
            System.out.println("The password was change to " + password1.getText().toString() + " were emeial " + email);
        }
    }
}