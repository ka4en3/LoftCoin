package com.akchimwf.loftcoin.ui.rates;

import androidx.annotation.NonNull;

import com.akchimwf.loftcoin.data.CmcCoin;

import java.util.List;

/*RatesPresenter will take this class as input to interact with*/
/*MVP pattern*/
public interface RatesView {

    void showCoins(@NonNull List<? extends CmcCoin> coins);

    void showError(@NonNull String error);
}
