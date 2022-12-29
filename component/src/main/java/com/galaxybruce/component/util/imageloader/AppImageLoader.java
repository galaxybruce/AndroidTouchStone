package com.galaxybruce.component.util.imageloader;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;

import com.blankj.utilcode.util.ScreenUtils;
import com.galaxybruce.component.R;

/**
 * @author bruce.zhang
 * @date 2019/3/6 16:52
 * @description 图片加载工具类
 *
 * 图片加载建议策略：
 * 1. 指明是scope是Activity还是fragment，因为当一个页面是ViewPager时，快速滚动ViewPage，会导致很多页面在后台加载
 * 导致后面的页面的图片会很长时间才显示
 * 2. RecyclerView或者ListView必须指定BBSPauseGlideOnRecyclerScrollListener，滚动时停止加载
 * 3. 必须指定是small、middle、screen、original四种尺寸的图片
 *
 * <p>
 * modification history:
 */
public class AppImageLoader {

    public static void displayImage(Object target, ImageView imageView, String uri) {
        displayImage(target, imageView, uri, R.drawable.app_default_placeholder, AppImageSize.ORIGINAL);
    }

    /**
     * 根据url后缀判断是显示bitmap or gif
     * @param target
     * @param imageView
     * @param uri
     * @param imageSize
     * @param placeholder
     */
    public static void displayImage(Object target, ImageView imageView, String uri,
                                    int placeholder, AppImageSize imageSize) {
        if (TextUtils.isEmpty(uri)) {
            uri = "";
        }
        if (uri.endsWith("#gif")) {
            AppImageLoadWithGlide.displayAsGif(target, imageView, uri.substring(0, uri.length() - 4), placeholder, null);
        } else {
            displayBitmap(target, imageView, uri, placeholder, imageSize, null);
        }
    }

    /**
     * 加载图片-指定具体尺寸
     * @param target target Activity|Fragment|Context
     * @param imageView
     * @param url
     * @param width
     * @param height
     * @param placeholder
     * @param listener
     */
    public static void displayBitmap(Object target, ImageView imageView, String url, int width, int height,
                                    int placeholder, AppBitmapLoadListener listener){
        AppImageLoadWithGlide.displayAsBitmap(target, imageView, url,
                width, height, placeholder, listener);
    }

    /**
     * 加载图片-指定图片尺寸类型
     * @param target
     * @param imageView
     * @param url
     * @param imageSize
     */
    public static void displayBitmap(Object target, ImageView imageView, String url,
                                     AppImageSize imageSize){
        displayBitmap(target, imageView, url, R.drawable.app_default_placeholder, imageSize, null);
    }

    public static void displayBitmap(Object target, ImageView imageView, String url,
                                     AppImageSize imageSize, AppBitmapLoadListener listener){
        displayBitmap(target, imageView, url, R.drawable.app_default_placeholder, imageSize, listener);
    }

    /**
     * 加载图片-指定图片尺寸类型
     * @param target
     * @param imageView
     * @param url
     * @param placeholder
     * @param imageSize
     * @param listener
     */
    public static void displayBitmap(Object target, ImageView imageView, String url, int placeholder,
                                            AppImageSize imageSize, AppBitmapLoadListener listener){
        if(placeholder <= 0) {
            placeholder = R.drawable.app_default_placeholder;
        }
        if(imageSize == AppImageSize.SMALL) {
            Context context = imageView != null ? imageView.getContext() : null;
            if (context == null) {
                return;
            }
            int size = context.getResources().getDimensionPixelSize(R.dimen.pic_size_small);
            displayBitmap(target, imageView, url, size, 0, placeholder, listener);
        } else if(imageSize == AppImageSize.MIDDLE) {
            Context context = imageView != null ? imageView.getContext() : null;
            if (context == null) {
                return;
            }
            int size = context.getResources().getDimensionPixelSize(R.dimen.pic_size_middle);
            displayBitmap(target, imageView, url, size, 0, placeholder, listener);
        } else if(imageSize == AppImageSize.SCREEN) {
            int reqWidth = ScreenUtils.getScreenWidth();
            displayBitmap(target, imageView, url, reqWidth, 0,
                    placeholder, listener);
        } else {
            displayBitmap(target, imageView, url, 0, 0, placeholder, listener);
        }
    }

    public static void displayGif(Object target, ImageView imageView, String url){
        AppImageLoadWithGlide.displayAsGif(target, imageView, url,
                R.drawable.app_default_placeholder, null);
    }

    public static void displayGif(Object target, ImageView imageView, String url,
                                  int placeholder, AppGifLoadListener listener){
        AppImageLoadWithGlide.displayAsGif(target, imageView, url,
                placeholder, listener);
    }
}
