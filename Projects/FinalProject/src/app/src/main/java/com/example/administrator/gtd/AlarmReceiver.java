package com.example.administrator.gtd;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by nxl on 2016/12/13.
 */

public class AlarmReceiver extends BroadcastReceiver {

    /* (non-Javadoc)
     * @see android.content.BroadcastReceiver#onReceive(android.content.Context, android.content.Intent)
     */
    @Override
    public void onReceive(Context arg0, Intent data) {
        Bitmap bm = BitmapFactory.decodeResource(arg0.getResources(),R.mipmap.achieve);
        NotificationManager manager = (NotificationManager)arg0.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification.Builder builder = new Notification.Builder(arg0);
        builder.setContentTitle("GTD")
                .setSmallIcon(R.mipmap.achieve)
                .setLargeIcon(bm)
                .setAutoCancel(true)
                .setContentText("It is time!");
        Intent intent1 = new Intent(arg0,MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(arg0,0,intent1,0);
        builder.setContentIntent(pendingIntent);
        Notification notification = builder.build();
        manager.notify(0,notification);
    }
}