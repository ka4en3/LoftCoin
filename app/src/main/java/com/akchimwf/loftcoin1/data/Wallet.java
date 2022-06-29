package com.akchimwf.loftcoin1.data;

import com.google.auto.value.AutoValue;

import java.util.Date;

@AutoValue
public abstract class Wallet {

    /*factory method (=static?) for creating Wallet class*/
    /*package private -> repo knows*/
    static Wallet create(String id, Coin coin, Double balance, Date date) {
        return new AutoValue_Wallet(id, coin, balance, date);
    }

    /*String uid as Firestore keeps id as a string hash*/
    public abstract String uid();

    public abstract Coin coin();

    public abstract double balance();

    public abstract Date createdAt();
}
