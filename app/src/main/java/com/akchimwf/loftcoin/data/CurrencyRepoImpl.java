package com.akchimwf.loftcoin.data;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.akchimwf.loftcoin.R;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
/*Not public as use Dagger and DI*/
class CurrencyRepoImpl implements CurrencyRepo {

    private final Context context;

    /*Context as input as we need localization from Resources*/
    /*Not public as use Dagger and DI*/
    @Inject
    CurrencyRepoImpl(@NonNull Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public LiveData<List<Currency>> availableCurrencies() {
        return new AllCurrenciesLiveDate(context);
    }

    @NonNull
    @Override
    public LiveData<Currency> currency() {
        return null;
    }

    @Override
    public void updateCurrency(@NonNull Currency currency) {

    }

    /*this class generates a list of currencies*/
    /*we can do on a main thread as its only 3 currencies*/
    private static class AllCurrenciesLiveDate extends LiveData<List<Currency>> {

        private final Context context;

        AllCurrenciesLiveDate(Context context) {
            this.context = context;
        }

        /*called when someone subscribed on this LiveData*/
        @Override
        protected void onActive() {
            List<Currency> currencies = new ArrayList<>();
            currencies.add(Currency.create("$", "USD", context.getString(R.string.usd)));
            currencies.add(Currency.create("€", "EUR", context.getString(R.string.eur)));
            currencies.add(Currency.create("₽", "RUB", context.getString(R.string.rub)));
            /*Sets the value. If there are active observers, the value will be dispatched to them.*/
            setValue(currencies);  //onActive always calling on a main thread -> can use setValue
        }
    }
}
