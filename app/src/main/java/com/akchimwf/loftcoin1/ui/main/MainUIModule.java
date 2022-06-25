package com.akchimwf.loftcoin1.ui.main;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentFactory;

import com.akchimwf.loftcoin1.ui.converter.ConverterFragment;
import com.akchimwf.loftcoin1.ui.currency.CurrencyDialog;
import com.akchimwf.loftcoin1.ui.rates.RatesFragment;
import com.akchimwf.loftcoin1.ui.wallets.WalletsFragment;
import com.akchimwf.loftcoin1.util.CustomFragmentFactory;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.ClassKey;
import dagger.multibindings.IntoMap;

@Module
abstract class MainUIModule {
    /*binding FragmentFactory class to its custom realisation in dependency graph(=MainUIComponent)*/
    @Binds
    abstract FragmentFactory fragmentFactory(CustomFragmentFactory impl);

    /*creating a Map<Class<?>, Provider<Fragment>>
    with Fragment instances as a values, and their classnames as a keys. Use @IntoMap and @ClassKey.*/
    /*in other case should use qualifiers as Dagger will not know which Fragment to inject*/
    @Binds
    @IntoMap
    @ClassKey(RatesFragment.class)
    abstract Fragment ratesFragment(RatesFragment impl);

    @Binds
    @IntoMap
    @ClassKey(ConverterFragment.class)
    abstract Fragment converterFragment(ConverterFragment impl);

    @Binds
    @IntoMap
    @ClassKey(WalletsFragment.class)
    abstract Fragment walletsFragment(WalletsFragment impl);

    @Binds
    @IntoMap
    @ClassKey(CurrencyDialog.class)
    abstract Fragment currencyDialog(CurrencyDialog impl);
}
