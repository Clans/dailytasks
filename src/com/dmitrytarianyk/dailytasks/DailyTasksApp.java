package com.dmitrytarianyk.dailytasks;

import android.app.Application;

import com.dmitrytarianyk.dailytasks.util.PreferenceHelper;

public class DailyTasksApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        PreferenceHelper.init(this);
    }
}
