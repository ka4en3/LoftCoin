package com.akchimwf.loftcoin.data;

import androidx.room.Database;
import androidx.room.RoomDatabase;

/*Marks a class as a RoomDatabase. The class should be an abstract class and extend RoomDatabase.*/
@Database(entities = {
        /*The list of entities included in the database. Each entity turns into a table in the database.*/
        RoomCoin.class
}, version = 1)
/*Base class for all Room databases. All classes that are annotated with Database must extend this class.
RoomDatabase provides direct access to the underlying database implementation but you should prefer using Dao classes.*/
public abstract class LoftDatabase extends RoomDatabase {
    abstract CoinsDao coins();
}
