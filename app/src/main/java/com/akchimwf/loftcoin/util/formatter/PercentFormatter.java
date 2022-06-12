package com.akchimwf.loftcoin.util.formatter;

import androidx.annotation.NonNull;

import java.util.Locale;

public class PercentFormatter implements Formatter<Double> {
    @NonNull
    @Override
    public String format(@NonNull Double value) {
        /*%.2f%%: .2 - 2 number after dot, %% - show percent symbol*/
        return String.format(Locale.US, "%.2f%%", value);
    }
}
