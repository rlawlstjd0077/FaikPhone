package com.faikphone.client.service;

import android.content.Intent;

import com.faikphone.client.NotificationBuilder;
import com.faikphone.client.activity.CallActivity;
import com.faikphone.client.application.FaikPhoneApplication;
import com.faikphone.client.utils.AppPreferences;
import com.faikphone.client.utils.VibrateManager;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by dsm_025 on 2017-04-13.
 */

public class FireBaseMessagingReceiver extends FirebaseMessagingService {
    AppPreferences appPreferences = FaikPhoneApplication.getAppPreferences();
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        try {
            JSONObject jsonObject = new JSONObject(remoteMessage.getData().get("json"));
            if (appPreferences.getPhoneMode()) {        // Fake Phone 일 경우
                switch (jsonObject.getString("event")) {
                    case "call":
                        Intent intent = new Intent(this, CallActivity.class);
                        intent.putExtra("name", jsonObject.getString("name"));
                        intent.putExtra("number", jsonObject.getString("number"));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        break;
                    case "call_miss":
                        Intent startMain = new Intent(Intent.ACTION_MAIN);
                        startMain.addCategory(Intent.CATEGORY_HOME);
                        startMain.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        getApplicationContext().startActivity(startMain);
                        NotificationBuilder.missedCall(getApplicationContext(), jsonObject.getString("name") != null
                                ? jsonObject.getString("name") : null,jsonObject.getString("number"));
                        break;
                    case "sms":
                        break;
                }
            }else{
                switch (jsonObject.getString("event")) {
                    case "call_accept":
                        //TODO 진짜 폰에서 전화 수신 동작
                        break;
                    case "call_refuse":
                        //TODO 진짜 폰에서 전화 거부 동작
                        break;
                    case "sms":
                        break;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
