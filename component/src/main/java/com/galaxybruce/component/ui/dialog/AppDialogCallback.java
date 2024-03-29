package com.galaxybruce.component.ui.dialog;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @date 2022/2/14 10:34
 * @author bruce.zhang
 * @description dialog callback 父类，用于防止旋转屏幕等callback丢失
 * 也可以参考ResultReciver实现 或者直接使用ResultReciver来当做callback
 *
 *  public T setCallback(Parcelable callback) {
 *      bundle.putParcelable("callback", callback);
 *      return (T)this;
 *  }
 *
 * modification history:
 */

public interface AppDialogCallback extends Parcelable {

    default int describeContents() {
        return 0;
    }

    default void writeToParcel(Parcel dest, int flags) {

    }
}
