package com.example.administrator.gtd;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.widget.RemoteViews;
import android.os.Bundle;
import android.content.ComponentName;

/**
 * Implementation of App Widget functionality.
 */
public class AppWidget extends AppWidgetProvider {
    static MyDB helper = null;
    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget);
        Cursor cursor = helper.query();
        if (cursor.moveToFirst()) {
            String title = cursor.getString(cursor.getColumnIndex("title"));
            String detail = cursor.getString(cursor.getColumnIndex("detail"));
            String date = cursor.getString(cursor.getColumnIndex("date"));
            String time = cursor.getString(cursor.getColumnIndex("time"));
            views.setTextViewText(R.id.widgettitle, title);
            views.setTextViewText(R.id.widgetdetail, detail);
            views.setTextViewText(R.id.widgetdate, date);
            views.setTextViewText(R.id.widgettime, time);
        }
        cursor.close();
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        views.setOnClickPendingIntent(R.id.widgetmain, pendingIntent);
        Intent intent1 = new Intent(context, AddActivity.class);
        PendingIntent pendingIntent1 = PendingIntent.getActivity(context, 0, intent1, 0);
        views.setOnClickPendingIntent(R.id.widgetadd, pendingIntent1);
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        if (helper == null) helper = new MyDB(context, "taskTable", null, 1);
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if (intent.getAction().equals("selfishlover.final.updateWidget")) {
            Bundle bundle = intent.getExtras();
            long id = bundle.getLong("id");
            if (helper == null) helper = new MyDB(context, "taskTable", null, 1);
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget);
            Cursor cursor = helper.query(id);
            if (cursor.moveToFirst()) {
                String title = cursor.getString(cursor.getColumnIndex("title"));
                String detail = cursor.getString(cursor.getColumnIndex("detail"));
                String date = cursor.getString(cursor.getColumnIndex("date"));
                String time = cursor.getString(cursor.getColumnIndex("time"));
                views.setTextViewText(R.id.widgettitle, title);
                views.setTextViewText(R.id.widgetdetail, detail);
                views.setTextViewText(R.id.widgetdate, date);
                views.setTextViewText(R.id.widgettime, time);
            }
            cursor.close();
            ComponentName name = new ComponentName(context, AppWidget.class);
            AppWidgetManager.getInstance(context).updateAppWidget(name, views);
        }
    }
}
