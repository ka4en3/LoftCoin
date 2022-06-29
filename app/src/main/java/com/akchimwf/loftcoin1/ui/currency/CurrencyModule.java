package com.akchimwf.loftcoin1.ui.currency;

import androidx.lifecycle.ViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.ClassKey;
import dagger.multibindings.IntoMap;

/*Similar to MainModule*/
@Module
abstract class CurrencyModule {
    @Binds
    @IntoMap
    @ClassKey(CurrencyViewModel.class)
    abstract ViewModel viewModel(CurrencyViewModel impl);
}
