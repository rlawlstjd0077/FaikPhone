package com.faikphone.client.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.faikphone.client.R;
import com.faikphone.client.application.FaikPhoneApplication;
import com.faikphone.client.service.FakeStatusBarService;
import com.faikphone.client.utils.AppPreferences;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_MANAGE_OVERLAY_PERMISSION = 11;
    private static final int PERMISSIONS_REQUEST_READ_PHONE_STATE = 21;
    private static final int PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 22;
    private static final int PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 23;

    private AppPreferences appPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (checkDrawOverlayPermission()) {
            startService(new Intent(this, FakeStatusBarService.class));
        }

        checkPermission(Manifest.permission.READ_PHONE_STATE, PERMISSIONS_REQUEST_READ_PHONE_STATE);
        checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE, PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
        checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);

        this.appPreferences = FaikPhoneApplication.getAppPreferences();
        if (appPreferences.getPhoneMode()) {  // fakePhone
            getSupportActionBar().setTitle("FaikPhone(Fake Mode)");
        } else {  // realPhone
            getSupportActionBar().setTitle("FaikPhone(Real Mode)");
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_READ_PHONE_STATE:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {
                    Toast.makeText(this, "Fake Call 기능을 사용하기 위해서는 전화 권한이 필요합니다.\n" +
                    "설정에서 권한을 허용해주세요.", Toast.LENGTH_LONG).show();
                }
                break;
            case PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
            case PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE:
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

    public void checkReadPhoneStatePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_PHONE_STATE)) {

            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_PHONE_STATE},
                        PERMISSIONS_REQUEST_READ_PHONE_STATE);
            }
        }
    }

    public void checkPermission(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(this, permission)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {

            } else {
                ActivityCompat.requestPermissions(this, new String[]{permission}, requestCode);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    public void onClickSettings(MenuItem menuItem) {
        Intent intent = new Intent(this, SettingsActivity.class);
        intent.putExtra(PreferenceActivity.EXTRA_SHOW_FRAGMENT, SettingsActivity.GeneralPreferenceFragment.class.getName());
        intent.putExtra(PreferenceActivity.EXTRA_NO_HEADERS, true);
        try {
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}