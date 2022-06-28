package com.akchimwf.loftcoin1.util;

import androidx.annotation.NonNull;

import io.reactivex.Completable;

public interface Notifier {
    /*The Completable class represents a deferred computation without any value but only indication for completion or exception.
    Completable behaves similarly to Observable except that it can only emit either a completion or error signal (there is no onNext or onSuccess as with the other reactive types)*/
    Completable sendMessage(@NonNull String title, @NonNull String message, @NonNull Class<?> receiver);
}
