package com.akchimwf.loftcoin1.util.formatter;

import androidx.annotation.NonNull;

public interface Formatter<T> {
    @NonNull
    String format(@NonNull T value);
}
