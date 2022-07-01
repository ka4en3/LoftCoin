package com.akchimwf.loftcoin1.data;

import android.content.Context;

import androidx.annotation.NonNull;

/*Use CurrencyRepoImpl, not interface, as it's totally local dependent class. Current realization is OK for testing.*/
public class TestCurrencyRepo extends CurrencyRepoImpl {

    public TestCurrencyRepo(@NonNull Context context) {
        super(context);
    }
}
