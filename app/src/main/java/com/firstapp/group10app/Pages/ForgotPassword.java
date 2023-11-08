package com.firstapp.group10app.Pages;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.firstapp.group10app.R;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
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
import javax.xml.parsers.DocumentBuilderFactory;
import com.firstapp.group10app.DB.DBConnection;


public class ForgotPassword extends AppCompatActivity implements View.OnClickListener {
    private EditText emailToSend;
    private String emailText;

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
            emailText = emailToSend.getText().toString();
            Pattern pattern;
            Matcher matcher;
            String pat = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
            pattern = Pattern.compile(pat);
            matcher = pattern.matcher(emailText);
            if ((!(emailText.equals(""))) && (matcher.matches())) {
                //Try to send email
                try {
                    toSend();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

            } else {
                //Incorrect format of email - tell the user.
                emailToSend.setError("Please add a valid email");
            }
        }
    }

    public boolean checkExists (String email) throws SQLException {
        DBConnection d = new DBConnection();
        ResultSet set = d.executeQuery("SELECT * FROM USER_TABLE WHERE Email = '" + email + "'");
        if(set.next()) {
            return true;
        }
        else return true;
    }

    public void toSend() {
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