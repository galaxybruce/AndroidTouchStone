package com.galaxybruce.component.util.imageloader;

import android.graphics.Bitmap;
import android.view.View;

/**
 * Date: 2017/5/4 15:01
 * Author: bruce.zhang
 * <p>
 * Modification  History:
 */
public interface IAppImageLoadListener {

    void onLoadingStarted(String imageUri, View view);

    boolean onLoadingFailed(String imageUri, View view);

    boolean onLoadingComplete(String imageUri, View view, Bitmap loadedImage);

    void onLoadingCancelled(String imageUri, View view);
}
