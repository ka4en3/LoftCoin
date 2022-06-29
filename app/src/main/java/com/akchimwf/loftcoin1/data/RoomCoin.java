package com.akchimwf.loftcoin1.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.auto.value.AutoValue;

/*Marks a class as an entity. This class will have a mapping SQLite table in the database.
Each entity must have at least 1 field annotated with PrimaryKey. You can also use primaryKeys() attribute to define the primary key.*/
@Entity
/*@AutoValue supported by Room*/
@AutoValue
public abstract class RoomCoin implements Coin {

    /*fabric method for instantiating - Room needs it*/
    static RoomCoin create(
            String name,
            String symbol,
            int rank,
            double price,
            double change24h,
            String currencyCode,
            /*id - the last field, as it redefined as primary key*/
            int id) {
        return new AutoValue_RoomCoin(name, symbol, rank, price, change24h, currencyCode, id);
    }

    @Override
    /*if not set PrimaryKey, SQLite anyway will create this field in the table*/
    @PrimaryKey
    @AutoValue.CopyAnnotations
    public abstract int id();
}
