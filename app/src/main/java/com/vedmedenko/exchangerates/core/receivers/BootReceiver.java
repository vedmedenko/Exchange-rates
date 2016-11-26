package com.vedmedenko.exchangerates.core.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.vedmedenko.exchangerates.utils.AlarmUtils;

public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        AlarmUtils.setupEverydayAlarm(context);
    }
}
