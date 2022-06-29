package com.akchimwf.loftcoin1.ui.wallets;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.akchimwf.loftcoin1.data.Currency;
import com.akchimwf.loftcoin1.data.CurrencyRepo;
import com.akchimwf.loftcoin1.data.Transaction;
import com.akchimwf.loftcoin1.data.Wallet;
import com.akchimwf.loftcoin1.data.WalletsRepo;
import com.akchimwf.loftcoin1.util.RxSchedulers;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.Subject;
import timber.log.Timber;

/*ViewModel is a class that is responsible for preparing and managing the data for an Activity or a Fragment.
It also handles the communication of the Activity / Fragment with the rest of the application (e.g. calling the business logic classes).*/
/*ViewModel's only responsibility is to manage the data for the UI.
It should never access your view hierarchy or hold a reference back to the Activity or the Fragment.*/
@Singleton
class WalletsViewModel extends ViewModel {

    /*Observable to store list of wallets*/
    private final Observable<List<Wallet>> wallets;

    /*Observable to store list of transactions*/
    private final Observable<List<Transaction>> transactions;

    /*Subject is analog of MutableLiveData*/
    /*Subjects are an extension of Observable while also implementing the Observer interface.
    The idea may seem strange, but in certain cases they make some things much easier.
    They can receive event messages (as an observer) and report them to their subscribers (as an observable).
    This makes them an ideal starting point for getting started with Rx code: when you have data coming from outside,
    you can pass it to a Subject, thus turning it into an observable.*/
    /*BehaviorSubject - Subject that emits the most recent item it has observed and all subsequent observed items to each subscribed Observer.*/
    final Subject<Integer> walletPosition = BehaviorSubject.createDefault(0);  //defaultValue - the item that will be emitted first to any Observer as long as the BehaviorSubject has not yet observed any items from its source Observable

    private WalletsRepo walletsRepo;
    private CurrencyRepo currencyRepo;
    private final RxSchedulers schedulers;

    @Inject
    WalletsViewModel(WalletsRepo walletsRepo, CurrencyRepo currencyRepo, RxSchedulers schedulers) {
        this.walletsRepo = walletsRepo;
        this.currencyRepo = currencyRepo;
        this.schedulers = schedulers;

        /*Get wallets*/
        /*currency is a upstream, trigger*/
        wallets = currencyRepo.currency()
//                .doOnNext(currency -> Timber.d("wallets_VM1: %s", currency.code()))

                /*Delay IS NECESSARY as walletsRepo.wallets(currency) could take time, then CACHED VALUE will be emitted?*/
                /*As result list of wallets will not update with new chosen currency*/
                /*This is cache using specific?*/
                .delay(1, TimeUnit.SECONDS)   //TODO

                .switchMap(currency -> walletsRepo.wallets(currency))
//                .doOnNext(wallets -> Timber.d("wallets_VM2: %s", wallets.get(0).coin().currencyCode()))

                /*Kind of cache, as WalletFragment has two subscriptions to this stream. No need get currency() and wallets() twice->let's cached it*/
                /*All chain above will be cached. Replays at most 1 item(here wallets()) emitted by above ObservableSource*/
                .replay(1)
                .autoConnect();

        /*Get current wallet*/
        /*wallets and walletPosition are upstreams*/
        final Observable<Wallet> currentWallet = wallets
                .<Wallet>switchMap(wallets1 -> walletPosition.map(index -> wallets1.get(index)));

        /*Get transactions list*/
        transactions = currentWallet.switchMap(wallet -> walletsRepo.transactions(wallet))
                /*Kind of cache. F.e. if orientation changed -> ViewModel stays alive, so can easily restore UI*/
                /*All chain above will be cached. Replays at most 1 item(here wallets()) emitted by above ObservableSource*/
                .replay(1)
                .autoConnect();
    }

    Observable<List<Wallet>> wallets() {
        return wallets.observeOn(schedulers.main());
    }

    Observable<List<Transaction>> transactions() {
        return transactions.observeOn(schedulers.main());
    }

    Completable addWallet() {
        return Completable.fromAction(() -> Timber.d(""));
    }

    /*change upstream for currentWallet*/
    void changeWallet(int position) {
        walletPosition.onNext(position);
    }
}
