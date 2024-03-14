package com.firstapp.group10app;

import android.app.Application;

import com.firstapp.group10app.Other.ExceptionHandler;

public class HealthApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
    }
}