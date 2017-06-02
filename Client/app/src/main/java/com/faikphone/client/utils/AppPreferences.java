package com.faikphone.client.utils;

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
    public static final String KEY_REAL_PHONE_NUM = "realPhoneNum";
    public static final String KEY_FAKE_STATUS_BAR_MODE = "fakeStatusBarMode";
    public static final String KEY_DEVICE_PHONE_NUMBER = "deviceNum";

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

    public String getKeyRealPhoneNum(){
        return sharedPreferences.getString(KEY_REAL_PHONE_NUM, null);
    }

    public String getKeyDevicePhoneNumber(){
        return sharedPreferences.getString(KEY_DEVICE_PHONE_NUMBER, null);
    }

    public void setKeyCode(String code){
        editor.putString(KEY_AUTH_CODE, code);
        editor.commit();
    }

    public void setRealPhoneNum(String phoneNum){
        editor.putString(KEY_REAL_PHONE_NUM, phoneNum);
        editor.commit();
    }

    public void setKeyDevicePhoneNumber(String phoneNumber){
        editor.putString(KEY_DEVICE_PHONE_NUMBER, phoneNumber);
        editor.commit();
    }

    public boolean isFakeStatusBarMode() {
        return sharedPreferences.getBoolean(KEY_FAKE_STATUS_BAR_MODE, false);
    }

    public void setFakeStatusBarMode(boolean mode) {
        editor.putBoolean(KEY_FAKE_STATUS_BAR_MODE, mode).commit();
    }
}
