package com.akchimwf.loftcoin;

import android.app.Application;
import android.content.Context;

import com.akchimwf.loftcoin.data.CoinsRepo;
import com.akchimwf.loftcoin.data.CurrencyRepo;
import com.akchimwf.loftcoin.data.DataModule;
import com.akchimwf.loftcoin.util.UtilModule;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;

/*Scope annotation, default from Dagger. */

/*In Dagger 2 scopes mechanism cares about keeping single instance of class as long as its scope exists.
In practice it means that instances scoped in @ApplicationScope lives as long as Application object.
@ActivityScope keeps references as long as Activity exists (for example we can share single instance of any class between all fragments hosted in this Activity).
In short - scopes give us “local singletons” which live as long as scope itself.*/

/*This annotation doesn't make AppCompat real singleton in our App. We should keep it instance manually(in LoftApp), as we declared it as singleton.*/

/*@Singleton needs when single instance has states -> need to keep it. It will be cached.*/
@Singleton
/*Main App graph, everything except UI*/
@Component(
        modules = {
                AppModule.class,
                DataModule.class,
                UtilModule.class
        }
)
/*not public, as BaseComponent used in LoftApp*/
abstract class AppComponent implements BaseComponent {

    @Component.Builder
    static abstract class Builder {

        @BindsInstance //bind Application as is
        abstract Builder application(Application app);

        abstract AppComponent build();
    }
}
