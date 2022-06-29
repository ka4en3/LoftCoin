package com.akchimwf.loftcoin1.widget;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import io.reactivex.Observable;
import io.reactivex.android.MainThreadDisposable;

public class RxRecycleView {

    /*Get snapped view in RV, easily with SnapHelper*/
    /*no checking is emitter alive, as this stream fully on the main thread (all stream presented here)*/
    @NonNull
    public static Observable<Integer> onSnap(@NonNull RecyclerView rv, @NonNull SnapHelper helper) {
        return Observable.create(emitter -> {

            /*Verify that the calling thread is the Android main thread.*/
            MainThreadDisposable.verifyMainThread();

            final RecyclerView.OnScrollListener listener = new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        final View snapView = helper.findSnapView(rv.getLayoutManager());
                        if (snapView != null) {
                            final RecyclerView.ViewHolder holder = rv.findContainingViewHolder(snapView);
                            if (holder != null) {
                                emitter.onNext(holder.getAdapterPosition());
                            }
                        }
                    }
                }
            };
            /*unregister listener if no subscribers anymore*/
            emitter.setCancellable(() -> rv.removeOnScrollListener(listener));

            /*finally set the listener to RV*/
            rv.addOnScrollListener(listener);
        });
    }

    /*Get adapter position onClick*/
    /*no checking is emitter alive, as this stream fully on the main thread (all stream presented here)*/
    @NonNull
    public static Observable<Integer> onClick(@NonNull RecyclerView rv) {
        return Observable.create((emitter) -> {
            MainThreadDisposable.verifyMainThread();
            final RecyclerView.OnItemTouchListener listener = new OnItemClick((v) -> {
                final RecyclerView.ViewHolder holder = rv.findContainingViewHolder(v);
                if (holder != null) {
                    emitter.onNext(holder.getAdapterPosition());
                }
            });
            emitter.setCancellable(() -> rv.removeOnItemTouchListener(listener));
            rv.addOnItemTouchListener(listener);
        });
    }

}
