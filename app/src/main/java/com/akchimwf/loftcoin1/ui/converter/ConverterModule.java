package com.akchimwf.loftcoin1.ui.converter;

import androidx.lifecycle.ViewModel;

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
