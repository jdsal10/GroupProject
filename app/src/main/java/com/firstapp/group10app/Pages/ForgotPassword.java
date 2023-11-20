package com.firstapp.group10app.Pages;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import androidx.appcompat.app.AppCompatActivity;

import com.firstapp.group10app.R;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.firstapp.group10app.DB.DBConnection;


public class ForgotPassword extends AppCompatActivity implements View.OnClickListener {
    private EditText emailToSend;
    private String emailText;
    private String validate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        emailToSend = findViewById(R.id.emailtosend);
        emailToSend.setOnClickListener(this);

        Button sendEmail = findViewById(R.id.passwordchange);
        sendEmail.setOnClickListener(this);
//        System.out.println(generateString());
    }

    @Override
    public void onClick(View v) {
        DBConnection d = new DBConnection();
        d.executeStatement("INSERT INTO HealthData.Users` (\n" +
                "    `Email`,\n" +
                "    `PreferredName`,\n" +
                "    `Password`,\n" +
                "    `DOB`,\n" +
                "    `Weight`,\n" +
                "    `Height`,\n" +
                "    `Sex`,\n" +
                "    `HealthCondition`,\n" +
                "    `ReasonForDownloading`\n" +
                "  )\n" +
                "VALUES\n" +
                "  (\n" +
                "    'teatherethan@gmail.com', \n" +
                "    'John Doe',\n" +
                "    'password123',\n" +
                "    '1990-01-01',\n" +
                "    70.5,\n" +
                "    180.0,\n" +
                "    'Male',\n" +
                "    'No health conditions',\n" +
                "    'Fitness goals'\n" +
                "  );");
        int id = v.getId();
        if (id == R.id.passwordchange) {
            emailText = emailToSend.getText().toString();
            String pat = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
            Pattern pattern = Pattern.compile(pat);
            Matcher matcher = pattern.matcher(emailText);
            try {
                if ((!(emailText.equals(""))) && (matcher.matches()) && checkExists(emailText)) {
                    try {
                        validate = generateString();
                        toSend(validate);
                        //Whilst the function to return to the app from the emails is still in
                        //progress, the app currently, bypasses it and send the email as intent,
                        //the same way the functional system will.

                        Intent in = new Intent(ForgotPassword.this, ForgotPasswordContinued.class);
                        in.putExtra("email", emailText);
                        System.out.println("starting");
                        startActivity(in);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }

                } else {
                    //Incorrect format of email - tell the user.
                    emailToSend.setError("The email provided is not valid. Please try again.");
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public boolean checkExists (String email) throws SQLException {
        DBConnection d = new DBConnection();
        ResultSet set = d.executeQuery("SELECT * FROM HealthData.Users WHERE Email = '" + email + "'");
        int size = 0;
        if(set.last()) {
            size++;
        }
        return size != 0;
    }


    public String generateString() {
        String randomOptions = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        Random r = new Random();
        char[] chars = new char[8];
        for(int i = 0; i < 8; i++) {
            chars[i] = randomOptions.charAt(r.nextInt(randomOptions.length()));
        }
        return new String(chars);
    }

    public void toSend(String str) {
        try {
            String testEmailToSend = emailText;

            String stringHost = "smtp.gmail.com";

            Properties properties = System.getProperties();

            properties.put("mail.smtp.host", stringHost);
            properties.put("mail.smtp.port", "465");
            properties.put("mail.smtp.ssl.enable", "true");
            properties.put("mail.smtp.auth", "true");

            javax.mail.Session session = Session.getInstance(properties, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication("noreplyhealthapp@gmail.com", "rocbljtgnqaaroet"
                    );
                }
            });

            MimeMessage mimeMessage = new MimeMessage(session);
            mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(testEmailToSend));

            mimeMessage.setSubject("Health App Password Reset");
            mimeMessage.setText("Hi " + emailToSend.getText().toString() + " . " +
                    "A request was recently made to reset." +
                    "If you didn't send a request, please ignore this email and check your " +
                    "account security." +
                    "" +
                    str +
                    "" +
                    "Many Thanks," +
                    "The Health App Team");

            Thread thread = new Thread(() -> {
                try {
                    Transport.send(mimeMessage);
                } catch (MessagingException e) {
                    e.printStackTrace();
                }
            });
            thread.start();

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}