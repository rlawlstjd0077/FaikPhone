package com.faikphone.client;

import android.content.Intent;
import android.os.Build;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_MANAGE_OVERLAY_PERMISSION = 11;
    private AppPreferences appPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (checkDrawOverlayPermission()) {
            startService(new Intent(this, FakeStatusBarService.class));
        }

        this.appPreferences = FaikPhoneApplication.getAppPreferences();
        if(appPreferences.getPhoneMode()){  //realPhone
            getSupportActionBar().setTitle("FaikPhone(Real Mode)");
        }else{  //fakePhone
            getSupportActionBar().setTitle("FaikPhone(Fake Mode)");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_MANAGE_OVERLAY_PERMISSION:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (Settings.canDrawOverlays(this)) {
                        startService(new Intent(this, FakeStatusBarService.class));
                        finish();
                    }
                }
                break;
            default:
                break;
        }
    }

    public boolean checkDrawOverlayPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                startActivityForResult(
                        new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION), REQUEST_MANAGE_OVERLAY_PERMISSION);
                return false;
            }
        }

        return true;
    }
}