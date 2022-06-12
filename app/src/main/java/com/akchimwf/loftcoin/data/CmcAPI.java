package com.akchimwf.loftcoin.data;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/*Working with REST-API with CoinMarketCap*/
/*https://coinmarketcap.com/api/documentation/v1/  - QuickStart guide*/
/*this is Domain Layer according to Clean Architecture*/
public interface CmcAPI {
    /*API_KEY header, described as in QS guide*/
    String API_KEY = "X-CMC_PRO_API_KEY";

    /*REST-API GET request*/
    @GET("cryptocurrency/listings/latest")
    Call<Listings> listings(@Query("convert") String convert);
}
