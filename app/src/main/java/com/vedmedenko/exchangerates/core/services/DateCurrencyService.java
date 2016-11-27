package com.vedmedenko.exchangerates.core.services;

import android.app.IntentService;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.vedmedenko.exchangerates.ExchangeRatesApplication;
import com.vedmedenko.exchangerates.core.DataManager;
import com.vedmedenko.exchangerates.core.rest.models.date.DateRate;
import com.vedmedenko.exchangerates.injection.ActivityContext;
import com.vedmedenko.exchangerates.utils.ConstantsManager;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import javax.inject.Inject;

import rx.Subscription;
import rx.schedulers.Schedulers;
import timber.log.Timber;

public class DateCurrencyService extends Service {

    @Inject
    DataManager dataManager;

    private Subscription subscription;

    public static Intent getStartIntent(@NonNull @ActivityContext Context context, @NonNull String date) {
        Intent intent = new Intent(context, DateCurrencyService.class);
        Bundle extras = new Bundle();
        extras.putString(ConstantsManager.EXTRA_STRING_DATE, date);
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
        final String date = extras.getString(ConstantsManager.EXTRA_STRING_DATE);

        if (date == null)
            return START_NOT_STICKY;

        subscription = dataManager.loadDateRates(date)
                .subscribeOn(Schedulers.io())
                .doAfterTerminate(() -> {
                })
                .subscribe(dateRates -> {
                    ArrayList<DateRate> rates = dateRates.getRates();

                    ArrayList<String> eur = new ArrayList<>();
                    ArrayList<String> rur = new ArrayList<>();
                    ArrayList<String> usd = new ArrayList<>();

                    for (DateRate rate : rates) {
                        if (rate.getCurrency().equals("EUR")) {
                            eur.add(rate.getSaleRateNB().toString());
                            eur.add(rate.getPurchaseRateNB().toString());
                            eur.add(rate.getSaleRate().toString());
                            eur.add(rate.getPurchaseRate().toString());
                        } else if (rate.getCurrency().equals("RUB")) {
                            rur.add(rate.getSaleRateNB().toString());
                            rur.add(rate.getPurchaseRateNB().toString());
                            rur.add(rate.getSaleRate().toString());
                            rur.add(rate.getPurchaseRate().toString());
                        } else if (rate.getCurrency().equals("USD")) {
                            usd.add(rate.getSaleRateNB().toString());
                            usd.add(rate.getPurchaseRateNB().toString());
                            usd.add(rate.getSaleRate().toString());
                            usd.add(rate.getPurchaseRate().toString());
                        }
                    }

                    Intent broadcast = new Intent(ConstantsManager.BROADCAST);
                    broadcast.putExtra(ConstantsManager.EXTRA_TYPE, "Date");
                    broadcast.putStringArrayListExtra(ConstantsManager.EXTRA_ARRAYLIST_DATE_RATES_EUR, eur);
                    broadcast.putStringArrayListExtra(ConstantsManager.EXTRA_ARRAYLIST_DATE_RATES_RUR, rur);
                    broadcast.putStringArrayListExtra(ConstantsManager.EXTRA_ARRAYLIST_DATE_RATES_USD, usd);
                    sendBroadcast(broadcast);
                }, throwable -> {
                    Timber.e(throwable, "Error while loading data occurred!");
                });

        return START_STICKY;
    }
}
