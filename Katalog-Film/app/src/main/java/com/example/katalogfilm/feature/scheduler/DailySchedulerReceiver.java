package com.example.katalogfilm.feature.scheduler;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.example.katalogfilm.R;

public class DailySchedulerReceiver extends BroadcastReceiver {
    public static final int NOTIF_ID_DAILY = 1;
    public static CharSequence CHANNEL_NAME = "ChannelMovie";
    public static String CHANNEL_ID = "Movie";

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManager notificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notifications_black_24dp)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_notifications_black_24dp))
                .setContentTitle(context.getString(R.string.content_title))
                .setContentText(context.getString(R.string.message_reminder))
                .setAutoCancel(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            builder.setChannelId(CHANNEL_ID);

            if (notificationManager != null) {

                notificationManager.createNotificationChannel(notificationChannel);
            }
        }
        Notification notification = builder.build();
        if (notificationManager != null) {

            notificationManager.notify(NOTIF_ID_DAILY, notification);
        }
    }
}
