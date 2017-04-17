package com.faikphone.client;

import android.content.Intent;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by dsm_025 on 2017-04-13.
 */

public class FireBaseMessagingReciever extends FirebaseMessagingService{
    AppPreferences appPreferences = FaikPhoneApplication.getAppPreferences();
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        try {
            if(!appPreferences.getPhoneMode()) {
                handleMessage(remoteMessage.getData().get("message"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void handleMessage(String message) throws JSONException {
        JSONObject object = new JSONObject(message);
        switch (object.getString("type")){
            case "call" :
                Intent intent = new Intent(this, FakeStatusBarService.class);
                startActivity(intent);
                break;
        }
    }
}
