package com.akchimwf.loftcoin1;

import android.content.Context;

import com.akchimwf.loftcoin1.data.CoinsRepo;
import com.akchimwf.loftcoin1.data.CurrencyRepo;
import com.akchimwf.loftcoin1.data.WalletsRepo;
import com.akchimwf.loftcoin1.util.ImageLoader;
import com.akchimwf.loftcoin1.util.Notifier;
import com.akchimwf.loftcoin1.util.RxSchedulers;

/*is needed to use in dependencies in MainUIComponent, as using scoped components (like AppComponent) directly not allowed by Dagger */
/*all listed here classes will be available from every Component has BaseComponent as dependency*/
public interface BaseComponent {
    /*will be taken from AppModule*/
    Context context();

    /*will be taken from DataModule*/
    CoinsRepo coinsRepo();

    /*will be taken from DataModule*/
    CurrencyRepo currencyRepo();

    /*will be taken from DataModule*/
    WalletsRepo walletsRepo();

    /*will be taken from UtilModule*/
    ImageLoader imageLoader();

    /*will be taken from UtilModule*/
    RxSchedulers schedulers();

    /*will be taken from UtilModule*/
    Notifier notifier();
}
