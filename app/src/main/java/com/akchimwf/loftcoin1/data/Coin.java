package com.akchimwf.loftcoin1.data;

public interface Coin {
    int id();

    String name();

    String symbol();

    int rank();

    double price();

    double change24h();

    /*excess field to keep currency for every coin*/
    String currencyCode();
}
