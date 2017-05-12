package com.faikphone.client;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceScreen;
import android.preference.SwitchPreference;
import android.support.annotation.RequiresApi;
import android.preference.PreferenceFragment;
import android.support.v7.app.AlertDialog;
import android.view.MenuItem;
import android.widget.Toast;

import com.faikphone.client.network.FakeHttpClient;
import com.faikphone.client.network.HttpClient;
import com.faikphone.client.network.RealHttpClient;
import com.google.firebase.iid.FirebaseInstanceId;

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
        private HttpClient httpClient;

        private AppPreferences mAppPrefs;

        // fakeMode : true is fake mode, false is real mode.
        public static boolean isFake;

        PreferenceScreen prefer_screen;

        SwitchPreference switchModeReference;

        EditTextPreference fakeConnectionPreference;

        Preference fakeRefreshPreference;

        SwitchPreference fakeChangeBarPreference;

        Preference realCodeViewPreference;

        Preference realCodeRefreshPreference;

        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            mAppPrefs = new AppPreferences(getActivity());
            httpClient = new RealHttpClient(getActivity());
            addPreferencesFromResource(R.xml.pref_general);
            setHasOptionsMenu(true);

            switchModeReference = (SwitchPreference) findPreference("fake_switch");
            switchModeReference.setOnPreferenceChangeListener(fakeSwitchChangeListener);
            switchModeReference.setChecked(mAppPrefs.getPhoneMode());

            prefer_screen = (PreferenceScreen) findPreference("prefer_screen");

            // Fake Preferences
            fakeConnectionPreference = (EditTextPreference)findPreference("fake_connection");
            fakeConnectionPreference.setOnPreferenceChangeListener(fakeConnectionChangeListener);

            fakeRefreshPreference = findPreference("fake_refresh");
            fakeRefreshPreference.setOnPreferenceClickListener(fakeRefreshClickListener);

            fakeChangeBarPreference = (SwitchPreference) findPreference("fake_statusbar");
            fakeChangeBarPreference.setOnPreferenceChangeListener(fakeChangeBarChangeListener);
            fakeChangeBarPreference.setChecked(mAppPrefs.isFakeStatusBarMode());

            //Real Preferences

            realCodeViewPreference = findPreference("real_code_view");
            realCodeViewPreference.setOnPreferenceClickListener(realCodeVIewListener);

            realCodeRefreshPreference = findPreference("real_code_refresh");
            realCodeRefreshPreference.setOnPreferenceClickListener(realCodeRefreshListener);

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
                mAppPrefs.setPhoneMode(isFake);
                ChangePreferences();
                // }
                getActivity().sendBroadcast(new Intent(getString(R.string.preferences_changed_broadcast)));

                return true;
            }
        };

        private Preference.OnPreferenceClickListener fakeRefreshClickListener = new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                String message = getResources().getString(R.string.refresh_alert_fake);

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
        private EditTextPreference.OnPreferenceChangeListener fakeConnectionChangeListener = new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                fakeConnectionPreference.setDialogTitle(R.string.pref_connection_dialog_fake);
                httpClient.doRegister(FirebaseInstanceId.getInstance().getToken(), "R2T1R9H47VRN");
                Toast.makeText(getActivity(), "연결에 성공했습니다.", Toast.LENGTH_SHORT).show();
                return true;
            }
        };

        private Preference.OnPreferenceChangeListener fakeChangeBarChangeListener = new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                mAppPrefs.setFakeStatusBarMode((boolean) newValue);
                getActivity().sendBroadcast(new Intent(getString(R.string.preferences_changed_broadcast)));
                return true;
            }
        };

        private Preference.OnPreferenceClickListener realCodeVIewListener = new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                String code = null;
                if((code = mAppPrefs.getKeyCode()) == null){
                    httpClient.doRegister(FirebaseInstanceId.getInstance().getToken());
                    code = mAppPrefs.getKeyCode();
                }
                if(code==null){
                    Toast.makeText(getActivity(), "서버 오류 발생", Toast.LENGTH_SHORT).show();
                }

                new AlertDialog.Builder(getActivity()).setMessage(code)
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                RefreshConnection();
                            }
                        })
                        .show();
                return true;
            }
        };

        private Preference.OnPreferenceClickListener realCodeRefreshListener = new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                String code = null;
                if((code = mAppPrefs.getKeyCode()) == null){
                    Toast.makeText(getActivity(), "등록되지 않은 기기 입니다. 등록해주세요", Toast.LENGTH_SHORT).show();
                    return true;
                }

                httpClient.doResetCode(FirebaseInstanceId.getInstance().getToken());
                code = mAppPrefs.getKeyCode();

                if(code==null){
                    Toast.makeText(getActivity(), "서버 오류 발생", Toast.LENGTH_SHORT).show();
                }

                new AlertDialog.Builder(getActivity()).setMessage(code)
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

        private void ChangePreferences(){
            if(isFake){
                prefer_screen.removePreference(realCodeViewPreference);
                prefer_screen.removePreference(realCodeRefreshPreference);
                prefer_screen.addPreference(fakeConnectionPreference);
                prefer_screen.addPreference(fakeRefreshPreference);
                prefer_screen.addPreference(fakeChangeBarPreference);
                httpClient = new FakeHttpClient(getActivity());

            }else{
                prefer_screen.removePreference(fakeConnectionPreference);
                prefer_screen.removePreference(fakeRefreshPreference);
                prefer_screen.removePreference(fakeChangeBarPreference);
                prefer_screen.addPreference(realCodeViewPreference);
                prefer_screen.addPreference(realCodeRefreshPreference);
                httpClient = new RealHttpClient(getActivity());
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
