Loftschool "Android: Advanced" graduation project.
Stack: MVVP, Dagger, Room, RxJava, Coinmarketcap, Firebase.
Wallets activity: keep available wallets in Firebase. Adding a new wallet is not realized.
Rates activity: get crypto rates from Coinmarketcap in chosen currency.
Converter activity: convert crypto's rates.
Sequence of developing: Splash->Welcome->Main->Rates(Currency)->Wallets->Converter.
Coinmarketcap: Key hardcoded at Settings->Build,Execution,...->Compiler->CL options.
Firebase: access settings made in Firebase account, no need to get key in App.
FCM: only testing, not necessary for App functionality. Get FCM token - in LoftApp.java.
Examples of unit tests and UI tests.

TODO:
 - Suppress? album orientation
 - Multilang
 - Designing 
