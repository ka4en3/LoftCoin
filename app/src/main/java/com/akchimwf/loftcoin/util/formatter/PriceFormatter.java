package com.akchimwf.loftcoin.util.formatter;

import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import java.util.Locale;

public class PriceFormatter implements Formatter<Double> {
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
