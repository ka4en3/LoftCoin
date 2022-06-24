package com.akchimwf.loftcoin.ui.converter;

import androidx.lifecycle.ViewModel;

import com.akchimwf.loftcoin.ui.rates.RatesViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.ClassKey;
import dagger.multibindings.IntoMap;

/*Similar to MainModule*/
@Module
abstract class ConverterModule {
    @Binds
    @IntoMap
    @ClassKey(ConverterViewModel.class)
    abstract ViewModel viewModel(ConverterViewModel impl);
}
