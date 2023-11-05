package com.firstapp.group10app;

import java.lang.*;
import android.os.Bundle;
import android.view.View;
import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.*;

public class forgotPassword extends AppCompatActivity implements View.OnClickListener {

    private EditText emailToSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        emailToSend = findViewById(R.id.editTextTextEmailAddress);
        emailToSend.setOnClickListener(this);

        Button sendEmail = findViewById(R.id.sendEmail);
        sendEmail.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.sendEmail) {
            if (!(emailToSend.getText().toString().equals(""))) {
                //Send email
                try {
                    toSend();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

            }
        }
    }

    public void toSend() {
        try {
            String testEmailToSend = "noreplyhealthapp@gmail.com";

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
            mimeMessage.setText("Hi " + emailToSend.getText().toString() + "." +
                    "A request was recently made to reset." +
                    "If you didn't send a request, please ignore this email and check your " +
                    "account security." +
                    "" +
                    " myapp://test/somepath " +
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