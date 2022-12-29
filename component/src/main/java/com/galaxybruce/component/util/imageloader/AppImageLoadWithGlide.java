package com.galaxybruce.component.util.imageloader;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.galaxybruce.component.R;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * @date 2018/6/9 09:14
 * @author bruce.zhang
 * @description 图片加载-glide实现
 *
 * glide使用：https://muyangmin.github.io/glide-docs-cn/doc/migrating.html
 * <p>
 * modification history:
 */
class AppImageLoadWithGlide {

    /**
     *
     * @param target Activity|Fragment|Context
     * @param imageView
     * @param uri
     * @param width 显示原图时传0
     * @param height 显示原图时传0
     * @param placeholder
     * @param listener
     */
    public static void displayAsBitmap(Object target, final ImageView imageView, final String uri,
                                       int width, int height, int placeholder,
                                       final AppBitmapLoadListener listener) {
        //这里height不需要填，以width为准
        //如果是0的话，则忽悠，以另外一个值为准，如果width和height都是0的话，则显示原图
        //如果都设置，超长图会以height压缩到很小，width被忽略了

        if(!((target instanceof Context) || (target instanceof Fragment))) {
            if(listener != null) {
                listener.onLoadingFailed(imageView, uri);
            }
            return;
        }

        String resultUrl;
        if (uri != null && uri.contains("wx.qlogo.cn")) {
            resultUrl = uri;
        } else if (width > 0) {
            resultUrl = AppImageUrlFormatter.formatImage(uri, width, 0, 90);
        } else {
            resultUrl = AppImageUrlFormatter.formatImage(uri);
        }

        RequestListener<Bitmap> requestListener = listener != null ? new RequestListener<Bitmap>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model,
                                        Target<Bitmap> target, boolean isFirstResource) {
                return listener.onLoadingFailed(imageView, uri);
            }

            @Override
            public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target,
                                           DataSource dataSource, boolean isFirstResource) {
                return listener.onLoadingComplete(imageView, uri, resource);
            }
        } : null;

        RequestManager requestManager = getRequestManager(target);
        try {
            if (placeholder > 0) {
                requestManager
                        .asBitmap()
                        .load(resultUrl)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(placeholder)
                        .error(placeholder)
                        .listener(requestListener)
                        .into(imageView);
            } else if (placeholder == 0) {
                requestManager
                        .asBitmap()
                        .load(resultUrl)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.drawable.app_default_placeholder)
                        .error(R.drawable.app_default_placeholder)
                        .listener(requestListener)
                        .into(imageView);
            } else {
                requestManager
                        .asBitmap()
                        .load(resultUrl)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .listener(requestListener)
                        .into(imageView);
            }
        } catch (Exception e) {
//            if (mImageFormatCheck != null) {
//                mImageFormatCheck.cancel();
//                mImageFormatCheck = null;
//            }
        }
    }

    public static void displayAsGif(Object target, ImageView imageView, String uri,
                                     int placeholder, AppGifLoadListener listener) {
        if(!((target instanceof Context) || (target instanceof Fragment))) {
            if(listener != null) {
                listener.onLoadingFailed(imageView, uri);
            }
            return;
        }

        RequestListener<GifDrawable> requestListener = listener != null ? new RequestListener<GifDrawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model,
                                        Target<GifDrawable> target, boolean isFirstResource) {
                return listener.onLoadingFailed(imageView, uri);
            }

            @Override
            public boolean onResourceReady(GifDrawable resource, Object model, Target<GifDrawable> target,
                                           DataSource dataSource, boolean isFirstResource) {
                return listener.onLoadingComplete(imageView, uri, resource);
            }
        } : null;

        RequestManager requestManager = getRequestManager(target);
        try {
            if (placeholder > 0) {
                requestManager
                        .asGif()
                        .load(uri)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(placeholder)
                        .error(placeholder)
                        .listener(requestListener)
                        .into(imageView);
            } else if (placeholder == -1) {
                requestManager
                        .asGif()
                        .load(uri)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.drawable.app_default_placeholder)
                        .error(R.drawable.app_default_placeholder)
                        .listener(requestListener)
                        .into(imageView);
            } else {
                requestManager
                        .asGif()
                        .load(uri)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .listener(requestListener)
                        .into(imageView);
            }
        } catch (Exception e) {
//            if (mImageFormatCheckFragment != null) {
//                mImageFormatCheckFragment.cancel();
//                mImageFormatCheckFragment = null;
//            }
        }
    }

    static RequestManager getRequestManager(Object target) {
        if (target instanceof Fragment) {
            return Glide.with((Fragment) target);
        } else if (target instanceof Activity) {
            return Glide.with((Activity) target);
        } else {
            return Glide.with((Context) target);
        }
    }
}
