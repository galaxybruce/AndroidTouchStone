package com.galaxybruce.component.util.imageloader;

import android.content.Context;
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
public class AppImageLoaderUtilWrapper {

    /**
     * 获取图片，指定宽高
     */
    public static void displayImage(Object target, String url, ImageView imageView, int width, int height,
                                    int placeholder, IAppImageLoadListener listener){
        if(target == null) {
            target = imageView != null ? imageView.getContext() : null;
        }
        if (target == null) {
            return;
        }
        AppUniversalImageLoaderUtil.displayImage(target, url, imageView,
                width, height, placeholder, listener);
    }

    /**
     * 显示原图
     * @deprecated 必须指明Object target是Activity还是Fragment
     */
    @Deprecated
    public static void displayOriginalImage(String url, ImageView imageView){
        AppImageLoaderUtilWrapper.displayOriginalImage(url, imageView, R.drawable.app_default_placeholder, null);
    }

    /**
     * 显示原图
     * 必须指明Object target是Activity还是Fragment
     */
    public static void displayOriginalImage(Object target, String url, ImageView imageView){
        AppImageLoaderUtilWrapper.displayOriginalImage(target, url, imageView, R.drawable.app_default_placeholder, null);
    }

    /**
     * 显示原图
     * @deprecated 必须指明Object target是Activity还是Fragment
     */
    @Deprecated
    public static void displayOriginalImage(String url, ImageView imageView, int placeholder,
                                            IAppImageLoadListener listener){
        AppImageLoaderUtilWrapper.displayOriginalImage(null, url, imageView, placeholder, listener);
    }

    /**
     * 显示原图
     * 必须指明Object target是Activity还是Fragment
     */
    public static void displayOriginalImage(Object target, String url, ImageView imageView, int placeholder,
                                            IAppImageLoadListener listener){
        AppImageLoaderUtilWrapper.displayImage(target, url, imageView,
                0, 0, placeholder, listener);
    }

    /**
     * 显示屏幕分辨率大小的图片
     * @deprecated 必须指明Object target是Activity还是Fragment
     */
    @Deprecated
    public static void displayScreenSizeImage(String url, ImageView imageView) {
        AppImageLoaderUtilWrapper.displayScreenSizeImage(null, url, imageView, R.drawable.app_default_placeholder, null);
    }

    /**
     *  必须指明Object target是Activity还是Fragment
     *
     * @param target
     * @param url
     * @param imageView
     */
    public static void displayScreenSizeImage(Object target, String url, ImageView imageView) {
        AppImageLoaderUtilWrapper.displayScreenSizeImage(target, url, imageView, R.drawable.app_default_placeholder,null);
    }

    /**
     * 显示屏幕分辨率大小的图片
     * @deprecated 必须指明Object target是Activity还是Fragment
     */
    @Deprecated
    public static void displayScreenSizeImage(String url, ImageView imageView,
                                              IAppImageLoadListener listener) {
        AppImageLoaderUtilWrapper.displayScreenSizeImage(null, url, imageView,
                R.drawable.app_default_placeholder, listener);
    }

    /**
     * 显示屏幕分辨率大小的图片
     * @param url
     * @param imageView
     *
     * 必须指明Object target是Activity还是Fragment
     */
    public static void displayScreenSizeImage(Object target, String url, ImageView imageView,
                                              int placeholder, IAppImageLoadListener listener) {
        int reqWidth = ScreenUtils.getScreenWidth();
        if(imageView.getScaleType() == ImageView.ScaleType.FIT_XY) {
            AppImageLoaderUtilWrapper.displayImage(target, url, imageView, reqWidth, 0,
                    placeholder, listener);
        } else {
            AppImageLoaderUtilWrapper.displayImage(target, url, imageView, reqWidth, 0,
                    placeholder, listener);
        }
    }

    /**
     *  加载小图片 小于200dp
     * @param url
     * @param imageView
     * @param listener
     * @deprecated 必须指明Object target是Activity还是Fragment
     */
    @Deprecated
    public static void displaySmallImage(String url, ImageView imageView,
                                         IAppImageLoadListener listener){
        AppImageLoaderUtilWrapper.displaySmallImage(null, url, imageView, R.drawable.app_default_placeholder, listener);
    }

    /**
     *  加载小图片 小于200dp
     * @param url
     * @param imageView
     * @param listener
     *
     * 必须指明Object target是Activity还是Fragment
     */
    public static void displaySmallImage(Object target, String url, ImageView imageView,
                                         IAppImageLoadListener listener){
        AppImageLoaderUtilWrapper.displaySmallImage(target, url, imageView, R.drawable.app_default_placeholder, listener);
    }

    /**
     * 加载小图片
     * @deprecated 必须指明Object target是Activity还是Fragment
     */
    @Deprecated
    public static void displaySmallImage(String url, ImageView imageView, int placeholder,
                                         IAppImageLoadListener listener){
        AppImageLoaderUtilWrapper.displaySmallImage(null, url, imageView, placeholder, listener);
    }

    /**
     * 加载小图片
     *
     * 必须指明Object target是Activity还是Fragment
     */
    public static void displaySmallImage(Object target, String url, ImageView imageView, int placeholder,
                                         IAppImageLoadListener listener){
        Context context = imageView != null ? imageView.getContext() : null;
        if (context == null) {
            return;
        }
        int size = context.getResources().getDimensionPixelSize(R.dimen.pic_size_small);
        AppImageLoaderUtilWrapper.displayImage(target, url, imageView, size, 0, placeholder, listener);
    }


    /**
     * 加载中等大小图片 200dp
     * @deprecated 必须指明Object target是Activity还是Fragment
     */
    @Deprecated
    public static void displayMiddleImage(String url, ImageView imageView){
        AppImageLoaderUtilWrapper.displayMiddleImage(null, url, imageView, R.drawable.app_default_placeholder,null);
    }

    /**
     * 加载中等大小图片 200dp
     *
     * 必须指明Object target是Activity还是Fragment
     */
    public static void displayMiddleImage(Object target, String url, ImageView imageView, int placeholder){
        AppImageLoaderUtilWrapper.displayMiddleImage(target, url, imageView, placeholder, null);
    }

    /**
     *  加载中等大小图片 200dp
     * @param url
     * @param imageView
     * @param listener
     * @deprecated 必须指明Object target是Activity还是Fragment
     */
    @Deprecated
    public static void displayMiddleImage(String url, ImageView imageView,
                                          IAppImageLoadListener listener){
        AppImageLoaderUtilWrapper.displayMiddleImage(null, url, imageView, R.drawable.app_default_placeholder, listener);
    }

    /**
     *  加载中等大小图片 200dp
     * @param url
     * @param imageView
     * @param listener
     *
     * 必须指明Object target是Activity还是Fragment
     */
    public static void displayMiddleImage(Object target, String url, ImageView imageView, int placeholder,
                                          IAppImageLoadListener listener){

        Context context = imageView != null ? imageView.getContext() : null;
        if (context == null) {
            return;
        }
        int size = context.getResources().getDimensionPixelSize(R.dimen.pic_size_middle);

        if(imageView.getScaleType() == ImageView.ScaleType.FIT_XY) {
            AppImageLoaderUtilWrapper.displayImage(target, url, imageView, size, 0, placeholder, listener);
        } else {
            AppImageLoaderUtilWrapper.displayImage(target, url, imageView, size, 0, placeholder, listener);
        }
    }

}
