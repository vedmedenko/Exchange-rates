package com.vedmedenko.exchangerates.core;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import javax.inject.Inject;

import com.vedmedenko.exchangerates.core.rest.PrivatbankApi;
import com.vedmedenko.exchangerates.core.rest.models.current.CurrentRates;
import com.vedmedenko.exchangerates.core.rest.models.date.DateRates;
import com.vedmedenko.exchangerates.utils.ConstantsManager;

import rx.Observable;


public class DataManager {

    private final PrivatbankApi privatbankApi;
    private final SharedPreferences preferences;

    @Inject
    public DataManager(@NonNull PrivatbankApi privatbankApi, @NonNull SharedPreferences preferences) {
        this.privatbankApi = privatbankApi;
        this.preferences = preferences;
    }

    @NonNull
    public Observable<CurrentRates> loadCurrentRates() {
        return privatbankApi.loadCurrentRates(ConstantsManager.CURRENT_RATES_REQUEST_URL);
    }

    @NonNull
    public Observable<DateRates> loadDateRates(@NonNull String date) {
        return privatbankApi.loadDateRates(true, date);
    }

    public boolean currentCurrencyLoaded() {
        return preferences.contains(ConstantsManager.PREFERENCE_CURRENT_EUR) &&
                preferences.contains(ConstantsManager.PREFERENCE_CURRENT_RUR) &&
                preferences.contains(ConstantsManager.PREFERENCE_CURRENT_USD);
    }

    public void saveCurrentCurrency(@NonNull String id, @NonNull String value) {
        switch (id) {
            case "EUR":
                preferences.edit().putString(ConstantsManager.PREFERENCE_CURRENT_EUR, value).apply();
                return;
            case "RUR":
                preferences.edit().putString(ConstantsManager.PREFERENCE_CURRENT_RUR, value).apply();
                return;
            case "USD":
                preferences.edit().putString(ConstantsManager.PREFERENCE_CURRENT_USD, value).apply();
            default:
        }
    }

    public String[] getCurrentCurrency() {
        return new String[] {
                preferences.getString(ConstantsManager.PREFERENCE_CURRENT_EUR,
                        ConstantsManager.CURRENT_CURRENCY_DEFAULT_VALUE),
                preferences.getString(ConstantsManager.PREFERENCE_CURRENT_RUR,
                        ConstantsManager.CURRENT_CURRENCY_DEFAULT_VALUE),
                preferences.getString(ConstantsManager.PREFERENCE_CURRENT_USD,
                        ConstantsManager.CURRENT_CURRENCY_DEFAULT_VALUE)
        };
    }
}
