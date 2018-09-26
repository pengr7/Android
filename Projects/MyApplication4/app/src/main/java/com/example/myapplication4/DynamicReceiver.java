package com.example.myapplication4;

import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;

public class DynamicReceiver extends BroadcastReceiver {
    private static final String DYNAMICACTION = "com.example.myapplication4.DynamicReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(DYNAMICACTION)) {
            RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);
            Bundle bundle = intent.getExtras();
            rv.setTextViewText(R.id.appwidget_text, bundle.getString("message"));
            rv.setImageViewResource(R.id.appwidget_image, R.drawable.dynamic);
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            ComponentName componentName = new ComponentName(context, NewAppWidget.class);
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(componentName);
            appWidgetManager.updateAppWidget(appWidgetIds, rv);
        }
    }
}
