package com.sakura.request;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.load.resource.bitmap.TransformationUtils;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.util.Util;
import com.sakura.utils.Config;

import java.nio.ByteBuffer;
import java.security.MessageDigest;

/**
 * The class is intended for the image-related processes.
 */
public class ImageLoader {

    private static int getRadius(int radiusDp) {
        return (int) (radiusDp * Resources.getSystem().getDisplayMetrics().density + 0.5f);
    }


    /**
     * In various occasions, the image loading is a complicated issue.<br/>
     * The method aims at making the process easy.<br/>
     * <h2>Importantly, the method can only be run in UI thread!</h2>
     *
     * @param context   The context of view which contains the ImageView
     * @param imageView The image container intended to fit an image from web
     * @param url       the online url of the image
     */
    public static void invoke(Context context, ImageView imageView, CharSequence url) {
        Glide.with(context)
                .load(url)
                .transform(new RoundCornerTransformation(Config.ANIME_PICTURE_RADIUS, true))
                .transition(withCrossFade())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .skipMemoryCache(false)
                .dontAnimate()
                .into(imageView);
    }

    public static void invokeTop(Context context, ImageView imageView, CharSequence url) {
        Glide.with(context)
                .load(url)
                .transform(new TopRoundCornerTransformation(Config.ANIME_PICTURE_RADIUS, true))
                .transition(withCrossFade())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .skipMemoryCache(false)
                .dontAnimate()
                .into(imageView);
    }

    @SuppressWarnings("unused")
    public static void fullInvoke(Context context, ImageView imageView, CharSequence url, @DrawableRes int placeholderId, @DrawableRes int errorId) {
        RequestOptions options = new RequestOptions()
                .placeholder(placeholderId)
                .error(errorId)
                .transform(new RoundCornerTransformation(Config.ANIME_PICTURE_RADIUS, true));
        Glide.with(context).load(url).apply(options)
                .thumbnail(provide(imageView.getContext(), placeholderId, Config.ANIME_PICTURE_RADIUS, true))
                .thumbnail(provide(imageView.getContext(), errorId, Config.ANIME_PICTURE_RADIUS, true))
                .into(imageView);
    }

    private static RequestBuilder<Drawable> provide(Context context, @DrawableRes int placeholderId, int radiusDp, boolean isCenterCrop) {
        return Glide.with(context)
                .load(placeholderId)
                .apply(new RequestOptions().transform(new RoundCornerTransformation(getRadius(radiusDp), isCenterCrop)));
    }


    static class RoundCornerTransformation extends BitmapTransformation {

        private final boolean isCenterCrop;
        private final int radius;

        public RoundCornerTransformation(int radiusDp, boolean isCenterCrop) {
            this.isCenterCrop = isCenterCrop;
            this.radius = getRadius(radiusDp);
        }

        @Override
        protected Bitmap transform(@NonNull BitmapPool pool, @NonNull Bitmap toTransform, int outWidth, int outHeight) {
            if (isCenterCrop) {
                Bitmap bitmap = TransformationUtils.centerCrop(pool, toTransform, outWidth, outHeight);
                return TransformationUtils.roundedCorners(pool, bitmap, radius);
            } else return TransformationUtils.roundedCorners(pool, toTransform, radius);
        }

        @Override
        public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {
            messageDigest.update("com.bumptech.glide.transformations.FillSpace".getBytes(CHARSET));
            byte[] radiusData = ByteBuffer.allocate(4).putInt((int) radius).array();
            messageDigest.update(radiusData);
        }

        @Override
        public int hashCode() {
            return Util.hashCode("com.bumptech.glide.transformations.FillSpace".hashCode(), Util.hashCode(radius));
        }

        @Override
        public boolean equals(@Nullable Object obj) {
            if (obj instanceof RoundCornerTransformation) {
                RoundCornerTransformation other = (RoundCornerTransformation) obj;
                return radius == other.radius;
            }
            return false;
        }
    }

    static class TopRoundCornerTransformation extends RoundCornerTransformation {
        private final boolean isCenterCrop;
        private final int radius;

        public TopRoundCornerTransformation(int radiusDp, boolean isCenterCrop) {
            super(radiusDp,isCenterCrop);
            this.isCenterCrop = isCenterCrop;
            this.radius = getRadius(radiusDp);
        }

        @Override
        protected Bitmap transform(@NonNull BitmapPool pool, @NonNull Bitmap toTransform, int outWidth, int outHeight) {
            if (isCenterCrop) {
                Bitmap bitmap = TransformationUtils.centerCrop(pool, toTransform, outWidth, outHeight);
                return TransformationUtils.roundedCorners(pool, bitmap, radius, radius, 100, 0);
            } else
                return TransformationUtils.roundedCorners(pool, toTransform, radius, radius, 100, 0);
        }
    }
}
