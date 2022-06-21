package com.akchimwf.loftcoin.ui.wallets;

import androidx.lifecycle.ViewModel;

import com.akchimwf.loftcoin.ui.converter.ConverterViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.ClassKey;
import dagger.multibindings.IntoMap;

/*Similar to MainModule*/
@Module
abstract class WalletsModule {
    @Binds
    @IntoMap
    @ClassKey(WalletsViewModel.class)
    abstract ViewModel viewModel(WalletsViewModel impl);
}
