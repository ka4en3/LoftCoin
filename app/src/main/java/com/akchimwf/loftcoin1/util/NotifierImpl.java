package com.akchimwf.loftcoin1.util;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.akchimwf.loftcoin1.R;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Completable;

@Singleton
class NotifierImpl implements Notifier {

    private Context context;

    private RxSchedulers schedulers;

    NotificationManager ntf;

    @Inject
    NotifierImpl(Context context, RxSchedulers schedulers) {
        this.context = context;
        this.schedulers = schedulers;
        /*NOTIFICATION_SERVICE - for informing the user of background events.*/
        this.ntf = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    @NonNull
    @Override
    public Completable sendMessage(@NonNull String title, @NonNull String message, @NonNull Class<?> receiver) {
        return Completable.fromAction(() -> {
                    final String channelId = context.getString(R.string.default_channel_id);
                    /*Helper for accessing features in Notification.*/
                    final Notification notification = new NotificationCompat.Builder(context, channelId)
                            .setSmallIcon(R.mipmap.ic_launcher_round)
                            .setContentTitle(title)
                            .setContentText(message)
                            .setAutoCancel(true)        //Setting this flag will make it so the notification is automatically canceled when the user clicks it in the panel.
                            /*Retrieve a PendingIntent that will start a new activity, like calling Context.startActivity(Intent).*/
                            .setContentIntent(PendingIntent.getActivity(
                                    context,
                                    0,        //?
                                    /* This provides a convenient way to create an intent that is intended to execute a hard-coded class name, rather than relying on the system to find an appropriate class for you*/
                                    new Intent(context, receiver).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),   //the activity will not be launched if it is already running at the top of the history stack
                                    PendingIntent.FLAG_ONE_SHOT,   //Flag indicating that this PendingIntent can be used only once.
                                    Bundle.EMPTY
                            ))
                            .build();//return a new Notification object.

                    ntf.notify(0, notification);     //Post a notification to be shown in the status bar.
                })
                /*Returns a Completable which first runs the other Completable then this completable if the other completed normally.*/
                .startWith(createDefaultChannel())   //In RxJava possible to start any stream with startWith()
                .subscribeOn(schedulers.main());
    }

    /*create notification channel*/
    /*if SDK ver < 26 Completable will just completed without any actions*/
    private Completable createDefaultChannel() {
        /*Returns a Completable instance that runs the given Action for each subscriber and emits either an unchecked exception or simply completes*/
        return Completable.fromAction(() -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                ntf.createNotificationChannel(new NotificationChannel(
                        context.getString(R.string.default_channel_id),
                        context.getString(R.string.default_channel_name),
                        NotificationManager.IMPORTANCE_LOW
                ));
            }
        });
    }
}
