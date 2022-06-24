package com.akchimwf.loftcoin.util;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.util.Map;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;

@Singleton
/*Similar to CustomFragmentFactory*/
/*As ViewModelProvider.Factory returns null by default, and we need some constructor ->
use ViewModelProvider.NewInstanceFactory - Simple factory, which calls empty constructor on the give class.*/
public class CustomViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final Map<Class<?>, Provider<ViewModel>> providers;

    @Inject
    CustomViewModelFactory(Map<Class<?>, Provider<ViewModel>> providers) {
        this.providers = providers;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        /*get Provider of exact class of ViewModel(f.e. RatesViewModel) which creating*/
        final Provider<ViewModel> provider = providers.get(modelClass);
        if (provider != null) {
            /*Provides a fully-constructed and injected instance of T.*/
            return (T) provider.get();  // here we sure that <T> extends ViewModel (f.e. RatesViewModel)
        }
        /*by default*/
        return super.create(modelClass);
    }
}
