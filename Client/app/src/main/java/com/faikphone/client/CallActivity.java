package com.faikphone.client;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by dsm_025 on 2017-04-18.
 */

public class CallActivity extends AppCompatActivity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_call);
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
    }
}
