package com.codingfeel.sm.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.codingfeel.sm.MyApplication;
import com.codingfeel.sm.R;

/**
 * Created by hwb on 16/2/17.
 */
public class NotificationUtil {


    public static void messageNotify(String title, String message, Intent intent) {
        showSimpleNotify(MyApplication.getInstance(), title, message, intent);
    }

    /**
     * 通知
     *
     * @param context
     * @param title
     * @param message
     * @param intent
     */
    public static void showSimpleNotify(Context context, String title, String message, Intent intent) {
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationCompat.Builder compat = new NotificationCompat.Builder(context);
        compat.setTicker(message)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_SOUND)
                .setSmallIcon(R.mipmap.logo)
                .setContentIntent(PendingIntent.getActivity(context, 0, intent, 0))
                .setPriority(Notification.PRIORITY_DEFAULT);

        manager.notify(100, compat.build());
    }
}
