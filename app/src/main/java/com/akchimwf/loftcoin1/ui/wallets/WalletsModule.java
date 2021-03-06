package com.akchimwf.loftcoin1.ui.wallets;

import androidx.lifecycle.ViewModel;

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
    abstract ViewModel walletsViewModel(WalletsViewModel impl);
}
