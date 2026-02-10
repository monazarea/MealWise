package com.example.mealwise.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.example.mealwise.R;
import com.facebook.shimmer.Shimmer;
import com.facebook.shimmer.ShimmerDrawable;

public class ImageLoader {

    private static ShimmerDrawable getShimmerDrawable() {
        Shimmer shimmer = new Shimmer.AlphaHighlightBuilder()
                .setDuration(1500)
                .setBaseAlpha(0.7f)
                .setHighlightAlpha(0.9f)
                .setDirection(Shimmer.Direction.LEFT_TO_RIGHT)
                .setAutoStart(true)
                .build();

        ShimmerDrawable shimmerDrawable = new ShimmerDrawable();
        shimmerDrawable.setShimmer(shimmer);
        return shimmerDrawable;
    }

    public static void loadImage(Context context, String url, ImageView imageView) {
        Glide.with(context)
                .load(url)
                .placeholder(getShimmerDrawable())
                .error(R.drawable.ic_restaurant)
                .diskCacheStrategy(com.bumptech.glide.load.engine.DiskCacheStrategy.ALL)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imageView);
    }
}