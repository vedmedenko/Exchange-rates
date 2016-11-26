package com.vedmedenko.exchangerates.injection.components;

import dagger.Component;

import com.squareup.haha.perflib.Main;
import com.vedmedenko.exchangerates.injection.PerActivity;
import com.vedmedenko.exchangerates.injection.modules.ActivityModule;
import com.vedmedenko.exchangerates.ui.activities.MainActivity;

@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {
    void inject(MainActivity activity);
}
