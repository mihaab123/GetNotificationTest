package com.mihailovalex.getnotification;

import android.app.Notification;

import android.content.Intent;

import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;



public class NLService extends NotificationListenerService {

    private String TAG = "MyLogs";

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        Notification mNotification=sbn.getNotification();
        Bundle extras = mNotification.extras;
        Log.i(TAG, "**********  onNotificationPosted");
        Log.i(TAG, "ID :" + sbn.getId() + "\t" + extras.getString("android.text")+ "\t" + sbn.getPackageName());
        Intent i = new Intent("com.mihailovalex.getnotification.NOTIFICATION_LISTENER_EXAMPLE");
        i.putExtra("title",extras.getString("android.title")+":"+extras.getString("android.text"));
        i.putExtra("date",sbn.getPostTime());
        i.putExtra("packageName",sbn.getPackageName());

        sendBroadcast(i);

    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {

    }

}
