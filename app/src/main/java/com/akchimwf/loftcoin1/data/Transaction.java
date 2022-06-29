package com.akchimwf.loftcoin1.data;

import com.google.auto.value.AutoValue;

import java.util.Date;

@AutoValue
public abstract class Transaction {

    /*factory method (=static?) for creating Transaction class*/
    /*package private -> repo knows*/
    static Transaction create(String uid, Coin coin, Double amount, Date createdAt) {
        return new AutoValue_Transaction(uid, coin, amount, createdAt);
    }

    /*String uid as Firestore keeps id as a string hash*/
    public abstract String uid();

    public abstract Coin coin();

    public abstract double amount();

    public abstract Date createdAt();
}
