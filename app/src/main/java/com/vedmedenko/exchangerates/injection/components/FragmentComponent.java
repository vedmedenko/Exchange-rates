package com.vedmedenko.exchangerates.injection.components;

import dagger.Component;
import com.vedmedenko.exchangerates.injection.PerFragment;
import com.vedmedenko.exchangerates.injection.modules.FragmentModule;
import com.vedmedenko.exchangerates.ui.fragments.ChartsFragment;
import com.vedmedenko.exchangerates.ui.fragments.CurrentRatesFragment;
import com.vedmedenko.exchangerates.ui.fragments.DateRatesFragment;

@PerFragment
@Component(dependencies = ActivityComponent.class, modules = FragmentModule.class)
public interface FragmentComponent {
    void inject(CurrentRatesFragment fragment);
    void inject(DateRatesFragment fragment);
    void inject(ChartsFragment fragment);
}
