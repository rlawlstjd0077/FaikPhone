package com.faikphone.client;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

/**
 * Created by dsm_025 on 2017-04-18.
 */

public class CallActivity extends AppCompatActivity{
    private TextView nameTV;
    private TextView numberTV;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_call);
        super.onCreate(savedInstanceState);

        nameTV = (TextView) findViewById(R.id.tv_name);
        numberTV = (TextView) findViewById(R.id.tv_number);
        getSupportActionBar().hide();

        Bundle bundle = getIntent().getExtras();
        numberTV.setText(bundle.getShort("number"));
    }
}
