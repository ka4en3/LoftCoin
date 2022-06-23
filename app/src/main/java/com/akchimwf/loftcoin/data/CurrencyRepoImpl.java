package com.akchimwf.loftcoin.data;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.preference.PreferenceManager;

import com.akchimwf.loftcoin.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
        /*Not public as use Dagger and DI*/
class CurrencyRepoImpl implements CurrencyRepo {

    private static final String KEY_CURRENCY = "currency";

    private final Map<String, Currency> availableCurrencies = new HashMap<>();

    private SharedPreferences prefs;

    /*Context as input as we need localization from Resources*/
    /*Not public as use Dagger and DI*/
    @Inject
    CurrencyRepoImpl(@NonNull Context context) {
        this.prefs = PreferenceManager.getDefaultSharedPreferences(context);
        /*fill HashMap with currencies in constructor*/
        /*currency.code == key*/
        availableCurrencies.put("USD", Currency.create("$", "USD", context.getString(R.string.usd)));
        availableCurrencies.put("EUR", Currency.create("€", "EUR", context.getString(R.string.eur)));
        availableCurrencies.put("RUB", Currency.create("₽", "RUB", context.getString(R.string.rub)));
    }

    /*  old implementation
    @NonNull
    @Override
    public LiveData<List<Currency>> availableCurrencies() {
        return new AllCurrenciesLiveDate(context);
    }*/

    /*get available currencies as a live data*/
    @NonNull
    @Override
    public LiveData<List<Currency>> availableCurrencies() {
        final MutableLiveData<List<Currency>> liveData = new MutableLiveData<>();
        /*trick: HashMap.values -> List<Currency>*/
        liveData.setValue(new ArrayList<>(availableCurrencies.values()));
        return liveData;
    }

    /*get the current(stored in prefs) currency as a live data*/
    @NonNull
    @Override
    public LiveData<Currency> currency() {
        return new CurrencyLiveData();
    }

    /*save chosen currency to prefs*/
    @Override
    public void updateCurrency(@NonNull Currency currency) {
        prefs.edit().putString(KEY_CURRENCY, currency.code()).apply();
    }

    private class CurrencyLiveData extends LiveData<Currency> implements SharedPreferences.OnSharedPreferenceChangeListener {

        /* LiveData has {@link LiveData#onActive()} and {@link LiveData#onInactive()} methods
         * to get notified when number of active {@link Observer}s change between 0 and 1.
         * This allows LiveData to release any heavy resources when it does not have any Observers that
         * are actively observing.*/

        @Override
        protected void onActive() {
            /*Registers a callback to be invoked when a change happens to a preference.*/
            prefs.registerOnSharedPreferenceChangeListener(this);
            setValue(availableCurrencies.get(prefs.getString(KEY_CURRENCY, "USD")));
        }

        /*called after changed fragment*/
        @Override
        protected void onInactive() {
            /*Unregisters a previous callback.*/
            prefs.unregisterOnSharedPreferenceChangeListener(this);
        }

        @Override
        public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
            setValue(availableCurrencies.get(prefs.getString(key, "USD")));
        }
    }

    /*  old implementation
    //this class generates a list of currencies
    //we can do on a main thread as its only 3 currencies
    private static class AllCurrenciesLiveDate extends LiveData<List<Currency>> {

        private final Context context;

        AllCurrenciesLiveDate(Context context) {
            this.context = context;
        }

        //called when someone subscribed on this LiveData
        @Override
        protected void onActive() {
            List<Currency> currencies = new ArrayList<>();
            currencies.add(Currency.create("$", "USD", context.getString(R.string.usd)));
            currencies.add(Currency.create("€", "EUR", context.getString(R.string.eur)));
            currencies.add(Currency.create("₽", "RUB", context.getString(R.string.rub)));
            //Sets the value. If there are active observers, the value will be dispatched to them.
            setValue(currencies);  //onActive always calling on a main thread -> can use setValue
        }
    }
    */
}
