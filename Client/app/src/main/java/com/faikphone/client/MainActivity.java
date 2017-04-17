package com.faikphone.client;

import android.content.Intent;
import android.os.Build;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import at.markushi.ui.CircleButton;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_MANAGE_OVERLAY_PERMISSION = 11;
    private AppPreferences appPreferences;

    @BindView(R.id.excuteButton) CircleButton excuteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.appPreferences = FaikPhoneApplication.getAppPreferences();
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        excuteButton.setVisibility(appPreferences.getPhoneMode() ? View.GONE : View.VISIBLE);
        if(appPreferences.getPhoneMode()){  //realPhone
            getSupportActionBar().setTitle("FaikPhone(Real Mode)");
        }else{  //fakePhone
            getSupportActionBar().setTitle("FaikPhone(Fake Mode)");
        }
    }

    @OnClick ({R.id.excuteButton})
    public void onClick(View view){
        if(checkDrawOverlayPermission()) {
            startService(new Intent(this, FakeStatusBarService.class));
            finish();
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