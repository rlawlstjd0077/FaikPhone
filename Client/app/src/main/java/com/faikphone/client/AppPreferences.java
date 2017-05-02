package com.faikphone.client;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by dsm_025 on 2017-04-14.
 */

public class AppPreferences {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Context context;

    public static final String KEY_PHONE_MODE = "phoneMode";    // true: 공기계, false: 본 핸드폰
    public static final String KEY_AUTH_CODE = "authCode";
    public static final String KEY_FAKE_STATUS_BAR_MODE = "fakeStatusBarMode";

    public AppPreferences(Context context){
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        this.editor = sharedPreferences.edit();
        this.context = context;
    }

    public boolean getPhoneMode(){
        return sharedPreferences.getBoolean(KEY_PHONE_MODE, false);
    }

    public void setPhoneMode(boolean state){
        editor.putBoolean(KEY_PHONE_MODE, state);
        editor.commit();
    }


    public String getKeyCode(){
        return sharedPreferences.getString(KEY_AUTH_CODE, null);
    }

    public void setKeyCode(String code){
        editor.putString(KEY_AUTH_CODE, code);
        editor.commit();
    }

    public boolean isFakeStatusBarMode() {
        return sharedPreferences.getBoolean(KEY_FAKE_STATUS_BAR_MODE, false);
    }

    public void setFakeStatusBarMode(boolean mode) {
        editor.putBoolean(KEY_FAKE_STATUS_BAR_MODE, mode).commit();
    }
}
