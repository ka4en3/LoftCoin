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
        /*in other cases return empty list*/
        return Collections.emptyList();
    }

    @NonNull
    @Override
    public LiveData<List<Coin>> listings(@NonNull Query query) {
        /*LiveData for Refresh Flag (update or not data from server)*/
        final MutableLiveData<Boolean> refresh = new MutableLiveData<>();
        /*Manage threads here as coinsCount() annotated with @WorkerThread*/
        executor.submit(() -> {
            /*Post a task to a main thread to set Refresh Flag. Refresh if forceUpdate() == true, or RoomCoin table has no records */
            refresh.postValue(query.forceUpdate() || db.coins().coinsCount() == 0);
        });

        /*Transformation method for LiveData.*/
        /*transformation of LiveData from Boolean to List<Coin>*/
        /*switchMap - Returns a LiveData mapped from the input source LiveData by applying switchMapFunction to each value set on source.*/
        /*switchMap returns new LiveData<List<Coin>>*/
        return Transformations.switchMap(refresh,
                new Function<Boolean, LiveData<List<Coin>>>() {
                    @Override
                    public LiveData<List<Coin>> apply(Boolean r) {
                        if (r) return fetchFromNetwork(query);
                        else return fetchFromDB(query);
                    }
                });
    }

    private LiveData<List<Coin>> fetchFromDB(Query query) {
        /*Transformation method for LiveData.*/
        /*transformation of LiveData from List<RoomCoin> to List<Coin>*/
        /*map - Returns a LiveData mapped from the input source LiveData by applying mapFunction to each value set on source.*/
        return Transformations.map(db.coins().fetchAll(), new Function<List<RoomCoin>, List<Coin>>() {
            @Override
            /*input parameter(coins) should be type of return fetchAll() function*/
            public List<Coin> apply(List<RoomCoin> coins) {
                /*List<RoomCoin> -> List<Coin>*/
                return new ArrayList<>(coins);
            }
        });
    }

    private LiveData<List<Coin>> fetchFromNetwork(Query query) {
        /*LiveData for coins*/
        final MutableLiveData<List<Coin>> liveData = new MutableLiveData<>();
        /*as request to network are asynchronous -> use own executor*/
        executor.submit(() -> {
            try {
                final Response<Listings> response = api.listings(query.currency()).execute();
                if (response.isSuccessful()) {
                    final Listings listings = response.body();
                    if (listings != null) {
                        final List<AutoValue_CmcCoin> cmcCoins = listings.data();

                        /*save data from network to DB*/
                        saveCoinsIntoDB(cmcCoins);

                        /*List<AutoValue_CmcCoin> -> List<Coin>*/
                        liveData.postValue(new ArrayList<>(cmcCoins));
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
        });
        return liveData;
    }

    /*on separate thread as this method called from asynchronous request*/
    private void saveCoinsIntoDB(List<? extends Coin> cmcCoins) {
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
                    coin.id()
                    ));
        }
        db.coins().insert(roomCoins);
    }
}
