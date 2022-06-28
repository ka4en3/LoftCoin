package com.akchimwf.loftcoin1.ui.rates;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.akchimwf.loftcoin1.data.Coin;
import com.akchimwf.loftcoin1.data.CoinsRepo;
import com.akchimwf.loftcoin1.data.CurrencyRepo;
import com.akchimwf.loftcoin1.data.SortBy;
import com.akchimwf.loftcoin1.util.RxSchedulers;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

/*ViewModel is a class that is responsible for preparing and managing the data for an Activity or a Fragment.
It also handles the communication of the Activity / Fragment with the rest of the application (e.g. calling the business logic classes).*/
/*ViewModel's only responsibility is to manage the data for the UI.
It should never access your view hierarchy or hold a reference back to the Activity or the Fragment.*/
@Singleton
class RatesViewModel extends ViewModel {

    /*Observable to store list of coins*/
    private final Observable<List<Coin>> coins;

    /*Subject is analog of MutableLiveData*/
    /*Subjects are an extension of Observable while also implementing the Observer interface.
    The idea may seem strange, but in certain cases they make some things much easier.
    They can receive event messages (as an observer) and report them to their subscribers (as an observable).
    This makes them an ideal starting point for getting started with Rx code: when you have data coming from outside,
    you can pass it to a Subject, thus turning it into an observable.*/
    /*BehaviorSubject - Subject that emits the most recent item it has observed and all subsequent observed items to each subscribed Observer.*/
    private final Subject<Boolean> isRefreshing = BehaviorSubject.create();

    /*split pullToRefresh to 2 fields - pullToRefresh(Subject) and forceUpdate(AtomicBoolean). Not need everytime to recreate AtomicBoolean object in refresh()*/
    /*data type inside Subject is not important*/
    final Subject<Class<?>> pullToRefresh = BehaviorSubject.createDefault(Void.TYPE);  //defaultValue - the item that will be emitted first to any Observer as long as the BehaviorSubject has not yet observed any items from its source Observable
    /*A boolean value that may be updated atomically*/
    /*An AtomicBoolean is used in applications such as atomically updated flags, and cannot be used as a replacement for a Boolean.*/
    /*This flag is responsible for fetching data either from server or db*/
    /*Atomic - thread safety*/
    final AtomicBoolean forceUpdate = new AtomicBoolean();

    private final Subject<SortBy> sortBy = BehaviorSubject.createDefault(SortBy.RANK);

    private int sortingIndex = 1; //PRICE by default

    private CoinsRepo coinsRepo;
    private CurrencyRepo currencyRepo;
    private RxSchedulers schedulers;

    /*ERROR HANDLING*/
    /*PublishSubject: A Subject that emits (multicasts) items to currently subscribed Observers and terminal events to current or late Observers.*/
    /*PublishSubject: in order to emmit error only once, second subscription to this Subject gives nothing. One-shot action.*/
    private final Subject<Throwable> error = PublishSubject.create();
    private final Subject<Class<?>> onRetry = PublishSubject.create();

    // AppComponent(BaseComponent) -> MainUIComponent -> Fragment(BaseComponent) -> RatesComponent -> RatesViewModel()
    /*this is the whole chain how we push coinsRepo from BaseComponent to RatesViewModel*/
    @Inject
    RatesViewModel(CoinsRepo coinsRepo, CurrencyRepo currencyRepo, RxSchedulers schedulers) {

        this.schedulers = schedulers;

        /*Kind of conveyor*/
        /*map - when function returns Observable*/
        /*switchMap - when function returns ObservableSource */

        /*(T|F) -> pullToRefresh -> currency -> sortBy -> query -> listings*/

        /*create Query from forceRefresh and Currency first*/
        /*forceRefresh is a upstream, start of a conveyor*/
        /*when forceRefresh or Currency changed -> Query changed automatically*/
        /*Currency is a downstream to forceRefresh, so changing Currency will change Query, but not forceRefresh*/

        this.coins = pullToRefresh  //initial trigger
                .map(pullToRefresh -> CoinsRepo.Query.builder())             //returns Observable<CoinsRepo.Query.Builder>

                .switchMap(builder -> currencyRepo.currency()                //get currency and place to query
                        .map(currency -> builder.currency(currency.code()))  //nested map, only happen when upstream executed
                )

                /*when change currency forceUpdate should be always true-> get data from server*/
                .doOnNext(builder -> forceUpdate.set(true))   //side effect to do something with stream, builder stays untouched

                .doOnNext(builder -> isRefreshing.onNext(true))

                .switchMap(builder -> sortBy
                        .map(srtBy -> builder.sortBy(srtBy))  //same for sortBy, builder here already modified by currency
                )

                /*Atomically sets to the given value and returns the previous value*/
                /*we need it here as don't need to get data from server(need to get from db) when sorting -> set forceUpdate to false*/
                /*first run forceUpdate==true, but second run if only sorting was called forceUpdate==false*/
                .map(builder -> builder.forceUpdate(forceUpdate.getAndSet(false)))  //map as forceUpdate is not a stream, so transform one result ot another

                .map(builder -> builder.build())                    //finally build query

                .switchMap(query -> coinsRepo.listings(query))      //get coins from repo with built query

                /*Error happened means terminating stream -> need to handle errors properly*/
                .doOnError(throwable -> error.onNext(throwable))    //catch error in ViewModel
                .retryWhen(throwableObservable -> onRetry)          //freeze stream until onRetry has no data. Data will come after press button Retry in this case.
//                .onErrorReturnItem(Collections.emptyList())       //after this step error is properly handled->can continue stream->empty list

                .doOnEach(listNotification -> isRefreshing.onNext(false))  //in any case hide refresher (could be error), Notification - object with information about the stream: finished, error, ...

                .replay(1)
                .autoConnect()
        ;
    }

    /*push data outside ViewModel*/
    @NonNull
    Observable<List<Coin>> coins() {
        /* coinsRepo.listings executing on the IO thread, but we need to observe data on main thread*/
        /*Modifies an ObservableSource to perform its emissions and notifications on a specified Scheduler,*/
        return coins.observeOn(schedulers.main());
    }

    @NonNull
    Observable<Boolean> isRefreshing() {
        return isRefreshing.observeOn(schedulers.main());
    }

    @NonNull
    Observable<Throwable> onError() {
        return error.observeOn(schedulers.main());
    }

    final void refresh() {
        pullToRefresh.onNext(Void.TYPE);  //this is just a trigger to refresh, so everytime put here same Void.TYPE when needs to refresh
    }

    /*get next order index*/
    final void switchSortingOrder() {
        /*% length -> normal safe way to get all indexes without out of bounds error*/
        //0 % 2 = 0
        //1 % 2 = 1
        //2 % 2 = 0
        // 0 1 0 1
        /*sortingIndex++ to get next order index*/
        sortBy.onNext(SortBy.values()[sortingIndex++ % SortBy.values().length]);   //trigger to sort
    }

    void retry() {
        onRetry.onNext(Void.class);
    }
}
