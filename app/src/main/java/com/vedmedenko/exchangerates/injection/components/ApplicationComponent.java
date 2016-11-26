package com.vedmedenko.exchangerates.injection.components;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import javax.inject.Singleton;

import dagger.Component;
import com.vedmedenko.exchangerates.core.rest.PrivatbankApi;
import com.vedmedenko.exchangerates.core.rest.RestModule;
import com.vedmedenko.exchangerates.core.services.SyncService;
import com.vedmedenko.exchangerates.injection.ApplicationContext;
import com.vedmedenko.exchangerates.injection.modules.ApplicationModule;

@Singleton
@Component(modules = {ApplicationModule.class, RestModule.class})
public interface ApplicationComponent {

    void inject(SyncService service);

    @ApplicationContext
    Context context();

    Application application();

    PrivatbankApi privatbankApi();

    SharedPreferences sharedPreferences();
}
