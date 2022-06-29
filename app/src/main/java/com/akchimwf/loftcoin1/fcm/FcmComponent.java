package com.akchimwf.loftcoin1.fcm;

import com.akchimwf.loftcoin1.BaseComponent;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {
        FcmModule.class
}, dependencies = {
        /*use BaseComponent, not AppComponent as AppComponent is scoped*/
        /*to resolve all dependencies from AppComponent*/
        BaseComponent.class
})
abstract class FcmComponent {
    /*no constructor here as due to dependencies it will auto-generate Builder()*/

    /*should have this method to have possibility to inject to @Inject field in Service(FcmService)*/
    /*name can vary*/
    abstract void injectToFcmService(FcmService service);
}
