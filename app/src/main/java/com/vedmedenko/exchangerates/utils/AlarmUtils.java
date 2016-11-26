package com.vedmedenko.exchangerates.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

import com.vedmedenko.exchangerates.core.services.SyncService;

import java.util.Calendar;

import timber.log.Timber;

public final class AlarmUtils {

    private static PendingIntent pendingIntent, pendingIntentRepeating;

    public static int getHour(String time) {
        String[] pieces = time.split(":");
        return(Integer.parseInt(pieces[0]));
    }

    public static int getMinute(String time) {
        String[] pieces = time.split(":");
        return(Integer.parseInt(pieces[1]));
    }

    public static void setupRepeatingAlarm(@NonNull Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent serviceIntent = SyncService.getStartIntent(context, false, true);
        pendingIntentRepeating = PendingIntent.getService(context, 0, serviceIntent, 0);

        alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), (ConstantsManager.REPEATING_ALARM_REPEAT_TIME_SECONDS * 1000), pendingIntentRepeating);

        Timber.d("Repeating alarm setup!");
    }

    public static void cancelRepeatingAlarm(@NonNull Context context) {
        if (pendingIntentRepeating != null) {
            Timber.d("Repeating alarm canceled");
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            alarmManager.cancel(pendingIntentRepeating);
            pendingIntentRepeating = null;
        }
    }

    public static void setupEverydayAlarm(@NonNull Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent serviceIntent = SyncService.getStartIntent(context, false, true);
        pendingIntent = PendingIntent.getService(context, 0, serviceIntent, 0);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String syncTime = preferences.getString("sync_time", "09:00");

        int hour = AlarmUtils.getHour(syncTime);
        int minute = AlarmUtils.getMinute(syncTime);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);

        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, pendingIntent);

        Timber.d("Alarm setup for time: " + syncTime);
    }

    public static void cancelEverydayAlarm(@NonNull Context context) {
        if (pendingIntent != null) {
            Timber.d("Alarm canceled");
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            alarmManager.cancel(pendingIntent);
            pendingIntent = null;
        }
    }

    public static boolean isRepeatingAlarmSet() {
        return pendingIntentRepeating != null;
    }

    private AlarmUtils() {
        throw new AssertionError("No instances.");
    }
}
