package com.galaxybruce.component.util.imageloader;

import android.graphics.Bitmap;
import android.view.View;

/**
 * Date: 2017/5/4 15:02
 * Author: bruce.zhang
 * <p>
 * Modification  History:
 */
public class AppSimpleImageLoadListener implements IAppImageLoadListener {

    @Override
    public void onLoadingStarted(String imageUri, View view) {

    }

    @Override
    public boolean onLoadingFailed(String imageUri, View view) {
        return false;
    }

    @Override
    public boolean onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
        return false;
    }

    @Override
    public void onLoadingCancelled(String imageUri, View view) {

    }
}
