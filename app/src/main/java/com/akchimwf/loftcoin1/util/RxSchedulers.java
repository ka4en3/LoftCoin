package com.akchimwf.loftcoin1.util;

import androidx.annotation.NonNull;

import io.reactivex.Scheduler;

/*Having own class of RxSchedulers possible easily to replace schedulers in projects, and make tests*/
public interface RxSchedulers {
    @NonNull
    Scheduler io();

    @NonNull
    Scheduler cmp();

    @NonNull
    Scheduler main();
}
