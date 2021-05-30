package com.example.remedy.Notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.remedy.Notification.NotificationHelper;

import androidx.core.app.NotificationCompat;

public class AlertReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        //String title = intent.getStringExtra("Title");
        //String message = intent.getStringExtra("Message");

        Bundle bundle = intent.getBundleExtra("bundle");

        String title = (String)bundle.getString("Title");
        String message = (String)bundle.getString("Message");


        NotificationHelper notificationHelper = new NotificationHelper(context);
        NotificationCompat.Builder nb = notificationHelper.getChannelNotification(title,message);
        notificationHelper.getManager().notify(1,nb.build());
    }
}
