package com.akchimwf.loftcoin.data;

import androidx.annotation.NonNull;
import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;

import javax.inject.Inject;
import javax.inject.Singleton;

import okhttp3.ResponseBody;
import retrofit2.Response;
import timber.log.Timber;

/*implementation of CoinsRepo interface according to CoinMarketCap coins list*/
/*this is Data Layer according to Clean Architecture*/
/*@Singleton as only one instance in AppComponent(=App) needed, and this instance has states*/
@Singleton
        /*Not public as use Dagger and DI*/
class CmcCoinsRepo implements CoinsRepo {

    private final CmcAPI api;
    private final LoftDatabase db;
    private final ExecutorService executor;  // CmcCoinsRepo can manage threads

    @Inject
        /*Not public as use Dagger and DI*/
    CmcCoinsRepo(CmcAPI api, LoftDatabase db, ExecutorService executor) {
        this.api = api;
        this.db = db;
        this.executor = executor;
    }

/*  //old method realization
    @NonNull
    @Override
    public List<? extends Coin> listings(@NonNull String currency) throws IOException {
        final Response<Listings> response = api.listings(currency).execute();

        if (response.isSuccessful()) {
            final Listings listings = response.body();
            if (listings != null) {
                return listings.data();
            }
        } else {
            final ResponseBody responseBody = response.errorBody();
            if (responseBody != null) {
                throw new IOException(responseBody.string());
            }
        }
        //in other cases return empty list
        return Collections.emptyList();
    }*/

    @NonNull
    @Override
    public LiveData<List<Coin>> listings(@NonNull Query query) {
        fetchFromNetworkIfNecessary(query);
        return fetchFromDB(query);
    }

    private LiveData<List<Coin>> fetchFromDB(Query query) {
        /*LiveData for RoomCoins*/
        LiveData<List<RoomCoin>> coins;

        /*fetching from db according to query.sortBy()*/
        if (query.sortBy() == SortBy.PRICE) {
            coins = db.coins().fetchAllSortByPrice();
        } else {
            coins = db.coins().fetchAllSortByRank();
        }

        /*Transformation method for LiveData.*/
        /*transformation of LiveData from List<RoomCoin> to List<Coin>*/
        /*map - Returns a LiveData mapped from the input source LiveData by applying mapFunction to each value set on source.*/
        return Transformations.map(coins, new Function<List<RoomCoin>, List<Coin>>() {
            @Override
            /*input parameter(coins) should have type of fetchAll() function return*/
            public List<Coin> apply(List<RoomCoin> coins) {
                /*trick to transform List<RoomCoin> to List<Coin>*/
                return new ArrayList<>(coins);
            }
        });
    }

    /*method if necessary get data from server and save to db*/
    private void fetchFromNetworkIfNecessary(Query query) {
        /*as request to network are asynchronous -> use own executor*/
        /*Manage threads here as coinsCount() annotated with @WorkerThread*/
        executor.submit(() -> {
            /*if forceUpdate() == true, or RoomCoin table has no records */
            if (query.forceUpdate() || db.coins().coinsCount() == 0) {
                try {
                    final Response<Listings> response = api.listings(query.currency()).execute();
                    if (response.isSuccessful()) {
                        final Listings listings = response.body();
                        if (listings != null) {
                            /*save data from network to DB*/
                            /*on separate thread as this method called from asynchronous request*/
                            saveCoinsIntoDB(query, listings.data());
                        }
                    } else {
                        final ResponseBody responseBody = response.errorBody();
                        if (responseBody != null) {
                            throw new IOException(responseBody.string());
                        }
                    }
                } catch (IOException e) {
                    Timber.e(e);
                }
            }
        });
    }

    /*on separate thread as this method called from asynchronous request*/
    private void saveCoinsIntoDB(Query query, List<? extends Coin> cmcCoins) {
        /*can't set List<RoomCoin> roomCoins = new ArrayList<>(cmcCoins) directly*/
        List<RoomCoin> roomCoins = new ArrayList<>(cmcCoins.size());
        /*manually creating List<RoomCoin>*/
        for (Coin coin : cmcCoins) {
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
        db.coins().insert(roomCoins);
    }
}
