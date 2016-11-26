package com.vedmedenko.exchangerates.core.rest;

import android.support.annotation.NonNull;

import com.vedmedenko.exchangerates.core.rest.models.current.CurrentRates;
import com.vedmedenko.exchangerates.core.rest.models.date.DateRates;

import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;
import rx.Observable;

public interface PrivatbankApi {

    @GET
    Observable<CurrentRates> loadCurrentRates(@NonNull @Url String url);

    @GET("exchange_rates?json")
    Observable<DateRates> loadDateRates(@NonNull @Query("date") String date);

}