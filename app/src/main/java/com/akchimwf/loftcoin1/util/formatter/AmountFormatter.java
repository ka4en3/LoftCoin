package com.akchimwf.loftcoin1.util.formatter;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.core.os.ConfigurationCompat;
import androidx.core.os.LocaleListCompat;

import com.akchimwf.loftcoin1.data.Transaction;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class AmountFormatter implements Formatter<Transaction> {

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
    Place AmountFormatter class in Component as is (because not binding interface or abstract to some custom realization)*/
    /*@Inject is enough*/
    @Inject
    AmountFormatter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public String format(@NonNull Transaction transaction) {
        /*Looking for "EUR" currency locale in HashMap*/
        Locale locale = LOCALES.get("EUR");

        /*if locale not found*/
        if (locale == null) {
            /*get first locale for current configuration(device)*/
            final LocaleListCompat locales = ConfigurationCompat.getLocales(context.getResources().getConfiguration());
            locale = locales.get(0);
        }

        final DecimalFormat format = (DecimalFormat) NumberFormat.getCurrencyInstance(locale);
        final DecimalFormatSymbols symbols = format.getDecimalFormatSymbols();
        symbols.setCurrencySymbol(transaction.coin().symbol());
        format.setDecimalFormatSymbols(symbols);
        return format.format(transaction.amount());
    }

}