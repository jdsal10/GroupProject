package com.firstapp.group10app.Pages;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.firstapp.group10app.DB.DBConnection;
import com.firstapp.group10app.R;

import java.sql.ResultSet;
import java.sql.SQLException;

public class forgotpasswordcheck extends AppCompatActivity implements View.OnClickListener {
    private EditText code;
    private Button codeConfirm;
    private String email;
    ForgotPassword f = new ForgotPassword();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpasswordcheck);

        code = findViewById(R.id.codeenter);
        code.setOnClickListener(this);

        Button tryagain = findViewById(R.id.tryagain);
        tryagain.setOnClickListener(this);

        codeConfirm = findViewById(R.id.codeconfirm);
        codeConfirm.setOnClickListener(this);

        Bundle data = getIntent().getExtras();
        if (data != null) {
            email = data.getString("email");
        }
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.tryagain) {
            f.toSend(email, f.generateString());
            code.setText("Code has been resent");
        } else if (id == R.id.codeconfirm) {
            try {
                if (checkCode(code.getText().toString())) {
                    Intent in = new Intent(forgotpasswordcheck.this, ForgotPasswordContinued.class);
                    in.putExtra("email", email);
                    startActivity(in);
                } else {
                    code.setError("Invalid code");
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

    }

    public boolean checkCode(String code) throws SQLException {
        ResultSet set = DBConnection.executeQuery("SELECT VerifyCode FROM HealthData.Users WHERE Email = '" + email + "'");
        if (set.next()) {
            String storedCode = set.getString("VerifyCode");
            return storedCode.equals(code);
        } else {
            return false;
        }
    }

//    public String getCode(String email) {
//        String verifyCode = "";
//        try {
//            ResultSet set = d.executeQuery("SELECT VerifyCode FROM HealthData.Users WHERE `Email` = '" + email + "';");
//            if (set.next()) {
//                verifyCode = set.getString("VerifyCode");
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return verifyCode;
//    }

}