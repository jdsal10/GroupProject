package com.firstapp.group10app.Pages;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.firstapp.group10app.DB.OnlineDb.OnlineDbHelper;
import com.firstapp.group10app.Other.Validator;
import com.firstapp.group10app.R;

public class ForgotPasswordContinued extends AppCompatActivity implements View.OnClickListener {
    private EditText password1, password2;
    private Button passwordchangeconfirm;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password_continued);

        password1 = findViewById(R.id.newPasswordLoggedIn1);
        password1.setOnClickListener(this);

        password2 = findViewById(R.id.newPasswordLoggedIn2);
        password2.setOnClickListener(this);

        passwordchangeconfirm = findViewById(R.id.changePasswordButton);
        passwordchangeconfirm.setOnClickListener(this);

        Button backToLogin = findViewById(R.id.backToLogin);
        backToLogin.setOnClickListener(this);

        Bundle data = getIntent().getExtras();
        if (data != null) {
            email = data.getString("email");
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.changePasswordButton) {
            if ((password1 != null) && (!password2.getText().toString().equals(password1.getText().toString()))) {
                passwordchangeconfirm.setError("The passwords do not match");
            } else if (Validator.passwordValidator(password1.getText().toString()) == null) {
                OnlineDbHelper.changeUserPassword(email, password1.getText().toString());
                Intent t = new Intent(ForgotPasswordContinued.this, Login.class);
                startActivity(t);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            } else {
                password1.setError("Invalid password");
            }
        } else if (id == R.id.backToLogin) {
            startActivity(new Intent(getApplicationContext(), Login.class));
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        }
    }
}