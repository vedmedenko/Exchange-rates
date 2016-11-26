package com.vedmedenko.exchangerates.core.rest.models.current;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.ArrayList;

@Root(name = "exchangerate")
public class CurrentRates {

    @ElementList(inline=true)
    private ArrayList<CurrentRate> rates;

    public CurrentRates() {
        rates = new ArrayList<>();
    }

    public ArrayList<CurrentRate> getRates() {
        return rates;
    }

    public void setRates(ArrayList<CurrentRate> rates) {
        this.rates = rates;
    }
}
