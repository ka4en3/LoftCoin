package com.akchimwf.loftcoin.ui.rates;

import androidx.lifecycle.ViewModelProvider;

import com.akchimwf.loftcoin.BaseComponent;
import com.akchimwf.loftcoin.util.ViewModelModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(
        modules = {
                RatesModule.class,
                ViewModelModule.class
        }, dependencies = {
        BaseComponent.class
})

abstract class RatesComponent {
    /*to call this method as component.viewModelFactory()*/
    abstract ViewModelProvider.Factory viewModelFactory();

    /*to call this method as adapter = component.ratesAdapter();*/
    abstract RatesAdapter ratesAdapter();
}
