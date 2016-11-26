package com.vedmedenko.exchangerates;

import android.app.Application;
import android.content.Context;

import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.vedmedenko.exchangerates.injection.components.ApplicationComponent;
import com.vedmedenko.exchangerates.injection.components.DaggerApplicationComponent;
import com.vedmedenko.exchangerates.injection.modules.ApplicationModule;
import com.vedmedenko.exchangerates.utils.AlarmUtils;

import java.lang.ref.WeakReference;

import timber.log.Timber;

public class ExchangeRatesApplication extends Application {

    private ApplicationComponent applicationComponent;
    private RefWatcher refWatcher;

    @Override
    public void onCreate() {
        super.onCreate();

        initTimber();
        initLeakCanary();
        AlarmUtils.setupEverydayAlarm(this);
    }

    public ApplicationComponent getComponent() {
        if (applicationComponent == null) {
            applicationComponent = DaggerApplicationComponent.builder()
                    .applicationModule(new ApplicationModule(this))
                    .build();
        }

        return applicationComponent;
    }

    public static ExchangeRatesApplication get(WeakReference<Context> contextWeakReference) {
        return (ExchangeRatesApplication) contextWeakReference.get().getApplicationContext();
    }

    public static RefWatcher getRefWatcher(Context context) {
        return ((ExchangeRatesApplication) context.getApplicationContext()).refWatcher;
    }

    private void initTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }

    private void initLeakCanary() {
        refWatcher = LeakCanary.install(this);
    }
}
