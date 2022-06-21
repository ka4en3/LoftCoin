package com.akchimwf.loftcoin;

import android.app.Application;
import android.content.Context;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public abstract class AppModule {
    /*Scope annotation, default from Dagger. */
    /*We want AppModule @Provides method have to be @Singleton too -> Context dependency will be singleton and has lifecycle same with AppComponent.*/
    /*@Singleton annotation means app.getApplicationContext() will not call everytime, only once*/
    @Singleton   //@Modules can't be scoped, only @Provides methods inside
    @Provides
    static Context context(Application app) {
        return app.getApplicationContext();
    }

    @Singleton
    @Provides
    static ExecutorService ioExecutor() {
        /*classic formula to get pool size (number of threads that possible to run on current device)*/
        int poolSize = Runtime.getRuntime().availableProcessors() * 2 + 1;
        return Executors.newFixedThreadPool(poolSize);
    }
}
