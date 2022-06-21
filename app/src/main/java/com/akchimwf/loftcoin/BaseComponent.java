package com.akchimwf.loftcoin;

import android.content.Context;

import com.akchimwf.loftcoin.data.CoinsRepo;
import com.akchimwf.loftcoin.data.CurrencyRepo;

/*is needed to use in dependencies in MainUIComponent, as using scoped components (like AppComponent) directly not allowed by Dagger */
public interface BaseComponent {
    /*when AppComponent needs Context, it will be taken from AppModule*/
    abstract Context context();

    /*when AppComponent needs CoinsRepo, it will be taken from DataModule*/
    abstract CoinsRepo coinsRepo();

    /*when AppComponent needs CurrencyRepo, it will be taken from DataModule*/
    abstract CurrencyRepo currencyRepo();
}
