package com.akchimwf.loftcoin1.ui.currency;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.akchimwf.loftcoin1.data.Currency;
import com.akchimwf.loftcoin1.data.CurrencyRepo;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

/*ViewModel is a class that is responsible for preparing and managing the data for an Activity or a Fragment.
It also handles the communication of the Activity / Fragment with the rest of the application (e.g. calling the business logic classes).*/
/*ViewModel's only responsibility is to manage the data for the UI.
It should never access your view hierarchy or hold a reference back to the Activity or the Fragment.*/
@Singleton
class CurrencyViewModel extends ViewModel {

    private final CurrencyRepo currencyRepo;

    @Inject
    CurrencyViewModel(CurrencyRepo currencyRepo) {
        this.currencyRepo = currencyRepo;
    }

    @NonNull
    LiveData<List<Currency>> availableCurrencies() {
        return currencyRepo.availableCurrencies();
    }

    void updateCurrency (@NonNull Currency currency) {
        currencyRepo.updateCurrency(currency);
    }
}
