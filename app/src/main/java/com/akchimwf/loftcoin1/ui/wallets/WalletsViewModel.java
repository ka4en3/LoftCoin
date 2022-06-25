package com.akchimwf.loftcoin1.ui.wallets;

import androidx.lifecycle.ViewModel;

import javax.inject.Inject;
import javax.inject.Singleton;

/*ViewModel is a class that is responsible for preparing and managing the data for an Activity or a Fragment.
It also handles the communication of the Activity / Fragment with the rest of the application (e.g. calling the business logic classes).*/
/*ViewModel's only responsibility is to manage the data for the UI.
It should never access your view hierarchy or hold a reference back to the Activity or the Fragment.*/
@Singleton
public class WalletsViewModel extends ViewModel {

    @Inject
    public WalletsViewModel() {

    }


}
