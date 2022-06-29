package com.akchimwf.loftcoin1.widget;

import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

public class OnItemClick implements RecyclerView.OnItemTouchListener {

    private final View.OnClickListener listener;

    private long downTime;

    public OnItemClick(@NonNull View.OnClickListener listener) {
        this.listener = listener;
    }

    @Override
    public boolean onInterceptTouchEvent(@NonNull @NotNull RecyclerView rv, @NonNull @NotNull MotionEvent e) {
        if (isSingleTap(e)) {
            final View view = rv.findChildViewUnder(e.getX(), e.getY());
            if (view != null) {
                listener.onClick(view);
            }
        }
        return false;
    }

    private boolean isSingleTap(MotionEvent e) {
        if (e.getActionMasked() == MotionEvent.ACTION_DOWN) {
            downTime = SystemClock.uptimeMillis();
        } else if (e.getActionMasked() == MotionEvent.ACTION_UP) {
            return (ViewConfiguration.getTapTimeout() >= e.getEventTime() - downTime);
        }
        return false;
    }

    @Override
    public void onTouchEvent(@NonNull @NotNull RecyclerView rv, @NonNull @NotNull MotionEvent e) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }
}
