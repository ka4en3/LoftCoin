package com.akchimwf.loftcoin.data;

import androidx.annotation.NonNull;
import androidx.annotation.WorkerThread;

import java.io.IOException;
import java.util.List;

/*interface for Repository according to and Clean Architecture*/
public interface CoinsRepo {
    @NonNull
    @WorkerThread   //separate thread?
    /*currency - set currency for a list of coins*/
    List<? extends Coin> listings(@NonNull String currency) throws IOException;
}
