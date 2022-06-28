package com.akchimwf.loftcoin1.util;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class UtilModule {
    @Binds
    abstract ImageLoader imageLoader(PicassoImageLoader impl);

    @Binds
    abstract RxSchedulers schedulers(RxSchedulersImpl impl);

    @Binds
    abstract Notifier notifier(NotifierImpl impl);
}
