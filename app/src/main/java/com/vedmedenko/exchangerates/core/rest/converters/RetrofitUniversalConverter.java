package com.vedmedenko.exchangerates.core.rest.converters;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.vedmedenko.exchangerates.core.rest.models.date.DateRates;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.inject.Inject;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

public class RetrofitUniversalConverter extends Converter.Factory {

    private final Converter.Factory xml;
    private final Converter.Factory json;

    @Inject
    public RetrofitUniversalConverter(@NonNull Gson gson) {
        xml = SimpleXmlConverterFactory.create();
        json = GsonConverterFactory.create(gson);
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {

        if (type.toString().contains("CurrentRates")) {
            return xml.responseBodyConverter(type, annotations, retrofit);
        }

        if (type.toString().contains("DateRates")) {
            return json.responseBodyConverter(type, annotations, retrofit);
        }

        return null;
    }
}
