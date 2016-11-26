package com.vedmedenko.exchangerates.core.rest.models.date;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class DateRates {

    private String date;
    private String bank;
    private Integer baseCurrency;
    private String baseCurrencyLit;

    @SerializedName("exchangeRate")
    private ArrayList<DateRate> rates;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public Integer getBaseCurrency() {
        return baseCurrency;
    }

    public void setBaseCurrency(Integer baseCurrency) {
        this.baseCurrency = baseCurrency;
    }

    public String getBaseCurrencyLit() {
        return baseCurrencyLit;
    }

    public void setBaseCurrencyLit(String baseCurrencyLit) {
        this.baseCurrencyLit = baseCurrencyLit;
    }

    public ArrayList<DateRate> getRates() {
        return rates;
    }

    public void setRates(ArrayList<DateRate> rates) {
        this.rates = rates;
    }
}
