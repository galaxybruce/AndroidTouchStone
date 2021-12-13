package com.galaxybruce.component.util.imageloader;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.galaxybruce.component.R;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * @date 2018/6/9 09:14
 * @author bruce.zhang
 * @description 图片加载，用的是glide
 *
 * glide使用：https://muyangmin.github.io/glide-docs-cn/doc/migrating.html
 * <p>
 * modification history:
 */
public class AppUniversalImageLoaderUtil {

    /**
     * glide图片展示
     *
     * @param target
     * @param uri
     * @param imageView
     * @param placeholder
     */
    public static void displayImage(Object target, String uri, ImageView imageView, int placeholder) {
        displayImage(target, uri, imageView, 0, 0, placeholder, null);
    }

    public static void displayImage(Object target, String uri, ImageView imageView, int width, int height,
                                    int placeholder) {
        displayImage(target, uri, imageView, width, height, placeholder, null);
    }

    public static void displayImage(Object target, String uri, ImageView imageView, int width, int height,
                                    int placeholder, IAppImageLoadListener listener) {
        if (TextUtils.isEmpty(uri)) {
            uri = "";
        }
        if (uri.endsWith("#gif")) {
            displayAsGif(target, uri.substring(0, uri.length() - 4), imageView, placeholder, listener);
        } else {
            displayAsBitmap(target, uri, imageView, width, height, placeholder, listener);
        }
    }

    public static void displayAsBitmap(Object target, final String uri, final ImageView imageView,
                                       int width, int height, int placeholder,
                                       final IAppImageLoadListener listener) {
        //这里height不需要填，以width为准
        //如果是0的话，则忽悠，以另外一个值为准，如果width和height都是0的话，则显示原图
        //如果都设置，超长图会以height压缩到很小，width被忽略了

        if(!((target instanceof Context) || (target instanceof Fragment))) {
            return;
        }

        String resultUrl;
        if (uri != null && uri.contains("wx.qlogo.cn")) {
            resultUrl = uri;
        } else if (width > 0) {
            resultUrl = AppImageUrlFormater.formatImage(uri, width, 0, 75);
        } else {
            resultUrl = AppImageUrlFormater.formatImage(uri);
        }

        RequestListener<Bitmap> requestListener = new RequestListener<Bitmap>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model,
                                        Target<Bitmap> target, boolean isFirstResource) {
                if(listener != null) {
                    return listener.onLoadingFailed(uri, imageView);
                }
                return false;
            }

            @Override
            public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target,
                                           DataSource dataSource, boolean isFirstResource) {
                if(listener != null) {
                    return listener.onLoadingComplete(uri, imageView, resource);
                }
                return false;
            }
        };

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

    private static void displayAsGif(Object target, String uri, ImageView imageView,
                                     int placeholder, IAppImageLoadListener listener) {
//        String resulturl;
//        if (width > 0) {
//            resulturl = formatGif(uri, width, height);
//        } else {
//            resulturl = uri;
//        }
        if(!((target instanceof Context) || (target instanceof Fragment))) {
            return;
        }

        RequestManager requestManager = getRequestManager(target);

        try {
            if (placeholder > 0) {
                requestManager
                        .asGif()
                        .load(uri)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(placeholder)
                        .error(placeholder)
                        .into(imageView);
            } else if (placeholder == -1) {
                requestManager
                        .asGif()
                        .load(uri)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.drawable.app_default_placeholder)
                        .error(R.drawable.app_default_placeholder)
                        .into(imageView);
            } else {
                requestManager
                        .asGif()
                        .load(uri)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(imageView);
            }
        } catch (Exception e) {
//            if (mImageFormatCheckFragment != null) {
//                mImageFormatCheckFragment.cancel();
//                mImageFormatCheckFragment = null;
//            }
        }
    }

    public static void displayAsBitmap(Object target, final Integer resourceId, final ImageView imageView) {
        if(!((target instanceof Context) || (target instanceof Fragment))) {
            return;
        }

        getRequestManager(target)
                .load(resourceId)
                .into(imageView);
    }

    public static void displayAsGif(Object target, final Integer resourceId, final ImageView imageView) {
        if(!((target instanceof Context) || (target instanceof Fragment))) {
            return;
        }

        getRequestManager(target)
                .asGif()
                .load(resourceId)
                .into(imageView);
    }

    public static RequestManager getRequestManager(Object target) {
        if (target instanceof Fragment) {
            return Glide.with((Fragment) target);
        } else if (target instanceof Activity) {
            return Glide.with((Activity) target);
        } else {
            return Glide.with((Context) target);
        }
    }
}
