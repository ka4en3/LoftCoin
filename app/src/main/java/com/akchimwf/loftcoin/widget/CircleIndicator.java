package com.akchimwf.loftcoin.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.DisplayMetrics;
import android.util.TypedValue;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;

public class CircleIndicator extends RecyclerView.ItemDecoration {
    /*creating Paint in constructor as Paint is a  heavy object*/
    private final Paint activePaint = new Paint();   //Paint for active Page indicator
    private final Paint inactivePaint = new Paint();

    private final float indicatorRadius;

    public CircleIndicator(Context context) {
        /*A structure describing general information about a display, such as its size, density, and font scaling.*/
        final DisplayMetrics dm = context.getResources().getDisplayMetrics();
        /*get the float value for indicator radius according to Device Independent Pixels and current Display Metrics */
        /*value - radius in dip we want to have*/
        indicatorRadius = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, dm);

        inactivePaint.setStyle(Paint.Style.FILL);
        inactivePaint.setColor(0x44ffffff);  //white with alfa
        inactivePaint.setAntiAlias(true);

        activePaint.setStyle(Paint.Style.FILL);
        activePaint.setColor(Color.WHITE);
        activePaint.setAntiAlias(true);
    }

    @Override
    public void onDrawOver(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDrawOver(c, parent, state);

        final Adapter adapter = parent.getAdapter();
        if (adapter != null) {
            /*total width of indicator, *2 as we need spaces between dots (circle indicator items)*/
            float totalWidth = 2 * indicatorRadius * adapter.getItemCount();
            /*start X of drawing indicator, parent.getWidth() - width of RecycleView=full screen width*/
            float posX = (parent.getWidth() - totalWidth) / 2;
            /*same for start Y*/
            float posY = (parent.getHeight() - 4 * indicatorRadius);

            final RecyclerView.LayoutManager lm = parent.getLayoutManager();
            /*init currentIndicator to NO_POSITION in RecycleView*/
            int currentIndicator = RecyclerView.NO_POSITION;
            /*just check if lm is LinearLayoutManager, probably not necessary. In case it's not - no indicator will be drawn*/
            if (lm instanceof LinearLayoutManager) {
                /*Returns the adapter position of the first fully visible view*/
                /*works correctly in our case as we have pager type RecycleView and only one page is visible at the moment */
                currentIndicator = ((LinearLayoutManager) lm).findFirstCompletelyVisibleItemPosition();
            }
            /*draw all indicators (dots), if active page -> draw with activePaint*/
            for (int i = 0; i < adapter.getItemCount(); i++) {
                drawIndicator(c, posX + 4 * indicatorRadius * i, posY, currentIndicator == i);
            }
        }
    }

    private void drawIndicator(Canvas c, float x, float y, boolean active) {
        c.drawCircle(x, y, indicatorRadius, active ? activePaint : inactivePaint);
    }
}
