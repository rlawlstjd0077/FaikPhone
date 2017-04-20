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

    public static final String KEY_MODE = "mode";
    public static final String KEY_CODE = "code";

    public AppPreferences(Context context){
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        this.editor = sharedPreferences.edit();
        this.context = context;
    }

    public boolean getPhoneMode(){
        return sharedPreferences.getBoolean(KEY_MODE, false);
    }

    public void setPhoneMode(boolean state){
        editor.putBoolean(KEY_MODE, state);
        editor.commit();
    }

    public String getKeyCode(){
        return sharedPreferences.getString(KEY_CODE, null);
    }

    public void setKeyCode(String code){
        editor.putString(KEY_CODE, code);
        editor.commit();
    }


}
