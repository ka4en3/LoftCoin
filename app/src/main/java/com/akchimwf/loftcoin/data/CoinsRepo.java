package com.akchimwf.loftcoin.data;

import androidx.annotation.NonNull;
import androidx.annotation.WorkerThread;
import androidx.lifecycle.LiveData;

import com.google.auto.value.AutoValue;

import java.io.IOException;
import java.util.List;

/*interface for Repository according to and Clean Architecture*/
public interface CoinsRepo {
    @NonNull
    @WorkerThread
        //non-main thread
        /*currency - set currency for a list of coins*/
    List<? extends Coin> listings(@NonNull String currency) throws IOException;

    @NonNull
    LiveData<List<Coin>> listings(@NonNull Query query);

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

        @AutoValue.Builder   //AutoValue allows to make Builder
        /*public as Builder must be called from outside package*/
        abstract static class Builder {
            /*Builder must contain all setters and method 'build()'*/
            abstract Builder currency(String currency);

            abstract Builder forceUpdate(Boolean forceUpdate);

            abstract Query build();
        }
    }
}
