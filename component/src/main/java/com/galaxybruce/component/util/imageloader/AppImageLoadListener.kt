package com.galaxybruce.component.util.imageloader

import android.widget.ImageView

/**
 * @date 2022/12/29 14:56
 * @author bruce.zhang
 * @description 图片加载listener
 *
 * modification history:
 */
interface AppImageLoadListener<T> {

    fun onLoadingStarted(view: ImageView?, imageUri: String?)

    /**
     * @return 参考glide解释
     */
    fun onLoadingComplete(view: ImageView?, imageUri: String?, loadedImage: T?): Boolean

    /**
     * @return 参考glide解释
     */
    fun onLoadingFailed(view: ImageView?, imageUri: String?): Boolean

}