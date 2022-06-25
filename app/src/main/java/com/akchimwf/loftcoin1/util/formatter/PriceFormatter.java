package com.akchimwf.loftcoin1.util.formatter;

import android.content.Context;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.os.ConfigurationCompat;
import androidx.core.os.LocaleListCompat;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class PriceFormatter implements Formatter<Double> {

    private static final Map<String, Locale> LOCALES = new HashMap<>();

    /*Static blocks are called at the time of class initialization (when the ClassLoader will load it into MetaSpace),
    and can be used to initialize static variables.*/
    static {
        LOCALES.put("RUB", new Locale("ru", "RU"));   //no RU locale constant predefined in Android
        LOCALES.put("EUR", Locale.GERMANY);
        LOCALES.put("USD", Locale.US);
    }

    private Context context;

    /*no @Binds, no @Provides in any @Module.
    Place PriceFormatter class in Component as is (because not binding interface or abstract to some custom realization)*/
    /*@Inject is enough*/
    @Inject
    PriceFormatter(Context context) {
        this.context = context;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @NonNull
    public String format(@NonNull String currency, @NonNull Double value) {

        /*Looking for current currency locale in HashMap*/
        Locale locale = LOCALES.get(currency);

        /*if locale not found*/
        if (locale == null) {
            /*get first locale for current configuration(device)*/
            final LocaleListCompat locales = ConfigurationCompat.getLocales(context.getResources().getConfiguration());
            locale = locales.get(0);
        }

        /*They've basically introduced this for the later API's to "reduce your APK footprint".
        I don't think that the ICU is a replacement for the java.*.* libraries but is just an alternative to use.
        There is also one interesting thing on that page:
            As the ICU team deprecates APIs in the future,
            Android will also mark them as deprecated but will continue to include them.*/
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {
            /*ICU stands for International Components for Unicode*/
            return android.icu.text.NumberFormat.getCurrencyInstance(locale).format(value);
        } else {
            /*getCurrencyInstance() - Returns a currency format for the current default FORMAT locale.*/
            return java.text.NumberFormat.getCurrencyInstance(locale).format(value);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @NonNull
    @Override
    public String format(@NonNull Double value) {
        /*They've basically introduced this for the later API's to "reduce your APK footprint".
        I don't think that the ICU is a replacement for the java.*.* libraries but is just an alternative to use.
        There is also one interesting thing on that page:
            As the ICU team deprecates APIs in the future,
            Android will also mark them as deprecated but will continue to include them.*/
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {
            /*ICU stands for International Components for Unicode*/
            return android.icu.text.NumberFormat.getCurrencyInstance().format(value);
        } else {
            /*getCurrencyInstance() - Returns a currency format for the current default FORMAT locale.*/
            return java.text.NumberFormat.getCurrencyInstance().format(value);
        }
    }
}
