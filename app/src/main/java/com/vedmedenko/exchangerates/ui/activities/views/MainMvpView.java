package com.vedmedenko.exchangerates.ui.activities.views;

import android.support.annotation.NonNull;

import com.vedmedenko.exchangerates.ui.activities.base.MvpView;

import java.util.ArrayList;

public interface MainMvpView extends MvpView {
    void refreshPage(int page);
    void setDateCurrencies(@NonNull ArrayList<String> eur,
                           @NonNull ArrayList<String> rur,
                           @NonNull ArrayList<String> usd);
}
