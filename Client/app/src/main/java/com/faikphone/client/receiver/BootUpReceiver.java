package com.faikphone.client.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.faikphone.client.service.FakeStatusBarService;
import com.faikphone.client.service.FireBaseMessagingReceiver;

/**
 * Created by BeINone on 2017-04-14.
 */

public class BootUpReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
//        context.startActivity(new Intent(context, MainActivity.class));
        context.startService(new Intent(context, FakeStatusBarService.class));
        context.startService(new Intent(context, FireBaseMessagingReceiver.class));
    }
}
