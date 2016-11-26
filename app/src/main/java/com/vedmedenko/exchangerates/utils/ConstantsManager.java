package com.vedmedenko.exchangerates.utils;

public final class ConstantsManager {

    public static final String CURRENT_CURRENCY_DEFAULT_VALUE = "0.00";

    // Alarms

    public static final int REPEATING_ALARM_REPEAT_TIME_SECONDS = 20;

    // Broadcast constants

    public static final String BROADCAST = "com.vedmedenko.exchangerates.BROADCAST";

    public static final String EXTRA_BOOLEAN = "com.vedmedenko.exchangerates.EXTRA_BOOLEAN";
    public static final String EXTRA_BOOLEAN_ALARM = "com.vedmedenko.exchangerates.EXTRA_BOOLEAN_ALARM";
    public static final String EXTRA_TYPE = "com.vedmedenko.exchangerates.EXTRA_TYPE";
    public static final String EXTRA_CURRENCY_EUR = "com.vedmedenko.exchangerates.EXTRA_CURRENCY_EUR";
    public static final String EXTRA_CURRENCY_RUR = "com.vedmedenko.exchangerates.EXTRA_CURRENCY_RUR";
    public static final String EXTRA_CURRENCY_USD = "com.vedmedenko.exchangerates.EXTRA_CURRENCY_USD";

    // Preferences constants

    public static final String PREFERENCE_CURRENT_EUR = "com.vedmedenko.exchangerates.PREFERENCE_CURRENT_EUR";
    public static final String PREFERENCE_CURRENT_RUR = "com.vedmedenko.exchangerates.PREFERENCE_CURRENT_RUR";
    public static final String PREFERENCE_CURRENT_USD = "com.vedmedenko.exchangerates.PREFERENCE_CURRENT_USD";

    // Service constants

    public static final int SYNC_TIME_OUT = 2 * 1000;

    // Retrofit constants.

    public static final String BASE_URL = "https://api.privatbank.ua/p24api/";
    public static final String CURRENT_RATES_REQUEST_URL = "https://privat24.privatbank.ua/p24/accountorder?oper=prp&PUREXML&apicour&country=ua";

    // RestModule constants.

    public static final int CONNECTION_TIME_OUT = 50;
    public static final int READ_TIME_OUT = 50;

    private ConstantsManager() {
        throw new AssertionError("No instances");
    }
}
