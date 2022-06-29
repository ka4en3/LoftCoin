package com.akchimwf.loftcoin1.data;

import com.google.auto.value.AutoValue;

import java.util.List;

/*Listing of coins*/
/*Data listing model*/
@AutoValue
abstract class Listings {
    abstract List<AutoValue_CmcCoin> data();
}
