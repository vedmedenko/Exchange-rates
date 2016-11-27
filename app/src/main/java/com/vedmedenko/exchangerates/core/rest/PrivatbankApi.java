package com.vedmedenko.exchangerates.core.rest;

import android.support.annotation.NonNull;

import com.vedmedenko.exchangerates.core.rest.models.current.CurrentRates;
import com.vedmedenko.exchangerates.core.rest.models.date.DateRates;

import org.simpleframework.xml.convert.Convert;

import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;
import rx.Observable;

public interface PrivatbankApi {

    @GET
    Observable<CurrentRates> loadCurrentRates(@NonNull @Url String url);

    @GET("exchange_rates")
    Observable<DateRates> loadDateRates(@NonNull @Query("json") Boolean json, @NonNull @Query("date") String date);

}