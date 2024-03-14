package com.firstapp.group10app.Other;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.firstapp.group10app.Pages.MainActivity;

public class ExceptionHandler implements Thread.UncaughtExceptionHandler {
    private Context context;

    public ExceptionHandler(Context context) {
        this.context = context;
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        Log.e("Error", ex.getMessage(), ex);
        Toast.makeText(context, "An error occurred. You are being logged out.", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
        System.exit(0);
    }
}