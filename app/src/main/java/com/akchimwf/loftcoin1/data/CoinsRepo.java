package com.akchimwf.loftcoin1.data;

import androidx.annotation.NonNull;

import com.google.auto.value.AutoValue;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Single;

/*interface for Repository according to and Clean Architecture*/
public interface CoinsRepo {

    @NonNull
    Observable<List<Coin>> listings(@NonNull Query query);

    /*The Single class implements the Reactive Pattern for a single value response.
    Single behaves similarly to Observable except that it can only emit either a single successful value or an error
    (there is no "onComplete" notification as there is for an Observable)*/
    @NonNull
    Single<Coin> coin(Currency currency, long id);

    /*get top 3 coins for coins_sheet dialog*/
    @NonNull
    Observable<List<Coin>> topCoins(@NonNull Currency currency);

    /*Inner class contained Query for LiveData<List<Coin>> listings(), can easy modify without modify of method listings()*/
    @AutoValue
    /*Modifier 'static' is redundant for inner classes of interfaces*/
    abstract static class Query {

        /*method-fabric to return instance of Builder*/
        @NonNull
        public static Builder builder() {
            return new AutoValue_CoinsRepo_Query.Builder()
                    .forceUpdate(true)       //default value
                    .sortBy(SortBy.RANK);    //default value
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
