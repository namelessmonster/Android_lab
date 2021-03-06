package com.example.dg123.shopping;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.RemoteViews;

import org.greenrobot.eventbus.EventBus;

public class DynamicReceiver extends BroadcastReceiver{
    Notification.Builder builder;
    Notification notify;
    private RemoteViews views = null;
    private ComponentName me = null;
    private AppWidgetManager appWidgetManager = null;
    @Override
    public void onReceive(Context context, Intent intent){
        if (intent.getAction().equals("DYNAMICATION")){
            Bundle bundle = intent.getExtras();
            int pos = bundle.getInt("pos");
            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),
                    MainActivity.pic.get(pos));
            Intent mainIntent = new Intent(context, MainActivity.class);
            PendingIntent mainPendingIntent = PendingIntent.getActivity(context, 0,
                    mainIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            builder = new Notification.Builder(context);
            builder.setContentTitle("马上下单")
                    .setContentText(MainActivity.listItems.get(pos).get("name").toString() + "已添加到购物车")
                    .setTicker("您有一条新消息")
                    .setSmallIcon(MainActivity.pic.get(pos))
                    .setLargeIcon(bitmap)
                    .setAutoCancel(true)
                    .setContentIntent(mainPendingIntent);
            notify = builder.build();
            MainActivity.manager.notify(0, notify);
            if (views == null) views = new RemoteViews(context.getPackageName(), R.layout.my_widget);
            Intent i = new Intent(context, MainActivity.class);
            PendingIntent pi = PendingIntent.getActivity(context, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
            views.setOnClickPendingIntent(R.id.appwidget_image, pi);
            views.setOnClickPendingIntent(R.id.appwidget_text, pi);
            views.setTextViewText(R.id.appwidget_text, MainActivity.listItems.get(pos).get("name").toString() + "已添加到购物车");
            if (me == null) me = new ComponentName(context, MyWidget.class);
            if (appWidgetManager == null) appWidgetManager = AppWidgetManager.getInstance(context);
            appWidgetManager.updateAppWidget(me, views);
        }
    }

}
