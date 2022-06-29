package com.akchimwf.loftcoin1;

//TODO no internet handling

import android.app.Application;
import android.os.StrictMode;

import com.akchimwf.loftcoin1.util.DebugTree;
import com.google.firebase.messaging.FirebaseMessaging;

import timber.log.Timber;

public class LoftApp extends Application {

    /*Manually keep instance in App, so AppComponent and App have same lifecycles*/
    private BaseComponent component;

    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            StrictMode.enableDefaults();
            Timber.plant(new DebugTree()); //for Releases should use ReleaseTree() to send crash to some analytics system
        }

        /*creating AppComponent in App*/
        component = DaggerAppComponent.builder()
                .application(this)
                .build();

        /*get FCM token - to send messages from Firebase -> CloudMessaging (manually coping from logcat to CloudMessaging interface)*/
        //FirebaseMessaging.getInstance().getToken().addOnSuccessListener(token -> Timber.d("fcm: %s", token));
    }

    public BaseComponent getComponent() {
        return component;
    }
}
