package com.vedmedenko.exchangerates.core.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.vedmedenko.exchangerates.ExchangeRatesApplication;
import com.vedmedenko.exchangerates.core.DataManager;
import com.vedmedenko.exchangerates.core.rest.models.current.CurrentRate;
import com.vedmedenko.exchangerates.injection.ActivityContext;
import com.vedmedenko.exchangerates.utils.AlarmUtils;
import com.vedmedenko.exchangerates.utils.ConstantsManager;
import com.vedmedenko.exchangerates.utils.NotificationUtils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import javax.inject.Inject;

import rx.Subscription;
import rx.schedulers.Schedulers;
import timber.log.Timber;

public class SyncService extends Service {

    @Inject
    DataManager dataManager;

    private Subscription subscription;

    public static Intent getStartIntent(@NonNull @ActivityContext Context context, boolean returnData, boolean alarm) {
        Intent intent = new Intent(context, SyncService.class);
        Bundle extras = new Bundle();
        extras.putBoolean(ConstantsManager.EXTRA_BOOLEAN, returnData);
        extras.putBoolean(ConstantsManager.EXTRA_BOOLEAN_ALARM, alarm);
        intent.putExtras(extras);
        return intent;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ExchangeRatesApplication.get(new WeakReference<>(getBaseContext())).getComponent().inject(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (subscription != null)
            subscription.unsubscribe();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Bundle extras = intent.getExtras();
        final boolean returnData = extras.getBoolean(ConstantsManager.EXTRA_BOOLEAN);
        final boolean alarm = extras.getBoolean(ConstantsManager.EXTRA_BOOLEAN_ALARM);

        if (alarm)
            Timber.d("Service fired from alarm!");

        if (!dataManager.currentCurrencyLoaded() && !AlarmUtils.isRepeatingAlarmSet())
            AlarmUtils.setupRepeatingAlarm(this);

        Intent broadcast = new Intent(ConstantsManager.BROADCAST);
        broadcast.putExtra(ConstantsManager.EXTRA_TYPE, "Current");

        subscription = dataManager.loadCurrentRates()
                .subscribeOn(Schedulers.io())
                .doAfterTerminate(() -> {
                })
                .subscribe(currentRates -> {
                    ArrayList<CurrentRate> rates = currentRates.getRates();

                    for (CurrentRate rate : rates) {
                        int zeros = rate.getUnit().toString().split("\\.")[0].replace("1", "").length();
                        dataManager.saveCurrentCurrency(rate.getCcy(),
                                rate.getBuy().toString().substring(0, zeros) + "." +
                                rate.getBuy().toString().substring(zeros + 1));
                    }

                    if (returnData) {
                        sendBroadcast(broadcast);
                    }

                    NotificationUtils.notifyCurrentCurrencyLoaded(this);
                    AlarmUtils.cancelRepeatingAlarm(this);
                }, throwable -> {
                    Timber.e(throwable, "Error while loading data occurred!");
                    broadcast.putExtra(ConstantsManager.EXTRA_BOOLEAN, true);
                    sendBroadcast(broadcast);
                });

        return START_STICKY;
    }
}
