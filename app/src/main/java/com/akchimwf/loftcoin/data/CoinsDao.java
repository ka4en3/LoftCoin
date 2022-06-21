package com.akchimwf.loftcoin.data;

import androidx.annotation.WorkerThread;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

/*Marks the class as a Data Access Object.
Data Access Objects are the main classes where you define your database interactions. They can include a variety of query methods.
The class marked with @Dao should either be an interface or an abstract class.
At compile time, Room will generate an implementation of this class when it is referenced by a Database.*/
@Dao
abstract class CoinsDao {

    /*Marks a method in a Dao annotated class as a query method.
    The value of the annotation includes the query that will be run when this method is called.
    This query is verified at compile time by Room to ensure that it compiles fine against the database.*/
    @Query("select * from RoomCoin")
    /*Room could provide LiveData and change it according to DB changing*/
    abstract LiveData<List<RoomCoin>> fetchAll();

    /*Blocking UI method -> @WorkerThread: we annotate a method or class with this to indicate it MUST be executed on a different thread than Main/UI Thread*/
    @WorkerThread
    @Query("select count(id) from RoomCoin")
    abstract int coinsCount();

    /*Marks a method in a Dao annotated class as an insert method.
    The implementation of the method will insert its parameters into the database.
    All of the parameters of the Insert method must either be classes annotated with Entity or collections/array of it.*/
    @Insert(onConflict = OnConflictStrategy.REPLACE)   //What to do if a Primary key conflict happens
    abstract void insert(List<RoomCoin> coins);
}
