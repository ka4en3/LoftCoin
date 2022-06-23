package com.akchimwf.loftcoin.ui.rates;

import androidx.annotation.NonNull;
import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.akchimwf.loftcoin.data.CmcCoin;
import com.akchimwf.loftcoin.data.Coin;
import com.akchimwf.loftcoin.data.CoinsRepo;
import com.akchimwf.loftcoin.data.Currency;
import com.akchimwf.loftcoin.data.CurrencyRepo;
import com.akchimwf.loftcoin.data.SortBy;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TransferQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.inject.Inject;
import javax.inject.Singleton;

import timber.log.Timber;

/*ViewModel is a class that is responsible for preparing and managing the data for an Activity or a Fragment.
It also handles the communication of the Activity / Fragment with the rest of the application (e.g. calling the business logic classes).*/
/*ViewModel's only responsibility is to manage the data for the UI.
It should never access your view hierarchy or hold a reference back to the Activity or the Fragment.*/
@Singleton
public class RatesViewModel extends ViewModel {

    /*Live data to store list of coins*/
    private final LiveData<List<Coin>> coins;

    private final MutableLiveData<Boolean> isRefreshing = new MutableLiveData<>();

    /*A boolean value that may be updated atomically*/
    /*An AtomicBoolean is used in applications such as atomically updated flags, and cannot be used as a replacement for a Boolean.*/
    /*This flag is responsible for fetching data either from server or db*/
    /*Atomic - thread safety*/
    final MutableLiveData<AtomicBoolean> forceRefresh = new MutableLiveData<>(new AtomicBoolean(true));

    private final MutableLiveData<SortBy> sortBy = new MutableLiveData<>(SortBy.RANK);

    private int sortingIndex = 1; //PRICE by default

    // AppComponent(BaseComponent) -> MainUIComponent -> Fragment(BaseComponent) -> RatesComponent -> RatesViewModel()
    /*this is the whole chain how we push coinsRepo from BaseComponent to RatesViewModel*/
    @Inject
    public RatesViewModel(CoinsRepo coinsRepo, CurrencyRepo currencyRepo) {
        /*LiveData with Transformations is a kind of conveyor*/
        /*Transformations.map - when transform function returns Object*/
        /*Transformations.switchMap - when transform function returns LiveData */

        /*This is kind of reactive with LiveData*/
        /*(T|F) -> forceRefresh -> currency -> sortBy -> query -> listings*/

        /*create(transform) Query from forceRefresh and Currency*/
        /*forceRefresh is a upstream, start of a conveyor*/
        /*when forceRefresh or Currency changed -> Query changed automatically*/
        /*Currency is a downstream to forceRefresh, so changing Currency will change Query, but not forceRefresh*/
        final LiveData<CoinsRepo.Query> query = Transformations.switchMap(forceRefresh, r -> {

            /*nested transformations, when working with LiveData in a react way*/
            return Transformations.switchMap(currencyRepo.currency(), c -> {

                /*when change currency forceRefresh should be always true-> get data from server*/
                r.set(true);

                isRefreshing.postValue(true);

                /*nested transformations, when working with LiveData in a react way*/
                return Transformations.map(sortBy, s -> {

                    /*Query depends of variables: r, c and s*/
                    return CoinsRepo.Query.builder()

                            /*Atomically sets to the given value and returns the previous value*/
                            /*we need it here as don't need to get data from server(need to get from db) when sorting -> set r to false*/
                            /*first run r==true, but second run if only sorting was called r==false*/
                            .forceUpdate(r.getAndSet(false))
                            .sortBy(s)

                            .currency(c.code())
                            .build();
                });

            });

        });

        /*second step of a conveyor*/
        /*create(transform) List<Coin> from Query*/
        /*when Query changed -> List<Coin> changed automatically*/
        final LiveData<List<Coin>> coins = Transformations.switchMap(query, new Function<CoinsRepo.Query, LiveData<List<Coin>>>() {
            @Override
            public LiveData<List<Coin>> apply(CoinsRepo.Query q) {
                return coinsRepo.listings(q);
            }
        });

        /*RatesFragment is subscribed on this.coins*/

        /*empty Transformation=empty step of conveyor, -> place isRefreshing(false) here */
        this.coins = Transformations.map(coins, new Function<List<Coin>, List<Coin>>() {
            @Override
            public List<Coin> apply(List<Coin> input) {
                /*previous transformation could take time,
                so we add empty transformation to set isRefreshing=false here, just after previous transformation completed */
                isRefreshing.postValue(false);
                /*no any things to do here*/
                return input;
            }
        });
    }

    /*push data outside ViewModel*/
    @NonNull
    LiveData<List<Coin>> coins() {
        return coins;
    }

    @NonNull
    LiveData<Boolean> isRefreshing() {
        return isRefreshing;
    }

    final void refresh() {
        forceRefresh.postValue(new AtomicBoolean(true));
    }

    /*get next order index*/
    /*% length -> normal safe way to get all indexes without out of bounds error*/
    final void switchSortingOrder(){
        //0 % 2 = 0
        //1 % 2 = 1
        //2 % 2 = 0
        // 0 1 0 1
        /*sortingIndex++ to get next order index*/
        sortBy.postValue(SortBy.values()[sortingIndex++ % SortBy.values().length]);
    }
}
