package com.akchimwf.loftcoin1.ui.rates;

import static org.junit.Assert.*;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.akchimwf.loftcoin1.data.Coin;
import com.akchimwf.loftcoin1.data.CoinsRepo;
import com.akchimwf.loftcoin1.data.FakeCoin;
import com.akchimwf.loftcoin1.data.TestCoinsRepo;
import com.akchimwf.loftcoin1.data.TestCurrencyRepo;
import com.akchimwf.loftcoin1.util.TestRxSchedulers;
import com.google.common.truth.Truth;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.annotation.Config;

import java.util.Arrays;
import java.util.List;

import io.reactivex.observers.TestObserver;

@RunWith(AndroidJUnit4.class)
public class RatesViewModelTest {

    TestCoinsRepo coinsRepo;
    TestCurrencyRepo currencyRepo;
    private RatesViewModel viewModel;

    private Context context;

    @Before
    public void setUp() throws Exception {
        context = ApplicationProvider.getApplicationContext();
        coinsRepo = new TestCoinsRepo();
        currencyRepo = new TestCurrencyRepo(context);
        viewModel = new RatesViewModel(coinsRepo, currencyRepo, new TestRxSchedulers());
    }

    /*Simulating viewModel.coin() stream*/
    @Config(manifest= Config.NONE)
    @Test
    public void coins() {
        /*RxJava -> creates a TestObserver and subscribes it to this Observable.*/
        final TestObserver<List<Coin>> coinsTest = viewModel.coins().test();
        /*ARxJava -> assert that this TestObserver received exactly one onNext value which is equal to the given value(true) with respect to Objects.equals.*/
        viewModel.isRefreshing().test().assertValue(true);
        /*creating list of FakeCoins (2 items)*/
        final List<Coin> fakeCoins = Arrays.asList(new FakeCoin(), new FakeCoin());
        /*push fakeCoins to CoinsRepo.listings to reflect listings method as in a stream*/
        coinsRepo.listings.onNext(fakeCoins);
        /*test if IsRefreshing hiding*/
        viewModel.isRefreshing().test().assertValue(false);
        /*check if it's really List<Coin> on the exit of the stream*/
        coinsTest.assertValue(fakeCoins);

        /*save query constructed by RatesViewModel*/
        CoinsRepo.Query query = coinsRepo.lastListingsQuery;
        /*check if query is not null*/
        Truth.assertThat(query).isNotNull();
        /*check if forceUpdate is true*/
        Truth.assertThat(query.forceUpdate()).isTrue();

        /*check sorting*/
        viewModel.switchSortingOrder();

        /*test if IsRefreshing hid*/
        viewModel.isRefreshing().test().assertValue(false);
        /*push fakeCoins to CoinsRepo.listings to reflect listings method as in a stream*/
        coinsRepo.listings.onNext(fakeCoins);
        /*test if IsRefreshing hid*/
        viewModel.isRefreshing().test().assertValue(false);

        /*save query constructed by RatesViewModel again*/
        query = coinsRepo.lastListingsQuery;
        /*check if query is not null*/
        Truth.assertThat(query).isNotNull();
        /*check if forceUpdate is false now*/
        Truth.assertThat(query.forceUpdate()).isFalse();

    }
}