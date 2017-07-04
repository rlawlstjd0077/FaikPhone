package com.faikphone.client.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import com.faikphone.client.network.RealHttpClient;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by dsm_025 on 2017-07-04.
 */

public class SMSReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
            Bundle bundle = intent.getExtras();
            Object messages[] = (Object[]) bundle.get("pdus");
            SmsMessage smsMessage[] = new SmsMessage[messages.length];

            for (int i = 0; i < messages.length; i++) {
                // PDU 포맷으로 되어 있는 메시지를 복원합니다.
                smsMessage[i] = SmsMessage.createFromPdu((byte[]) messages[i]);
            }

//// SMS 수신 시간 확인
//            Date curDate = new Date(smsMessage[0].getTimestampMillis());
//            Log.d("문자 수신 시간", curDate.toString());

// SMS 발신 번호 확인
            String incomingNumber = smsMessage[0].getOriginatingAddress();

// SMS 메시지 확인
            String message = smsMessage[0].getMessageBody().toString();
            Log.d("문자 내용", "발신자 : " + incomingNumber + ", 내용 : " + message);


            RealHttpClient httpClient= new RealHttpClient(context);
            try {
                JSONObject messageJSON = new JSONObject();
                messageJSON.put("event", "sms");
                messageJSON.put("name", "");
                messageJSON.put("number", incomingNumber);
                messageJSON.put("content", message);
                String token = FirebaseInstanceId.getInstance().getToken();
                httpClient.doSendMessage(messageJSON, token);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
