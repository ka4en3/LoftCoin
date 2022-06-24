package com.akchimwf.loftcoin.ui.currency;

import androidx.lifecycle.ViewModel;

import com.akchimwf.loftcoin.ui.converter.ConverterViewModel;

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
