package com.akchimwf.loftcoin;

import android.app.Application;
import android.content.Context;

import dagger.BindsInstance;
import dagger.Component;

/*Main App graph, everything except UI*/
@Component(
        modules = {AppModule.class}
)
public abstract class AppComponent {

    /*when AppComponent needs Context, it will be taken from AppModule*/
    abstract Context context();

    static abstract class Builder {
        @BindsInstance //bind Application as is
        abstract Builder application(Application app);

        abstract AppComponent build();
    }
}
