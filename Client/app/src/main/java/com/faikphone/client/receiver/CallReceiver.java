package com.faikphone.client.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.faikphone.client.utils.AppPreferences;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by BeINone on 2017-05-11.
 */

public class CallReceiver extends BroadcastReceiver {
    //    call
//    {
//        "type" : "call",
//            "number" : "010-2222-2222",
//            "name" : "" (NullAble)
//    }
    @Override
    public void onReceive(Context context, Intent intent) {
        boolean phoneMode = new AppPreferences(context).getPhoneMode();
        if (!phoneMode) {
            if (intent.getAction().equals("android.intent.action.PHONE_STATE")) {
                HttpClient httpClient = new HttpClient(phoneMode);
                String phoneNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
                Log.d("testLog", phoneNumber);
                try {
                    JSONObject messageJSON = new JSONObject();
                    messageJSON.put("type", "call");
                    messageJSON.put("number", phoneNumber);
                    String token = FirebaseInstanceId.getInstance().getToken();
                    httpClient.doSendMessage(messageJSON.toString(), token);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
