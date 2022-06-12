package com.akchimwf.loftcoin.ui.rates;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.akchimwf.loftcoin.data.CmcCoinsRepo;
import com.akchimwf.loftcoin.data.Coin;
import com.akchimwf.loftcoin.data.CoinsRepo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/*ViewModel is a class that is responsible for preparing and managing the data for an Activity or a Fragment.
It also handles the communication of the Activity / Fragment with the rest of the application (e.g. calling the business logic classes).*/
/*ViewModel's only responsibility is to manage the data for the UI.
It should never access your view hierarchy or hold a reference back to the Activity or the Fragment.*/
public class RatesViewModel extends ViewModel {

    /*get mutable data inside ViewModel*/
    private final MutableLiveData<List<Coin>> coins = new MutableLiveData<>();

    private final MutableLiveData<Boolean> isRefreshing = new MutableLiveData<>();

    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    private final CoinsRepo repo;

    /*A Future represents the result of an asynchronous computation.*/
    private Future<?> future;

    public RatesViewModel() {
        repo = new CmcCoinsRepo();
        refresh();
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

    /*in our case refresh() called only from main thread*/
    final void refresh() {
        /*postValue sends isRefreshing to the main thread as all UI interaction should be there*/
        /*Posts a task to a main thread to set the given value.*/
        isRefreshing.postValue(true);

        /*Submits a Runnable task for execution and returns a Future representing that task.
        The Future's get method will return null upon successful completion.*/
        /*use lambda for Runnable interface*/
        /*current executor makes request in a separate thread, but postValue sends to main thread*/
        future = executor.submit(() -> {
            try {
                /*because repo.listings returns List<? extends Coin> and we need List<Coin>*/
                /*copy result of repo.listings to new ArrayList, on separate thread. */
                final List<Coin> coins_from_repo = new ArrayList<>(repo.listings("USD"));
                /*postValue sends coins to the main thread as all UI interaction should be there*/
                /*Posts a task to a main thread to set the given value.*/
                this.coins.postValue(coins_from_repo);

                isRefreshing.postValue(false);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    /*Attempts to cancel execution of this task. if ViewModel was destroyed, f.e, user stops data acquisition  */
    @Override
    protected void onCleared() {
        if (future != null) {
            future.cancel(true);
        }
    }
}
