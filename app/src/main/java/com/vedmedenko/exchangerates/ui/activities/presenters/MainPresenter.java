package com.vedmedenko.exchangerates.ui.activities.presenters;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.vedmedenko.exchangerates.core.DataManager;
import com.vedmedenko.exchangerates.core.services.SyncService;
import com.vedmedenko.exchangerates.injection.ActivityContext;
import com.vedmedenko.exchangerates.ui.activities.base.BasePresenter;
import com.vedmedenko.exchangerates.ui.activities.views.MainMvpView;
import com.vedmedenko.exchangerates.utils.ConstantsManager;

import javax.inject.Inject;

import timber.log.Timber;

public class MainPresenter extends BasePresenter<MainMvpView> {

    private BroadcastReceiver broadcastReceiver;

    private final Context context;
    private final DataManager dataManager;

    @Inject
    public MainPresenter(@NonNull @ActivityContext Context context, @NonNull DataManager dataManager) {
        this.context = context;
        this.dataManager = dataManager;
    }

    @Override
    public void attach(MainMvpView mvpView) {
        super.attach(mvpView);
        registerReciever();
    }

    @Override
    public void detach() {
        super.detach();
        deregisterReciever();
    }

    public String[] getCurrentCurrency() {
        if (!dataManager.currentCurrencyLoaded())
            context.startService(SyncService.getStartIntent(context, true, false));

        return dataManager.getCurrentCurrency();
    }

    private void registerReciever() {
        broadcastReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {

                Bundle extras = intent.getExtras();

                String type = extras.getString(ConstantsManager.EXTRA_TYPE);

                if (type == null)
                    return;

                switch (type) {
                    case "Current":
                        getMvpView().refreshPage(0);
                    default:
                }
            }
        };

        context.registerReceiver(broadcastReceiver, new IntentFilter(ConstantsManager.BROADCAST));
    }

    private void deregisterReciever() {
        try {
            context.unregisterReceiver(broadcastReceiver);
        } catch (IllegalArgumentException | NullPointerException e) {
            Timber.d(e.getMessage());
        }
    }
}
