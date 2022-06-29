package com.akchimwf.loftcoin1.util;

import android.widget.ImageView;

import androidx.annotation.NonNull;

/*typical API for graphic packet = abstraction for our needs*/
/*Picasso has the same methods*/
public interface ImageLoader {

    @NonNull
    ImageRequest load(String url);

    interface ImageRequest {
        void into(@NonNull ImageView view);
    }
}
