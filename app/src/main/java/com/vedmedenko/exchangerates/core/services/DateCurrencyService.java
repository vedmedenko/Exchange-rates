package com.vedmedenko.exchangerates.core.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.Pair;

import com.vedmedenko.exchangerates.ExchangeRatesApplication;
import com.vedmedenko.exchangerates.core.DataManager;
import com.vedmedenko.exchangerates.core.rest.models.date.DateRate;
import com.vedmedenko.exchangerates.injection.ActivityContext;
import com.vedmedenko.exchangerates.ui.fragments.ChartsFragment;
import com.vedmedenko.exchangerates.utils.ConstantsManager;

import java.lang.ref.WeakReference;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;
import java.util.Set;
import java.util.TreeMap;

import javax.inject.Inject;

import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

public class DateCurrencyService extends Service {

    @Inject
    DataManager dataManager;

    private static TreeMap<String, Pair<Float, Float>> monthData;

    private CompositeSubscription compositeSubscription;

    public static Intent getStartIntent(@NonNull @ActivityContext Context context, @NonNull String date, boolean oneShot) {
        Intent intent = new Intent(context, DateCurrencyService.class);
        Bundle extras = new Bundle();
        extras.putString(ConstantsManager.EXTRA_STRING_DATE, date);
        extras.putBoolean(ConstantsManager.EXTRA_BOOLEAN, oneShot);
        intent.putExtras(extras);
        return intent;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ExchangeRatesApplication.get(new WeakReference<>(getBaseContext())).getComponent().inject(this);
        compositeSubscription = new CompositeSubscription();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (compositeSubscription != null)
            compositeSubscription.unsubscribe();

        monthData.clear();
        monthData = null;
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
        final boolean oneShot = extras.getBoolean(ConstantsManager.EXTRA_BOOLEAN);

        if (date == null)
            return START_NOT_STICKY;

        if (oneShot) {
            compositeSubscription.add(dataManager.loadDateRates(date)
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
                    }));
        } else {

            if (monthData == null) {
                final SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy", Locale.US);

                monthData = new TreeMap<>(new Comparator<String>() {
                    @Override
                    public int compare(String s1, String s2) {
                        Date date1 = null, date2 = null;
                        int i;

                        try {
                            date1 = format.parse(s1);
                            date2 = format.parse(s2);
                        } catch (ParseException e) {
                            Timber.d("Date parsing exception: " + e.getMessage());
                        } finally {
                            if (date1 == null)
                                i = -1;
                            else if (date2 == null)
                                i = 1;
                            else
                                i = date1.compareTo(date2);
                        }

                        return i;
                    }
                });

            }

            compositeSubscription.add(dataManager.loadDateRates(date)
                    .subscribeOn(Schedulers.io())
                    .doAfterTerminate(() -> {
                    })
                    .subscribe(dateRates -> {
                        ArrayList<DateRate> rates = dateRates.getRates();

                        Float eurSale = 0.0f, usdSale = 0.0f;

                        for (DateRate rate : rates) {
                            if (rate.getCurrency().equals("EUR")) {
                                eurSale = rate.getSaleRate();
                            }

                            if (rate.getCurrency().equals("USD")) {
                                usdSale = rate.getSaleRate();
                            }
                        }

                        monthData.put(date, new Pair<>(eurSale, usdSale));

                        Timber.d(monthData.size() + "");

                        if (monthData.size() == ChartsFragment.DAY_COUNT) {

                            ArrayList<String> eur = new ArrayList<>(ChartsFragment.DAY_COUNT);
                            ArrayList<String> usd = new ArrayList<>(ChartsFragment.DAY_COUNT);

                            Set<String> keys = monthData.keySet();

                            for (String key : keys) {
                                Pair pair = monthData.get(key);

                                if (pair == null) {
                                    Timber.d(key);
                                    usd.add("0.0f");
                                    eur.add("0.0f");
                                } else {
                                    usd.add(pair.second.toString());
                                    eur.add(pair.first.toString());
                                }
                            }

                            Intent broadcast = new Intent(ConstantsManager.BROADCAST);
                            broadcast.putExtra(ConstantsManager.EXTRA_TYPE, "DateNotOneShot");
                            broadcast.putStringArrayListExtra(ConstantsManager.EXTRA_ARRAYLIST_DATE_RATES_EUR, eur);
                            broadcast.putStringArrayListExtra(ConstantsManager.EXTRA_ARRAYLIST_DATE_RATES_USD, usd);
                            sendBroadcast(broadcast);

                            monthData.clear();
                            monthData = null;
                        }

                    }, throwable -> {
                        Timber.e(throwable, "Error while loading data occurred!");
                    }));
        }

        return START_STICKY;
    }
}
