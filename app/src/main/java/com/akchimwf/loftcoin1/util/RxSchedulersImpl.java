package com.akchimwf.loftcoin1.util;

import androidx.annotation.NonNull;

import java.util.concurrent.ExecutorService;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/*Having own class of RxSchedulers possible easily to replace schedulers in projects, and make tests*/
@Singleton
class RxSchedulersImpl implements RxSchedulers {

    private final Scheduler ioScheduler;

    @Inject
    public RxSchedulersImpl(ExecutorService executor) {
        /*now own ioScheduler use own thread pool from own executor*/
        ioScheduler = Schedulers.from(executor);  //create once in constructor
    }

    @NonNull
    @Override
    public Scheduler io() {
        return ioScheduler;  //return singleton ioScheduler
    }

    @NonNull
    @Override
    public Scheduler cmp() {
        return Schedulers.computation();  //return singleton Schedulers.computation()
    }

    @NonNull
    @Override
    public Scheduler main() {
        return AndroidSchedulers.mainThread();  //return singleton AndroidSchedulers.mainThread()
    }
}
