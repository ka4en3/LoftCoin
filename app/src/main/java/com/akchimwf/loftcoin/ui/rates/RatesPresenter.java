package com.akchimwf.loftcoin.ui.rates;

import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;

import com.akchimwf.loftcoin.data.CmcCoinsRepo;
import com.akchimwf.loftcoin.data.Coin;
import com.akchimwf.loftcoin.data.CoinsRepo;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/*Presenter of MVP pattern*/
class RatesPresenter {
    /*Use the provided Looper instead of the default one.*/
    /*getMainLooper() - Returns the application's main looper, which lives in the main thread of the application.*/
    private final Handler handler = new Handler(Looper.getMainLooper());

    private final ExecutorService executor;

    private final CoinsRepo repo;

    private List<? extends Coin> coins = Collections.emptyList();

    private RatesView view;

    RatesPresenter() {
        /*to establish requests in a single thread*/
        this.executor = Executors.newSingleThreadExecutor();
        /*create instance of CmcCoinsRepo*/
        this.repo = new CmcCoinsRepo();
        refresh();
    }

    void attach(@NonNull RatesView view) {
        /*storing view of RatesFragment*/
        this.view = view;

        if (!coins.isEmpty()) {
            view.showCoins(coins);
        }
    }

    void detach(@NonNull RatesView view) {

    }

    private void onSuccess(List<? extends Coin> coins) {
        this.coins = coins;
        /*first run view=null, as RatesPresenter.refresh() called in onCreate method of Fragment*/
        if (view != null) {
            view.showCoins(coins);
        }
    }

    private void onError(IOException e) {
    }

    public void refresh() {
        /*Submits a Runnable task for execution and returns a Future representing that task.
        The Future's get method will return null upon successful completion.*/
        /*use lambda for Runnable interface*/
        /*current executor makes request in a separate thread*/
        executor.submit(() -> {
            try {
                final List<? extends Coin> coins = repo.listings("USD");
                /*send coins to main thread (in method onSuccess), as all UI interaction should be there*/
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        RatesPresenter.this.onSuccess(coins);
                    }
                });
            } catch (IOException e) {
                /*send IOException to main thread (in method onError), as all UI interaction should be there*/
                /*same procedure, but using lambda*/
                handler.post(() -> onError(e));
            }

        });

    }

}
