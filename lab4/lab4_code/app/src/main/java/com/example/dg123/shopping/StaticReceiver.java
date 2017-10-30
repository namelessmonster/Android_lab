package com.example.dg123.shopping;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class StaticReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent){
        Bundle bundle = intent.getExtras();
        Notification.Builder builder = new Notification.Builder(context);
        builder.setContentTitle("新商品热卖")
                .setContentText(bundle.getString("name") + "仅售" + bundle.getString("price") + "!")
                .setTicker("您有一条新消息")
                .setSmallIcon(bundle.getInt("picture"))
                .setAutoCancel(true);
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notify = builder.build();
        manager.notify(0, notify);
    }
}
