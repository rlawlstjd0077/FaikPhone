package com.faikphone.client;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by BeINone on 2017-04-14.
 */

public class BootUpReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
//        context.startActivity(new Intent(context, MainActivity.class));
        context.startService(new Intent(context, FakeStatusBarService.class));
    }
}
