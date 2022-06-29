package com.akchimwf.loftcoin1.util;

import androidx.lifecycle.ViewModelProvider;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class ViewModelModule {
    @Binds
    abstract ViewModelProvider.Factory viewModelFactory(CustomViewModelFactory impl);
}
