package com.akchimwf.loftcoin1.util;

import androidx.annotation.NonNull;

import com.akchimwf.loftcoin1.util.RxSchedulers;

import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;

public class TestRxSchedulers implements RxSchedulers {
    @NonNull
    @Override
    public Scheduler io() {
        /*a Scheduler that queues work on the current thread*/
        /*simplifying*/
        return Schedulers.trampoline();
    }

    @NonNull
    @Override
    public Scheduler cmp() {
        /*a Scheduler that queues work on the current thread*/
        /*simplifying*/
        return Schedulers.trampoline();
    }

    @NonNull
    @Override
    public Scheduler main() {
        /*a Scheduler that queues work on the current thread*/
        /*simplifying*/
        return Schedulers.trampoline();
    }
}
