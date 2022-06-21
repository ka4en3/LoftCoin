package com.akchimwf.loftcoin.data;

import androidx.annotation.NonNull;

import com.google.auto.value.AutoValue;

/*model of a Currency*/
/*Java has its own Currency class, so we can use it if needs*/
@AutoValue
public abstract class Currency {

    /*factory method (=static?) for creating Currency class*/
    /*package private -> repo knows*/
    @NonNull
    static Currency create(String symbol, String code, String name){
        return new AutoValue_Currency(symbol, code, name);
    }

    public abstract String symbol();

    public abstract String code();

    public abstract String name();
}
