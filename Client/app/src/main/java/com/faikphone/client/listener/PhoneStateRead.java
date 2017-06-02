package com.faikphone.client.listener;

import android.content.Context;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.faikphone.client.network.HttpClient;
import com.faikphone.client.network.RealHttpClient;
import com.faikphone.client.utils.AppPreferences;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

public class PhoneStateRead extends PhoneStateListener {
    private Context context;
    private HttpClient httpClient;

    String TAG = "PHONE STATE READ";

    public PhoneStateRead(Context context){
        this.context = context;
    }

    @Override
    public void onCallStateChanged(int state, String incomingNumber) {
        switch (state) {
            case TelephonyManager.CALL_STATE_IDLE:
                Log.i(TAG, "MyPhoneStateListener->onCallStateChanged() -> CALL_STATE_IDLE " + incomingNumber);
                httpClient = new RealHttpClient(context);
                try {
                    JSONObject messageJSON = new JSONObject();
                    messageJSON.put("event", "call_miss");
                    messageJSON.put("name", "");
                    messageJSON.put("number", incomingNumber);
                    messageJSON.put("time", "11:00");
                    String token = FirebaseInstanceId.getInstance().getToken();
                    httpClient.doSendMessage(messageJSON, token);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case TelephonyManager.CALL_STATE_OFFHOOK:
                Log.i(TAG, "MyPhoneStateListener->onCallStateChanged() -> CALL_STATE_OFFHOOK " + incomingNumber);
                break;
            case TelephonyManager.CALL_STATE_RINGING:
                Log.i(TAG, "MyPhoneStateListener->onCallStateChanged() -> CALL_STATE_RINGING " + incomingNumber);
                boolean phoneMode = new AppPreferences(context).getPhoneMode();
            if (!phoneMode) {
                httpClient= new RealHttpClient(context);
                try {
                    JSONObject messageJSON = new JSONObject();
                    messageJSON.put("event", "call");
                    messageJSON.put("name", "");
                    messageJSON.put("number", incomingNumber);
                    String token = FirebaseInstanceId.getInstance().getToken();
                    httpClient.doSendMessage(messageJSON, token);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
                break;
            default:
                Log.i(TAG, "MyPhoneStateListener->onCallStateChanged() -> default -> " + Integer.toString(state));
                break;
        }
    }
}