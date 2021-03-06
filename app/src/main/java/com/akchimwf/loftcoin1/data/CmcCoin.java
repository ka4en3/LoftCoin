package com.akchimwf.loftcoin1.data;

import androidx.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.auto.value.extension.memoized.Memoized;
import com.squareup.moshi.Json;

import java.util.Iterator;
import java.util.Map;

/*Data model for HTTP*/
/*this is Domain Layer according to Clean Architecture*/
@AutoValue
/*abstract class implements interface and some of its methods*/
public abstract class CmcCoin implements Coin{

    @Override  // override interface method
    @Json(name = "cmc_rank")             //parce rank to cmc_rank
    @AutoValue.CopyAnnotations           //copy annotation to auto generated class
    public abstract int rank();

    @Override
    @Nullable   //then it can be Null, as we don't need it in CmcCoin model. in other case Moshi parser returns error
    public abstract String currencyCode();

    @Override  // override interface method
    @Memoized  // price should be cashed
    public double price() {
        /*Returns an Iterator<Quote> over the elements in this collection (quote()).
        There are no guarantees concerning the order in which the elements are returned */
        Iterator<? extends Quote> iterator = quote().values().iterator();
        /* only take the first currency in the Map! as only USD in REST-API response*/
        if (iterator.hasNext()) return iterator.next().price();
        return 0d;
    }

    @Override  // override interface method
    @Memoized  // change24h should be cashed
    public double change24h() {
        Iterator<? extends Quote> iterator = quote().values().iterator();
        if (iterator.hasNext()) return iterator.next().change24h();
        return 0d;
    }

    /*storing quotes in Map<String key=currency(USD,BTC), Quote>*/
    /*use AutoValue_Coin_Quote instead of Quote (abstraction) as Moshi doesn't work in this way for some reason*/
    abstract Map<String, AutoValue_CmcCoin_Quote> quote();

    /*Cmc API returns object Quote with fields price and percent_change_24 (grouped by currency (USD,BTC))*/
    @AutoValue
    abstract static class Quote {
        public abstract double price();

        @Json(name = "percent_change_24h")   //parce change24h to percent_change_24h
        @AutoValue.CopyAnnotations           //copy annotation to auto generated class
        public abstract double change24h();
    }
}
