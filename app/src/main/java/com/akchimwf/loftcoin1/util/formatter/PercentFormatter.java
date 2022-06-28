package com.akchimwf.loftcoin1.util.formatter;

//TODO realize one formatter interface with different methods and bind to realization

import androidx.annotation.NonNull;

import java.util.Locale;

import javax.inject.Inject;

public class PercentFormatter implements Formatter<Double> {

    /*no @Binds, no @Provides in any @Module.
    Place PercentFormatter class in Component as is (because not binding interface or abstract to some custom realization)*/
    /*@Inject is enough*/
    @Inject
    public PercentFormatter() {
    }

    @NonNull
    @Override
    public String format(@NonNull Double value) {
        /*%.2f%%: .2 - 2 number after dot, %% - show percent symbol*/
        return String.format(Locale.US, "%.2f%%", value);
    }
}
