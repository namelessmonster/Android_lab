package com.example.dg123.shopping;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;

public class StaticReceiver extends BroadcastReceiver{
    Notification.Builder builder;
    Notification notify;
    @Override
    public void onReceive(Context context, Intent intent){
        if (intent.getAction().equals("STATICATION")){
            Bundle bundle = intent.getExtras();
            int pos = bundle.getInt("pos");
            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), MainActivity.pic.get(pos));
            Intent mainIntent = new Intent(context, InfoActivity.class);
            mainIntent.putExtra("pos", pos);
            PendingIntent mainPendingIntent = PendingIntent.getActivity(context, 0, mainIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            builder = new Notification.Builder(context);
            builder.setContentTitle("新商品热卖")
                    .setContentText(MainActivity.listItems.get(pos).get("name").toString() + "仅售"
                            + MainActivity.listItems.get(pos).get("price").toString() + "!")
                    .setTicker("您有一条新消息")
                    .setSmallIcon(MainActivity.pic.get(pos))
                    .setLargeIcon(bitmap)
                    .setAutoCancel(true)
                    .setContentIntent(mainPendingIntent);
            notify = builder.build();
            MainActivity.manager.notify(0, notify);
        }
    }
}
