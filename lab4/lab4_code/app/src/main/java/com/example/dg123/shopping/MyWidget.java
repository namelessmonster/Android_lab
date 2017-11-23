package com.example.dg123.shopping;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.TextView;

/**
 * Implementation of App Widget functionality.
 */
public class MyWidget extends AppWidgetProvider {
    private RemoteViews views = null;
    private ComponentName me = null;
    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        CharSequence widgetText = context.getString(R.string.appwidget_text);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.my_widget);
        views.setTextViewText(R.id.appwidget_text, widgetText);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        if (views == null) views = new RemoteViews(context.getPackageName(), R.layout.my_widget);
        //views是一个RemoteViews，负责在桌面中显示widget
        Intent i = new Intent(context, MainActivity.class);
        //i是一个intent，表示从当前界面跳转至应用主界面
        PendingIntent pi = PendingIntent.getActivity(context, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
        //pi是一个PendingIntent，包装i表示i这个intent需要满足某些条件才会启动
        views.setOnClickPendingIntent(R.id.appwidget_image, pi);
        //为widget的图片设置点击事件，点击后会启动pi包装的intent
        views.setOnClickPendingIntent(R.id.appwidget_text, pi);
        //为widget的文字设置点击事件，点击后会启动pi包装的intent
        if (me == null) me = new ComponentName(context, MyWidget.class);
        appWidgetManager.updateAppWidget(me, views);
        //使用appWidgetManager的updateAppWidget方法使views生效
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onReceive(Context context, Intent intent){
        super.onReceive(context, intent);
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        Bundle bundle = intent.getExtras();
        if (intent.getAction().equals("STATICATION")){
            int pos = bundle.getInt("pos");
            String name = MainActivity.listItems.get(pos).get("name").toString();
            String price = MainActivity.listItems.get(pos).get("price").toString();
            if (views == null) views = new RemoteViews(context.getPackageName(), R.layout.my_widget);
            views.setTextViewText(R.id.appwidget_text, name + "仅售" + price);
            views.setImageViewResource(R.id.appwidget_image, MainActivity.pic.get(pos));

            Intent i = new Intent(context, InfoActivity.class);
            i.putExtra("pos", pos);
            PendingIntent pi = PendingIntent.getActivity(context, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);

            views.setOnClickPendingIntent(R.id.appwidget_image, pi);
            views.setOnClickPendingIntent(R.id.appwidget_text, pi);

            if (me == null) me = new ComponentName(context, MyWidget.class);
            appWidgetManager.updateAppWidget(me, views);
        }
    }
}

