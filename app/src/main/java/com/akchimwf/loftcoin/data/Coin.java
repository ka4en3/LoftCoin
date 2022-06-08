package com.akchimwf.loftcoin.data;

import com.google.auto.value.AutoValue;

/*Data model*/
/*this is Domain Layer according to Clean Architecture*/
@AutoValue
public abstract class Coin {
    public abstract int id();
    public abstract String name();
    public abstract String symbol();
}
