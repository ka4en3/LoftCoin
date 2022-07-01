package com.akchimwf.loftcoin1.data;

import androidx.annotation.NonNull;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

public class TestCoinsRepo implements CoinsRepo {

    /*imitating of listings()*/
    /*Subject = one time shot*/
    public final Subject<List<Coin>> listings = PublishSubject.create();

    public Query lastListingsQuery;

    @NonNull
    @Override
    public Observable<List<Coin>> listings(@NonNull Query query) {
        lastListingsQuery = query;
        return listings;
    }

    @NonNull
    @Override
    public Single<Coin> coin(Currency currency, long id) {
        return Single.error(() -> new AssertionError("Stub!"));
    }

    @NonNull
    @Override
    public Observable<List<Coin>> topCoins(@NonNull Currency currency) {
        return Observable.error(() -> new AssertionError("Stub!"));
    }
}
