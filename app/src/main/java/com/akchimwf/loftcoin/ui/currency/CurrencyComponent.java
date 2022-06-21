package com.akchimwf.loftcoin.ui.currency;

import androidx.lifecycle.ViewModelProvider;

import com.akchimwf.loftcoin.BaseComponent;
import com.akchimwf.loftcoin.util.ViewModelModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(
        modules = {
                CurrencyModule.class,
                ViewModelModule.class
        }, dependencies = {
        BaseComponent.class
}
)
public abstract class CurrencyComponent {
    /*to call this method as component.viewModelFactory()*/
    abstract ViewModelProvider.Factory viewModelFactory();
}
