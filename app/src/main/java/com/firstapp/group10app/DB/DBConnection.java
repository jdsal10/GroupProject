package com.firstapp.group10app.DB;

import android.os.StrictMode;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class DBConnection {
    public Connection conn;
    public Statement st;
    public DBConnection() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try {
            String connectionString = "jdbc:mysql://gateway01.eu-central-1.prod.aws.tidbcloud.com:4000/test?user=4JhVNGoguqAHant.root&password=1uTxHEUpEVM4ex04&sslMode=VERIFY_IDENTITY&enabledTLSProtocols=TLSv1.2,TLSv1.3";
            conn = DriverManager.getConnection(connectionString);
            st = conn.createStatement();
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public void executeStatement(String createStatement) {
        try{
            st.execute(createStatement);
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}