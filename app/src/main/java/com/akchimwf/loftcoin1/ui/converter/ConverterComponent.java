package com.akchimwf.loftcoin1.ui.converter;

import androidx.lifecycle.ViewModelProvider;

import com.akchimwf.loftcoin1.BaseComponent;
import com.akchimwf.loftcoin1.util.ViewModelModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(
        modules = {
                ConverterModule.class,
                ViewModelModule.class
        }, dependencies = {
        BaseComponent.class
}
)
public abstract class ConverterComponent {
    /*to call this method as component.viewModelFactory()*/
    abstract ViewModelProvider.Factory viewModelFactory();
}
