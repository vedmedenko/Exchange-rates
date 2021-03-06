package com.vedmedenko.exchangerates.core.rest;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

import com.vedmedenko.exchangerates.core.rest.converters.RetrofitUniversalConverter;
import com.vedmedenko.exchangerates.utils.ConstantsManager;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

@Module
public class RestModule {

    @Provides
    @Singleton
    public HttpLoggingInterceptor providesHttpLogginInterceptor() {
        return new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);
    }

    @Provides
    @Singleton
    public OkHttpClient providesOkHttpClient(@NonNull HttpLoggingInterceptor loggingInterceptor) {
        return new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .connectTimeout(ConstantsManager.CONNECTION_TIME_OUT, TimeUnit.SECONDS)
                .readTimeout(ConstantsManager.READ_TIME_OUT, TimeUnit.SECONDS)
                .build();
    }

    @Provides
    @Singleton
    public Gson providesGson() {
        return new GsonBuilder().create();
    }

    @Provides
    @Singleton
    public Retrofit providesRetrofit(@NonNull OkHttpClient okHttpClient,
                                     @NonNull RetrofitUniversalConverter converter) {
        return new Retrofit.Builder()
                .baseUrl(ConstantsManager.BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(converter)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }

    @Provides
    @Singleton
    public PrivatbankApi providesPrivatbankApi(@NonNull Retrofit retrofit) {
        return retrofit.create(PrivatbankApi.class);
    }
}
