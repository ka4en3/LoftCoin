package com.akchimwf.loftcoin1.ui.rates;

import androidx.lifecycle.ViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.ClassKey;
import dagger.multibindings.IntoMap;

/*Similar to MainModule*/
@Module
abstract class RatesModule {
    @Binds
    @IntoMap
    @ClassKey(RatesViewModel.class)
    abstract ViewModel viewModel(RatesViewModel impl);
}
