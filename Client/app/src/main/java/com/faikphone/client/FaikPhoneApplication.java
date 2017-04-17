package com.faikphone.client;

import android.app.Application;

import com.faikphone.client.AppPreferences;

/**
 * Created by dsm_025 on 2017-04-14.
 */

public class FaikPhoneApplication extends Application {
    private static AppPreferences sAppPreferences;

    @Override
    public void onCreate() {
        sAppPreferences = new AppPreferences(this);
        super.onCreate();
    }

    public static AppPreferences getAppPreferences() {
        return sAppPreferences;
    }
}
