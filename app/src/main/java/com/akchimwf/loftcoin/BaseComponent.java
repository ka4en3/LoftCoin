package com.akchimwf.loftcoin;

import android.content.Context;

import com.akchimwf.loftcoin.data.CoinsRepo;
import com.akchimwf.loftcoin.data.CurrencyRepo;
import com.akchimwf.loftcoin.util.ImageLoader;
import com.akchimwf.loftcoin.util.RxSchedulers;

/*is needed to use in dependencies in MainUIComponent, as using scoped components (like AppComponent) directly not allowed by Dagger */
public interface BaseComponent {
    /*when AppComponent needs Context, it will be taken from AppModule*/
    Context context();

    /*when AppComponent needs CoinsRepo, it will be taken from DataModule*/
    CoinsRepo coinsRepo();

    /*when AppComponent needs CurrencyRepo, it will be taken from DataModule*/
    CurrencyRepo currencyRepo();

    /*when AppComponent needs ImageLoader, it will be taken from UtilModule*/
    ImageLoader imageLoader();

    /*when AppComponent needs ImageLoader, it will be taken from UtilModule*/
    RxSchedulers schedulers();
}
