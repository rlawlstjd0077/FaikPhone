package com.faikphone.client.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.faikphone.client.listener.PhoneStateRead;
import com.faikphone.client.network.HttpClient;
import com.faikphone.client.network.RealHttpClient;
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
        PhoneStateRead phoneListener = new PhoneStateRead(context);
        TelephonyManager telephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        telephony.listen(phoneListener,PhoneStateRead.LISTEN_SERVICE_STATE);
        telephony.listen(phoneListener,PhoneStateRead.LISTEN_CALL_STATE);
//
//        if (intent.getAction().equals("android.intent.action.PHONE_STATE")) {
//            boolean phoneMode = new AppPreferences(context).getPhoneMode();
//            if (!phoneMode) {
//                HttpClient httpClient = new RealHttpClient(context);
//                String phoneNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
//                try {
//                    JSONObject messageJSON = new JSONObject();
//                    messageJSON.put("event", "call");
//                    messageJSON.put("name", "");
//                    messageJSON.put("number", phoneNumber);
//                    String token = FirebaseInstanceId.getInstance().getToken();
//                    httpClient.doSendMessage(messageJSON, token);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
    }
}
