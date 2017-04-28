package com.faikphone.client;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.SwitchPreference;
import android.support.v7.app.ActionBar;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.RingtonePreference;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import java.util.List;

public  class SettingsActivity extends AppCompatPreferenceActivity {
    /**
     * Helper method to determine if the device has an extra-large screen. For
     * example, 10" tablets are extra-large.
     */
    private static boolean isXLargeTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_XLARGE;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onIsMultiPane() {
        return isXLargeTablet(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void onBuildHeaders(List<Header> target) {
        loadHeadersFromResource(R.xml.pref_headers, target);
    }

    /**
     * This method stops fragment injection in malicious applications.
     * Make sure to deny any unknown fragments here.
     */
    protected boolean isValidFragment(String fragmentName) {
        return PreferenceFragment.class.getName().equals(fragmentName)
                || GeneralPreferenceFragment.class.getName().equals(fragmentName);
    }

    /**
     * This fragment shows general preferences only. It is used when the
     * activity is showfing a two-pane settings UI.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class GeneralPreferenceFragment extends PreferenceFragment {

        // fakeMode : true is fake mode, false is real mode.
        public static boolean isFake;

        SwitchPreference switchModeReference;

        // fake is try to connect, real is view code.
        EditTextPreference connectionPreference;
        // fake is disconnect, real is same too
        Preference refreshPreference;
        //
        SwitchPreference changeBarPreference;


        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_general);
            setHasOptionsMenu(true);

            switchModeReference = (SwitchPreference) findPreference("fake_switch");
            switchModeReference.setOnPreferenceChangeListener(fakeSwitchChangeListener);

            connectionPreference = (EditTextPreference) findPreference("fake_connection");
            connectionPreference.setOnPreferenceClickListener(connectionClickListener);

            refreshPreference = (Preference) findPreference("fake_refresh");
            refreshPreference.setOnPreferenceClickListener(refreshClickListener);

            changeBarPreference = (SwitchPreference) findPreference("fake_statusbar");

            // TODO: 설정 파일에서 불러올 수 있도록 하기.
            isFake = switchModeReference.isChecked();
            switchModeReference.setChecked(isFake);
            ChangePreferences();
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                startActivity(new Intent(getActivity(), SettingsActivity.class));
                return true;
            }
            return super.onOptionsItemSelected(item);
        }

        private Preference.OnPreferenceChangeListener fakeSwitchChangeListener = new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                // TODO: 공기계 -> 본 핸드폰, 또는 그 반대 모드로 넘어갈 때 이전의 기기와의 연결을 끊기

                // if(job is done){
                isFake = (boolean) newValue;
                ChangePreferences();
                // }

                return true;
            }
        };

        private Preference.OnPreferenceClickListener refreshClickListener = new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                String message;
                if(isFake){
                    message = getResources().getString(R.string.refresh_alert_fake);
                }else{
                    message = getResources().getString(R.string.refresh_alert_real);
                }

                new AlertDialog.Builder(getActivity()).setMessage(message)
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                RefreshConnection();
                            }
                        })
                        .setNegativeButton("취소", null)
                        .show();

                return true;
            }
        };

        private EditTextPreference.OnPreferenceClickListener connectionClickListener = new EditTextPreference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                if(isFake){
                    connectionPreference.setDialogTitle(R.string.pref_connection_dialog_fake);
                }else{
                    connectionPreference.setDialogTitle(R.string.pref_connection_dialog_real);
                    // TODO: 인증코드 불러오는 로직 작성하기
                }

                return true;
            }
        };

        private void ChangePreferences(){
            if(isFake){
                connectionPreference.setTitle(R.string.pref_connection_fake);
                connectionPreference.setDialogTitle(R.string.pref_connection_dialog_fake);
                refreshPreference.setTitle(R.string.pref_refresh_fake);
                changeBarPreference.setEnabled(true);
            }else{
                connectionPreference.setTitle(R.string.pref_connection_real);
                connectionPreference.setDialogTitle(R.string.pref_connection_dialog_real);
                refreshPreference.setTitle(R.string.pref_refresh_real);
                changeBarPreference.setEnabled(false);
            }
        }

        private void RefreshConnection(){
            if(isFake){
                // TODO: 연결 끊기 작업 수행
            }else{
                // TODO: 인증 코드 재발급 작업 수행
            }
        }
    }
}
