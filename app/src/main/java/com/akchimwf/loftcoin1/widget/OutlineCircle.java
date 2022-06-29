package com.akchimwf.loftcoin1.widget;

import android.graphics.Outline;
import android.view.View;
import android.view.ViewOutlineProvider;

import androidx.annotation.NonNull;

/*Interface by which a View builds its Outline, used for shadow casting and clipping.*/
public class OutlineCircle extends ViewOutlineProvider {

    //TODO memory leak if we use static method and place View on input?
    public static void apply(@NonNull View view) {
        view.setOutlineProvider(new ViewOutlineProvider() {
            @Override
            public void getOutline(View view, Outline outline) {
                int minSize = Math.min(view.getWidth(), view.getHeight());   //common use for a case of a rectangle, not square as we have set round rectangle, radius=rectangle side/2
                outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), minSize / 2f);
            }
        });
    }

    @Override
    public void getOutline(View view, Outline outline) {
        int minSize = Math.min(view.getWidth(), view.getHeight());   //common use for a case of a rectangle, not square as we have set round rectangle, radius=rectangle side/2
        outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), minSize / 2f);
    }
}
