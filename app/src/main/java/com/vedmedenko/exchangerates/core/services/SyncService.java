package com.vedmedenko.exchangerates.core.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

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

public class SyncService extends IntentService {

    @Inject
    DataManager dataManager;

    private static int count = 0;

    private Subscription subscription;

    public static Intent getStartIntent(@NonNull @ActivityContext Context context, boolean returnData, boolean alarm) {
        Intent intent = new Intent(context, SyncService.class);
        Bundle extras = new Bundle();
        extras.putBoolean(ConstantsManager.EXTRA_BOOLEAN, returnData);
        extras.putBoolean(ConstantsManager.EXTRA_BOOLEAN_ALARM, alarm);
        intent.putExtras(extras);
        return intent;
    }

    public SyncService() {
        super("Sync service thread");
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

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        final boolean returnData = extras.getBoolean(ConstantsManager.EXTRA_BOOLEAN);
        final boolean alarm = extras.getBoolean(ConstantsManager.EXTRA_BOOLEAN_ALARM);

        Timber.d("Service fired: " + (++count));

        if (alarm)
            Timber.d("Service fired from alarm!");

        if (!dataManager.currentCurrencyLoaded() && !AlarmUtils.isRepeatingAlarmSet())
            AlarmUtils.setupRepeatingAlarm(this);

        if (subscription != null)
            subscription.unsubscribe();

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
                        Intent broadcast = new Intent(ConstantsManager.BROADCAST);
                        broadcast.putExtra(ConstantsManager.EXTRA_TYPE, "Current");
                        sendBroadcast(broadcast);
                    }

                    NotificationUtils.notifyCurrentCurrencyLoaded(this);
                    AlarmUtils.cancelRepeatingAlarm(this);
                }, throwable -> {
                    Timber.e(throwable, "Error while loading data occurred!");
                });
    }
}
