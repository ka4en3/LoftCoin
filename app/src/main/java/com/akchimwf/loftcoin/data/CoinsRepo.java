package com.akchimwf.loftcoin.data;

import androidx.annotation.NonNull;
import androidx.annotation.WorkerThread;
import androidx.lifecycle.LiveData;

import com.google.auto.value.AutoValue;

import java.io.IOException;
import java.util.List;

import io.reactivex.Observable;

/*interface for Repository according to and Clean Architecture*/
public interface CoinsRepo {
    @NonNull
    Observable<List<Coin>> listings(@NonNull Query query);

    /*Inner class contained Query for LiveData<List<Coin>> listings(), can easy modify without modify of method listings()*/
    @AutoValue
    /*Modifier 'static' is redundant for inner classes of interfaces*/
    abstract static class Query {

        /*method-fabric to return instance of Builder*/
        @NonNull
        public static Builder builder() {
            return new AutoValue_CoinsRepo_Query.Builder()
                    .forceUpdate(true); //default value
        }

        abstract String currency();

        abstract Boolean forceUpdate();   //reload data from server or not

        abstract SortBy sortBy();

        @AutoValue.Builder   //AutoValue allows to make Builder
        /*public as Builder must be called from outside package*/
        public abstract static class Builder {
            /*Builder must contain all setters and method 'build()'*/
            public abstract Builder currency(String currency);

            public abstract Builder forceUpdate(Boolean forceUpdate);

            public abstract Builder sortBy(SortBy sortBy);

            public abstract Query build();
        }
    }
}
