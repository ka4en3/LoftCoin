package com.akchimwf.loftcoin1.data;

import androidx.annotation.NonNull;

import com.akchimwf.loftcoin1.util.RxSchedulers;

import java.util.ArrayList;
import java.util.List;


import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;

/*implementation of CoinsRepo interface according to CoinMarketCap coins list*/
/*this is Data Layer according to Clean Architecture*/
/*@Singleton as only one instance in AppComponent(=App) needed, and this instance has states*/
@Singleton
        /*Not public as use Dagger and DI*/
class CmcCoinsRepo implements CoinsRepo {

    private final CmcAPI api;
    private final LoftDatabase db;
    private RxSchedulers schedulers;

    @Inject
        /*Not public as use Dagger and DI*/
    CmcCoinsRepo(CmcAPI api, LoftDatabase db, RxSchedulers schedulers) {
        this.api = api;
        this.db = db;
        this.schedulers = schedulers;
    }

    @NonNull
    @Override
    public Observable<List<Coin>> listings(@NonNull Query query) {
        return Observable  //create Observable from callback
                .fromCallable(() -> query.forceUpdate() || db.coins().coinsCount() == 0)  //result: Observable<Boolean>
                .switchMap(f -> f ? api.listings(query.currency()) : Observable.empty())  //api.listings returns Observable, result: Observable<Listings>
                .map(listings -> mapToRoomCoins(query, listings.data()))                  //this code executed only if Observable is not empty, result: Observable<List<RoomCoin>>
                .doOnNext(coins -> db.coins().insert(coins))                              //doOnNext - side effect, not a Map(=not make any transformation). coins=List<RoomCoin>
                .switchMap(coins -> fetchFromDB(query))                                   //as previous step didn't do transformation, coins is the same. No result here as we only fetching from db
                .switchIfEmpty(fetchFromDB(query))                                        //this step executed only if step2 returns Observable.empty(), and all further steps will executed too
                .<List<Coin>>map(coins -> new ArrayList<>(coins))                         //result: Observable<List<Coin>>. As this is the last we should define return type to <List<Coin>>, only then Scheduler can be assigned
                /*Asynchronously subscribes Observers to this ObservableSource on the specified Scheduler*/
                .subscribeOn(schedulers.io())                                             //RxJava has predefined schedulers, this one will execute all chain on the IO thread, when subscription happened
                ;
    }

    private Observable<List<RoomCoin>> fetchFromDB(Query query) {
        //fetching from db according to query.sortBy()
        if (query.sortBy() == SortBy.PRICE) {
            return db.coins().fetchAllSortByPrice();
        } else {
            return db.coins().fetchAllSortByRank();
        }

    }

    private List<RoomCoin> mapToRoomCoins(Query query, List<? extends Coin> data) {
        /*can't set List<RoomCoin> roomCoins = new ArrayList<>(cmcCoins) directly*/
        List<RoomCoin> roomCoins = new ArrayList<>(data.size());
        /*manually creating List<RoomCoin>*/
        for (Coin coin : data) {
            roomCoins.add(RoomCoin.create(
                    coin.name(),
                    coin.symbol(),
                    coin.rank(),
                    coin.price(),
                    coin.change24h(),
                    query.currency(),  //get currency directly from query, some excess data for save in DB, but easier to handle further with currency viewing
                    coin.id()
            ));
        }
        return roomCoins;
    }
}
