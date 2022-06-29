package com.akchimwf.loftcoin1.ui.main;

import com.akchimwf.loftcoin1.BaseComponent;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {
        MainUIModule.class
}, dependencies = {
        /*use BaseComponent, not AppComponent as AppComponent is scoped*/
        /*to resolve all dependencies from AppComponent*/
        BaseComponent.class
})
public abstract class MainUIComponent {

    /*no constructor here as due to dependencies it will auto-generate Builder()*/

    /*should have this method to have possibility to inject to @Inject field in MainActivity*/
    /*name can vary*/
    abstract void injectToMainActivity(MainActivity activity);
}
