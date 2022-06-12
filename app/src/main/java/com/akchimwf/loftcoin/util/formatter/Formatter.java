package com.akchimwf.loftcoin.util.formatter;

import androidx.annotation.NonNull;

public interface Formatter<T> {
    @NonNull
    String format(@NonNull T value);
}
