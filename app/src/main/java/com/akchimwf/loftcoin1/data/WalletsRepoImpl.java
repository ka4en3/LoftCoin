package com.akchimwf.loftcoin1.data;

import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.Single;
import timber.log.Timber;

@Singleton
public class WalletsRepoImpl implements WalletsRepo {

    private final FirebaseFirestore firestore;
    private CoinsRepo coinsRepo;

    @Inject
    public WalletsRepoImpl(CoinsRepo coinsRepo) {
        this.coinsRepo = coinsRepo;
        this.firestore = FirebaseFirestore.getInstance();
    }

    /*get Wallets with amount in chosen currency*/
    @RequiresApi(api = Build.VERSION_CODES.N)
    @NonNull
    @Override
    public Observable<List<Wallet>> wallets(@NonNull Currency currency) {

        /*produce Observable from listening of Firestore*/
        /*creating infinite source(Observable), as no onComplete method here -> stream exists until at least one Observer subscribed */
        /*Observable.<T>create() - Provides an API (via a cold Observable) that bridges the reactive world with the callback-style world(EventListener in our case).*/
        /*Observable.<T>create() is a TYPED method, so better to provide type explicit - QuerySnapshot. Because onEvent returns QuerySnapshot*/
        /*returns Observable<QuerySnapshot>. A QuerySnapshot contains the results of a query. QuerySnapshot is a trigger to start all stream.*/
        final Observable<List<Wallet>> observableListWallets = Observable.
                <QuerySnapshot>create(emitter -> {  //TODO make Firebase string constants
            /*Starts listening to this query.*/
            final ListenerRegistration registration = firestore
                    .collection("wallets")
                    .orderBy("balance", Query.Direction.DESCENDING)
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot snapshots, @Nullable FirebaseFirestoreException e) {
                            if (emitter.isDisposed())
                                return;   //if emitter not actual anymore -> only return from method
                            if (snapshots != null)
                                emitter.onNext(snapshots);
                            else if (e != null)
                                emitter.tryOnError(e);
                        }
                    });
            /*unregister listener*/
            emitter.setCancellable(registration::remove);
        })

                /*next downstream for created Observable*/
                .map(snapshots -> snapshots.getDocuments())   //return Observable<List<DocumentSnapshot>>
                .switchMapSingle(documentSnapshots -> {       //switchMapSingle because toList() returns Single<List<Wallet>>
                    final Single<List<Wallet>> listSingle = Observable
                            /*Converts an Iterable sequence into an ObservableSource that emits the items in the sequence.*/
                            /*as we need to map every single document(=wallet) to its coin from CoinsRepo -> this is the way to get single document*/
                            /*creating finite source (Observable), use fromIterable -> iterables number is finite*/
                            .fromIterable(documentSnapshots)
                            /*get Coin for given Currency and coinId*/
                            .flatMapSingle(documentSnapshot -> coinsRepo   //not switchMapSingle, as switchMapSingle emitting the item emitted by the MOST RECENTLY emitted of these SingleSources.
                                    .coin(currency, Objects.requireNonNull(documentSnapshot.getLong("coinId"), "coinId error!"))  //switchMapSingle as returns Single<Coin>
                                    /*transform document and coin to wallet*/
                                    .map(coin -> Wallet.create(
                                            documentSnapshot.getId(),
                                            coin,
                                            documentSnapshot.getDouble("balance"),
                                            documentSnapshot.getDate("created_at")
                                    ))
                            )
                            /*Returns a Single that emits a single item, a list composed of all the items emitted by the finite source ObservableSource.*/
                            .toList()

                            /*sort this list onSuccess, because flatMapSingle flattening without any order
                            and do not match the order from Firestore */
                            .doOnSuccess(wallets -> {
                                wallets.sort(new Comparator<Wallet>() {
                                    @Override
                                    public int compare(Wallet w1, Wallet w2) {
                                        return w2.createdAt().compareTo(w1.createdAt());
                                    }
                                });
//                                Timber.d("List<Wallet> acquired from Firestore, size: %s", wallets.size());
                            })

                            .doOnError(throwable -> {
                                Timber.d("%s", throwable.getLocalizedMessage());
                            });
                    return listSingle;
                });

        return observableListWallets;

    }

    @NonNull
    @Override
    public Observable<List<Transaction>> transactions(@NonNull Wallet wallet) {

        /*produce Observable from listening of Firestore*/
        /*creating infinite source(Observable), as no onComplete method here -> stream exists until at least one Observer subscribed */
        /*Observable.<T>create() - Provides an API (via a cold Observable) that bridges the reactive world with the callback-style world(EventListener in our case).*/
        /*Observable.<T>create() is a TYPED method, so better to provide type explicit - QuerySnapshot. Because onEvent returns QuerySnapshot*/
        /*returns Observable<QuerySnapshot>. A QuerySnapshot contains the results of a query. QuerySnapshot is a trigger to start all stream.*/
        return Observable.
                <QuerySnapshot>create(emitter -> {  //TODO make Firebase string constants
            /*Starts listening to this query.*/
            final ListenerRegistration registration = firestore
                    .collection("wallets")
                    .document(wallet.uid())
                    .collection("transactions")
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot snapshots, @Nullable FirebaseFirestoreException e) {
                            if (emitter.isDisposed())
                                return;   //if emitter not actual anymore -> only return from method
                            if (snapshots != null && !snapshots.isEmpty())
                                emitter.onNext(snapshots);
                            else if (e != null)
                                emitter.tryOnError(e);
                        }
                    });
            /*unregister listener if no subscribers anymore*/
            emitter.setCancellable(registration::remove);
        })

                /*next downstream for created Observable*/
                .map(snapshots -> snapshots.getDocuments())   //return Observable<List<DocumentSnapshot>>
                .switchMapSingle(documentSnapshots -> {       //switchMapSingle because toList() returns Single<List<Transaction>>
                    final Single<List<Transaction>> listSingle = Observable
                            /*Converts an Iterable sequence into an ObservableSource that emits the items in the sequence.*/
                            /*this is the way to get single document*/
                            /*creating finite source (Observable), use fromIterable -> iterables number is finite*/
                            .fromIterable(documentSnapshots)
                            /*transform document and coin to wallet*/
                            .map(documentSnapshot -> Transaction.create(
                                    documentSnapshot.getId(),
                                    wallet.coin(),
                                    documentSnapshot.getDouble("amount"),
                                    documentSnapshot.getDate("created_at")
                            ))
                            /*Returns a Single that emits a single item, a list composed of all the items emitted by the finite source ObservableSource.*/
                            .toList();
                    return listSingle;
                });

    }

}
