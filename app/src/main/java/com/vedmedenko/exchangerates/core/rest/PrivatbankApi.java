package com.vedmedenko.exchangerates.core.rest;

import android.support.annotation.NonNull;

import com.vedmedenko.exchangerates.core.rest.converters.Json;
import com.vedmedenko.exchangerates.core.rest.converters.Xml;
import com.vedmedenko.exchangerates.core.rest.models.current.CurrentRates;
import com.vedmedenko.exchangerates.core.rest.models.date.DateRates;

import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;
import rx.Observable;

public interface PrivatbankApi {

    @GET @Xml
    Observable<CurrentRates> loadCurrentRates(@NonNull @Url String url);


    @GET("exchange_rates") @Json
    Observable<DateRates> loadDateRates(@NonNull @Query("json") Boolean json, @NonNull @Query("date") String date);

}

