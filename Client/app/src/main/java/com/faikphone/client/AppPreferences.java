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

    public static final String KeyMode = "mode";
    public static final String KeyCode = "code";

    public AppPreferences(Context context){
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        this.editor = sharedPreferences.edit();
        this.context = context;
    }

    public boolean getPhoneMode(){
        return sharedPreferences.getBoolean(KeyMode, false);
    }

    public void setKeyMode(boolean state){
        editor.putBoolean(KeyMode, state);
        editor.commit();
    }

    public String getKeyCode(){
        return sharedPreferences.getString(KeyCode, null);
    }

    public void setKeyCode(String code){
        editor.putString(KeyCode, code);
        editor.commit();
    }
}
