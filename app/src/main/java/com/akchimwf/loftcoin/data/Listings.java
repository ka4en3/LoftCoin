package com.akchimwf.loftcoin.data;

import com.google.auto.value.AutoValue;

/*Listing of coins*/
/*Data listing model*/
@AutoValue
abstract class Listings {
    abstract List<AutoValue_Coin> data();
}
