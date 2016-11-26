package com.vedmedenko.exchangerates.utils;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;

import com.vedmedenko.exchangerates.R;
import com.vedmedenko.exchangerates.ui.activities.MainActivity;

public final class NotificationUtils {

    public static final int NOTIFICATION_ID = 1;

    public static void notifyCurrentCurrencyLoaded(@NonNull Context context) {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_attach_money_white_48dp)
                        .setContentTitle("Currencies!")
                        .setContentText("Current currencies loaded!");

        Intent resultIntent = new Intent(context, MainActivity.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(resultIntent);

        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);

        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }

    private NotificationUtils() {
        throw new AssertionError("No instances.");
    }
}
