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
    DBConnection d = new DBConnection();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        emailToSend = findViewById(R.id.emailtosend);
        emailToSend.setOnClickListener(this);

        Button sendEmail = findViewById(R.id.passwordchange);
        sendEmail.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
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
                        toSend(emailText,validate);
                        d.executeStatement("UPDATE HealthData.Users " +
                                "SET VerifyCode = '" + validate + "' " +
                                "WHERE Email = '" + emailText + "';");

                        Intent in = new Intent(ForgotPassword.this, forgotpasswordcheck.class);
                        in.putExtra("email", emailText);
                        System.out.println("starting");
                        startActivity(in);

                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }

                } else {
                    emailToSend.setError("The email provided is not valid. Please try again.");
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public boolean checkExists(String email) throws SQLException {
        DBConnection d = new DBConnection();
        ResultSet set = d.executeQuery("SELECT * FROM HealthData.Users WHERE Email = '" + email + "'");
        int size = 0;
        if (set.last()) {
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

    public void toSend(String email, String code) {
        try {
            String testEmailToSend = email;

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
            mimeMessage.setText("Hi " + email + " .\n" +
                    "A request was recently made to reset.\n" +
                    "If you didn't send a request, please ignore this email and check your " +
                    "account security.\n" +
                    "Your code: \n" +
                    code +
                    "\n" +
                    "Many Thanks,\n" +
                    "The Health App Team\n");

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