package com.faikphone.client;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

/**
 * Created by boxfox on 2017-04-20.
 */

public class NotificationBuilder {
    public static final int TYPE_SMS = 1;
    public static final int TYPE_MISSED_CALL = 2;

    public static void missedCall(Context context, String name, String phone){
        build(context, R.drawable.missed_call, "부재중 전화", name+" "+phone );
    }

    public static void sms(Context context, String name, String content){
        build(context, R.drawable.sms, name, content );
    }

    public static void build(Context context, int icon, String title, String content) {
        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, new Intent(context, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Builder mBuilder = new Notification.Builder(context);
        mBuilder.setSmallIcon(icon);
        mBuilder.setTicker("TestAAA");
        mBuilder.setWhen(System.currentTimeMillis());
        mBuilder.setNumber(10);
        mBuilder.setContentTitle(title);;
        mBuilder.setContentText(content);
        mBuilder.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);
        mBuilder.setContentIntent(pendingIntent);
        mBuilder.setAutoCancel(true);

        nm.notify(111, mBuilder.build());
    }
}