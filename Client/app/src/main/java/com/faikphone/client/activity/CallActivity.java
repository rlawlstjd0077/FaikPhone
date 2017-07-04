package com.faikphone.client.activity;

import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.faikphone.client.R;
import com.faikphone.client.utils.VibrateManager;

/**
 * Created by dsm_025 on 2017-04-18.
 */

public class CallActivity extends AppCompatActivity{
    private TextView nameTV;
    private TextView numberTV;
    private Thread thread;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_call);
        super.onCreate(savedInstanceState);

        nameTV = (TextView) findViewById(R.id.tv_name);
        numberTV = (TextView) findViewById(R.id.tv_number);
        getSupportActionBar().hide();

        Bundle bundle = getIntent().getExtras();
        String number = bundle.getString("number");
        StringBuffer sb = new StringBuffer(number);
        sb.insert(3, "-");
        sb.insert(8, "-");
        numberTV.setText(sb);
        if(bundle.getString("name") != null){
            nameTV.setText(bundle.getString("name"));
        }

        thread = new VibrateManager((Vibrator) getSystemService(VIBRATOR_SERVICE));
        thread.start();
    }

    @Override
    public void onBackPressed() {
        return;
    }

    @Override
    protected void onStop() {
        thread.interrupt();
        super.onStop();
    }
}