package com.akchimwf.loftcoin1.util;

import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;
import javax.inject.Singleton;

/*realization of ImageLoader with Picasso*/
@Singleton
class PicassoImageLoader implements ImageLoader {

    private Picasso picasso;

    /*Picasso instance provided with Dagger*/
    @Inject
    PicassoImageLoader(Picasso picasso) {
        this.picasso = picasso;
    }

    @NonNull
    @NotNull
    @Override
    public ImageRequest load(String url) {
        return new PicassoImageRequest(picasso.load(url));
    }

    private static class PicassoImageRequest implements ImageRequest {

        private final RequestCreator request;

        PicassoImageRequest(RequestCreator request) {
            this.request = request;
        }

        @Override
        public void into(@NonNull @NotNull ImageView view) {
            request.into(view);
        }
    }
}
