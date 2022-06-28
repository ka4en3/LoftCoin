package com.akchimwf.loftcoin1.data;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.preference.PreferenceManager;

import com.akchimwf.loftcoin1.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Cancellable;

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

    /*get available currencies as a live data*/
    @NonNull
    @Override
    public LiveData<List<Currency>> availableCurrencies() {
        final MutableLiveData<List<Currency>> liveData = new MutableLiveData<>();
        /*trick: HashMap.values -> List<Currency>*/
        liveData.setValue(new ArrayList<>(availableCurrencies.values()));
        return liveData;
    }

    /*get the current(stored in prefs) currency as a Observable*/
    @NonNull
    @Override
    public Observable<Currency> currency() {
        /*Provides an API (via a cold Observable) that bridges the reactive world with the callback-style world.*/
        return Observable.create(new ObservableOnSubscribe<Currency>() {  //create Observer and set ObservableOnSubscribe listener
            @Override
            public void subscribe(ObservableEmitter<Currency> emitter) throws Exception {

                /*create listener*/
                SharedPreferences.OnSharedPreferenceChangeListener listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
                    @Override
                    public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
                        if (!emitter.isDisposed()) {  //if emitter is still active
                            emitter.onNext(availableCurrencies.get(prefs.getString(key, "USD")));
                        }
                    }
                };

                /*register listener*/
                prefs.registerOnSharedPreferenceChangeListener(listener);

                /*unregister listener*/
                emitter.setCancellable(new Cancellable() {   //if all Observers unsubscribed
                    @Override
                    public void cancel() throws Exception {
                        prefs.unregisterOnSharedPreferenceChangeListener(listener);
                    }
                });

                /*default data pushed to emitter*/
                emitter.onNext(availableCurrencies.get(prefs.getString(KEY_CURRENCY, "USD")));
            }
        });
    }

    /*save chosen currency to prefs*/
    @Override
    public void updateCurrency(@NonNull Currency currency) {
        prefs.edit().putString(KEY_CURRENCY, currency.code()).apply();
    }
}
