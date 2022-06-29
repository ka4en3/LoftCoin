package com.akchimwf.loftcoin1.ui.converter;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.akchimwf.loftcoin1.data.Coin;
import com.akchimwf.loftcoin1.data.CoinsRepo;
import com.akchimwf.loftcoin1.data.Currency;
import com.akchimwf.loftcoin1.data.CurrencyRepo;
import com.akchimwf.loftcoin1.util.RxSchedulers;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.Subject;

/*ViewModel is a class that is responsible for preparing and managing the data for an Activity or a Fragment.
It also handles the communication of the Activity / Fragment with the rest of the application (e.g. calling the business logic classes).*/
/*ViewModel's only responsibility is to manage the data for the UI.
It should never access your view hierarchy or hold a reference back to the Activity or the Fragment.*/
@Singleton
class ConverterViewModel extends ViewModel {

    /*no value by default as don't know what user choose*/
    private final Subject<Coin> fromCoin = BehaviorSubject.create();
    private final Subject<Coin> toCoin = BehaviorSubject.create();

    private final Subject<String> fromValue = BehaviorSubject.create();
    private final Subject<String> toValue = BehaviorSubject.create();

    private final Observable<List<Coin>> topCoins;

    private final Observable<Double> factor;

    private final RxSchedulers schedulers;

    private Boolean firstRun = true;

    @Inject
    ConverterViewModel(CoinsRepo coinsRepo, CurrencyRepo currencyRepo, RxSchedulers schedulers) {
        this.schedulers = schedulers;
        topCoins = currencyRepo.currency()
                .switchMap(currency -> coinsRepo.topCoins(currency))
                .doOnNext(coins -> {
                    if (firstRun) fromCoin.onNext(coins.get(0));  //default value  //TODO set top 3 coins not hardcoded
                    if (firstRun) toCoin.onNext(coins.get(1));
                    firstRun = false;
                })

                .replay(1)
                .autoConnect()
        ;

        /*calculating factor*/
        factor = fromCoin
                .flatMap((fromCoin) -> toCoin
                        .map((tc) -> fromCoin.price() / tc.price())
                )
                .replay(1)
                .autoConnect()
        ;
    }

    /*get top 3 coins for coins sheet*/
    @NonNull
    Observable<List<Coin>> topCoins() {
        return topCoins.observeOn(schedulers.main());
    }

    /*getter*/
    @NonNull
    Observable<Coin> fromCoin() {
        return fromCoin.observeOn(schedulers.main());
    }

    /*getter*/
    @NonNull
    Observable<Coin> toCoin() {
        return toCoin.observeOn(schedulers.main());
    }

    /*getter*/
    @NonNull
    Observable<String> fromValue() {
        return fromValue.observeOn(schedulers.main());
    }

    /*getter*/
    @NonNull
    Observable<String> toValue() {
        return fromValue
                .observeOn(schedulers.cmp())                            //whole below stream on computation thread (not subscribe())
                .map((s) -> s.isEmpty() ? "0.0" : s)                    //check if input is empty
                .map(Double::parseDouble)                               //get double from string
                .flatMap((value) -> factor.map((f) -> value * f))       //calculation
                .map(v -> String.format(Locale.US, "%.2f", v))    //format
                .map((v) -> "0.0".equals(v) ? "" : v)                   //if input is empty -> output is empty too(not 0.0)
                .observeOn(schedulers.main());                          //return to main thread
    }

    void fromCoin(Coin coin) {
        fromCoin.onNext(coin);
    }

    void toCoin(Coin coin) {
        toCoin.onNext(coin);
    }

    void fromValue(CharSequence text) {
        fromValue.onNext(text.toString());
    }

    void toValue(CharSequence text) {
        toValue.onNext(text.toString());
    }
}
