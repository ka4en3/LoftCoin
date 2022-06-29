package com.akchimwf.loftcoin1.data;

import android.content.Context;

import androidx.room.Room;

import com.akchimwf.loftcoin1.BuildConfig;
import com.squareup.moshi.Moshi;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.moshi.MoshiConverterFactory;

@Module
public abstract class DataModule {
    @Provides
    /*creating Moshi instance*/
    static Moshi moshi() {
        /*creating instance of Moshi to use its all custom factories later*/
        final Moshi moshi = new Moshi.Builder().build();
        /*set Moshi for parsing JSON*/
        /*Moshi dynamically creates adapters for different classes. But not for abstract classes -> need to re-config Moshi*/
        /*Returns a new builder containing all custom factories used by the current instance.*/
        return moshi.newBuilder()
                /*as CmcCoin and Listings class are abstract, Moshi needs exact implementation of its -> add correct adapters*/
                /*now Moshi can instantiate CmcCoin and Listings classes with set adapters*/
                .add(CmcCoin.class, moshi.adapter(AutoValue_CmcCoin.class))
                .add(Listings.class, moshi.adapter(AutoValue_Listings.class))
                .build();
    }

    /*@Singleton is not necessary as we don't need to keep it instance, creating once only (Retrofit has no states)*/
    @Provides
    /*creating Retrofit setting with OkHttpClient*/
    static Retrofit createRetrofit(OkHttpClient httpClient, Moshi moshi) {
        final Retrofit.Builder builder = new Retrofit.Builder();
        /*modify httpClient with newBuilder, add API_KEY to header, only for Retrofit*/
        builder.client(httpClient.newBuilder()
                /*add Interceptor interface, implementing its method "public Response intercept(@NonNull Chain chain)" with a lambda*/
                .addInterceptor(chain -> {
                    /*get Request*/
                    final Request request = chain.request();

                    /*modify Request with API_KEY*/
                    okhttp3.Response resp = chain.proceed(request.newBuilder()
                            .addHeader(CmcAPI.API_KEY, BuildConfig.API_KEY)
                            .build()
                    );
                    return resp;
                })
                .build()
        );

        builder.baseUrl(BuildConfig.API_ENDPOINT);
        builder.addConverterFactory(MoshiConverterFactory.create(moshi));

        /*this adapter allows to convert Call (from CmcAPI) to Observable*/
        builder.addCallAdapterFactory(RxJava2CallAdapterFactory.createAsync());

        /*return instance of Retrofit*/
        return builder.build();
    }

    @Provides
    /*creating CmcAPI instance*/
    static CmcAPI cmcAPI(Retrofit retrofit){
        /*create instance of CmcAPI*/
        /*Retrofit.create -> Single-interface proxy creation guarded by parameter safety.*/
        /*Create an implementation of the API endpoints defined by the service(CmcAPI) interface.*/
        return retrofit.create(CmcAPI.class);
    }

    @Provides
    @Singleton
    static LoftDatabase loftDatabase(Context context){
        if (BuildConfig.DEBUG) {
            /*Creates a RoomDatabase.Builder for an in memory database.
            Information stored in an in memory database disappears when the process is killed.
            Once a database is built, you should keep a reference to it and re-use it.*/

            return Room.databaseBuilder(context, LoftDatabase.class, "loftcoin.db").build();
            //return Room.inMemoryDatabaseBuilder(context, LoftDatabase.class).build();

        }else{
            /*Creates a RoomDatabase.Builder for a persistent database.
            Once a database is built, you should keep a reference to it and re-use it.*/
            return Room.databaseBuilder(context, LoftDatabase.class, "loftcoin.db").build();
        }
    }

    /*binding CoinsRepo interface to its CmcCoinsRepo realisation*/
    /*use @Binds as CmcCoinsRepo instance fully controlled as Object, and possible to @Inject to its constructor*/
    @Binds
    /*@Singleton annotation made in CmcCoinsRepo class. Means as coinsRepo is a @Singleton ->
    CmcAPI(), Retrofit(), Moshi() will be called only once as well -> no need to make them @Singletons */
    abstract CoinsRepo coinsRepo(CmcCoinsRepo impl);

    @Binds
    abstract CurrencyRepo currencyRepo(CurrencyRepoImpl impl);

    @Binds
    abstract WalletsRepo walletsRepo(WalletsRepoImpl impl);
}
