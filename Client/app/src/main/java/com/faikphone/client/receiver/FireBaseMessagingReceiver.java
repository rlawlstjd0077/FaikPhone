package com.faikphone.client.receiver;

import android.content.Intent;

import com.faikphone.client.utils.AppPreferences;
import com.faikphone.client.activity.CallActivity;
import com.faikphone.client.application.FaikPhoneApplication;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by dsm_025 on 2017-04-13.
 */

public class FireBaseMessagingReceiver extends FirebaseMessagingService{
    AppPreferences appPreferences = FaikPhoneApplication.getAppPreferences();
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Intent intent = new Intent(this, CallActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
//        Log.d("onMessageReceived: ", remoteMessage.getData().get("message"));
//        try {
//            if(!appPreferences.getPhoneMode()) {        // Fake Phone 일 경우
//                handleMessage(remoteMessage.getData().get("message"));
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
    }

    public void handleMessage(String message) throws JSONException {
        JSONObject object = new JSONObject(message);
        switch (object.getString("type")){
            case "call" :
                Intent intent = new Intent(this, CallActivity.class);
                startActivity(intent);
                break;
        }
    }
}
