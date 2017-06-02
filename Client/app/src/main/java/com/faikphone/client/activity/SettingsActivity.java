package com.faikphone.client.activity;

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

import com.faikphone.client.utils.AppPreferences;
import com.faikphone.client.R;
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

        Preference fakeDisconnectPreference;

        SwitchPreference fakeChangeBarPreference;

        Preference realCodeViewPreference;

        Preference realCodeRefreshPreference;

        Preference fakeCheckConnectionPreference;

        Preference realDisconnectPreference;

        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            mAppPrefs = new AppPreferences(getActivity());
            httpClient = new RealHttpClient(getActivity());
            addPreferencesFromResource(R.xml.pref_general);
            setHasOptionsMenu(true);

            isFake = mAppPrefs.getPhoneMode();
            switchModeReference = (SwitchPreference) findPreference("fake_switch");
            switchModeReference.setOnPreferenceChangeListener(fakeSwitchChangeListener);
            switchModeReference.setChecked(mAppPrefs.getPhoneMode());

            prefer_screen = (PreferenceScreen) findPreference("prefer_screen");

            // Fake Preferences
            fakeConnectionPreference = (EditTextPreference)findPreference("fake_connection");
            fakeConnectionPreference.setOnPreferenceChangeListener(fakeConnectionChangeListener);
            fakeConnectionPreference.setOnPreferenceClickListener(fakeConnectionClickListener);

            fakeDisconnectPreference = findPreference("fake_disconnect");
            fakeDisconnectPreference.setOnPreferenceClickListener(fakeRefreshClickListener);

            fakeChangeBarPreference = (SwitchPreference) findPreference("fake_statusbar");
            fakeChangeBarPreference.setOnPreferenceChangeListener(fakeChangeBarChangeListener);
            fakeChangeBarPreference.setChecked(mAppPrefs.isFakeStatusBarMode());

            fakeCheckConnectionPreference = findPreference("fake_check_connection");
            fakeCheckConnectionPreference.setOnPreferenceClickListener(fakeClickedCheckConnectionListener);

                    //Real Preferences



            realCodeViewPreference = findPreference("real_code_view");
            realCodeViewPreference.setOnPreferenceClickListener(realCodeVIewListener);

            realCodeRefreshPreference = findPreference("real_code_refresh");
            realCodeRefreshPreference.setOnPreferenceClickListener(realCodeRefreshListener);

            realDisconnectPreference = findPreference("real_disconnect");
            realDisconnectPreference.setOnPreferenceClickListener(realDisconnectListener);

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
            public boolean onPreferenceChange(Preference preference, final Object newValue) {
//
                new AlertDialog.Builder(getActivity()).setMessage("Mode를 변경하시면 연결된 데이터는 모두 초기화 됩니다. 변경하시겠습니까?")
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO: 공기계 -> 본 핸드폰, 또는 그 반대 모드로 넘어갈 때 이전의 기기와의 연결을 끊기
//                                if((Boolean) newValue){
//                                    httpClient.doResetAll(FirebaseInstanceId.getInstance().getToken());
//                                }else{
//                                    httpClient.doResetConnection(FirebaseInstanceId.getInstance().getToken());
//                                }
                                isFake = (boolean) newValue;
                                mAppPrefs.setPhoneMode(isFake);
                                ChangePreferences();
                                getActivity().sendBroadcast(new Intent(getString(R.string.preferences_changed_broadcast)));
                                switchModeReference.setChecked((Boolean) newValue);
                            }
                        })
                        .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switchModeReference.setChecked(!(Boolean) newValue);
                            }
                        })
                        .show();
//                fakeChangeBarPreference.setChecked(!(Boolean) newValue);
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
                                RefreshConnection(FirebaseInstanceId.getInstance().getToken());
                            }
                        })
                        .setNegativeButton("취소", null)
                        .show();
                return true;
            }
        };
        private EditTextPreference.OnPreferenceClickListener fakeConnectionClickListener = new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                fakeConnectionPreference.setDialogTitle(R.string.pref_connection_dialog_fake);
                if(mAppPrefs.getKeyRealPhoneNum() != null) {
                        new AlertDialog.Builder(getActivity()).setMessage("이미 연결 되어 있는 디바이스 입니다. 다른 기기에 연결하시겠습니까? ")
                                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //TODO 연결 끊어야 함
                                        fakeConnectionPreference.getDialog().show();
                                    }
                                })
                                .setNegativeButton("취소", null)
                                .show();
                }
                fakeConnectionPreference.getDialog().hide();
                return false;
            }
        };

        private EditTextPreference.OnPreferenceChangeListener fakeConnectionChangeListener = new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                //TODO 테스트 시에만 고정 토큰 넣어줌
                httpClient.doFakeRegister(FirebaseInstanceId.getInstance().getToken(), "1QEE3R8WBM6F");
                if(mAppPrefs.getKeyRealPhoneNum() != null) {
                    Toast.makeText(getActivity(), "연결에 성공했습니다.", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getActivity(), "연결에 실패했습니다.", Toast.LENGTH_SHORT).show();
                }
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

        private Preference.OnPreferenceClickListener fakeClickedCheckConnectionListener = new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                //TODO Server에 상태 체크 오청 보내기
                HttpClient httpClient = new FakeHttpClient(getActivity());
                httpClient.doCheckConnection(FirebaseInstanceId.getInstance().getToken());
                String phoneNum;

                if((phoneNum= mAppPrefs.getKeyRealPhoneNum()) == null){
                    Toast.makeText(getActivity(), "연결되어 있지 않습니다.", Toast.LENGTH_SHORT).show();
                    return false;
                }

                new AlertDialog.Builder(getActivity()).setMessage("연결된 기기 : " + phoneNum)
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .show();

                return true;
            }
        };

        private Preference.OnPreferenceClickListener realCodeVIewListener = new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                HttpClient httpClient = new RealHttpClient(getActivity());
                String code;
                if((code = mAppPrefs.getKeyCode()) == null){
                    httpClient.doRealRegister(FirebaseInstanceId.getInstance().getToken(), mAppPrefs.getKeyDevicePhoneNumber());
                    code = mAppPrefs.getKeyCode();
                }
                if(code==null){
                    Toast.makeText(getActivity(), "서버 오류 발생", Toast.LENGTH_SHORT).show();
                }

                new AlertDialog.Builder(getActivity()).setMessage(code)
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .show();
                return true;
            }
        };

        private Preference.OnPreferenceClickListener realCodeRefreshListener = new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                String code;
                if((mAppPrefs.getKeyCode()) == null){
                    Toast.makeText(getActivity(), "등록되지 않은 기기 입니다. 인증 코드를 발급해주세요.", Toast.LENGTH_SHORT).show();
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
                            }
                        })
                        .show();
                return true;
            }
        };

        private Preference.OnPreferenceClickListener realDisconnectListener = new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                String message = getResources().getString(R.string.refresh_alert_fake);
                new AlertDialog.Builder(getActivity()).setMessage(message)
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                RefreshConnection(FirebaseInstanceId.getInstance().getToken());
                            }
                        })
                        .setNegativeButton("취소", null)
                        .show();
                return true;
            }
        };



        private void ChangePreferences(){
            if(isFake){
                prefer_screen.removePreference(realDisconnectPreference);
                prefer_screen.removePreference(realCodeViewPreference);
                prefer_screen.removePreference(realCodeRefreshPreference);
                prefer_screen.addPreference(fakeConnectionPreference);
                prefer_screen.addPreference(fakeDisconnectPreference);
                prefer_screen.addPreference(fakeChangeBarPreference);
                prefer_screen.addPreference(fakeCheckConnectionPreference);
                httpClient = new FakeHttpClient(getActivity());
            }else{
                prefer_screen.removePreference(fakeCheckConnectionPreference);
                prefer_screen.removePreference(fakeConnectionPreference);
                prefer_screen.removePreference(fakeDisconnectPreference);
                prefer_screen.removePreference(fakeChangeBarPreference);
                prefer_screen.addPreference(realCodeViewPreference);
                prefer_screen.addPreference(realCodeRefreshPreference);
                prefer_screen.addPreference(realDisconnectPreference);
                httpClient = new RealHttpClient(getActivity());
            }
        }

        private void RefreshConnection(String token){
                httpClient.doResetConnection(token);
        }
    }
}
