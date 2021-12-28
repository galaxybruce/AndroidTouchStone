package com.galaxybruce.component.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.widget.Toast;

public class ToastUtils {
    private static Toast mToast;

    @SuppressLint("ShowToast")
    public static void showToast(Context context, String message, int duration) {
        if (TextUtils.isEmpty(message) || context == null) {
            return;
        }
        try {
//            if (mToast == null) {
//                mToast = Toast.makeText(context.getApplicationContext(), message, duration);
//            } else {
//                mToast.setDuration(duration);
//                mToast.setText(message);
//                // cancel same toast only on Android P and above, to avoid IllegalStateException on addView
//                // 参考 https://stackoverflow.com/questions/51956971/illegalstateexception-of-toast-view-on-android-p-preview
//                if (mToast.getView() != null && mToast.getView().isShown()) {
//                    mToast.cancel();
//                }
//            }
            Toast toast = Toast.makeText(context.getApplicationContext(), message, duration);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void showToast(Context context, String message) {
        showToast(context, message, Toast.LENGTH_SHORT);
    }

    public static void showToast(Context context, int resId) {
        String message = context.getString(resId);
        showToast(context, message);
    }

    public static void showToast(Context context, int resId, int duration) {
        String message = context.getString(resId);
        showToast(context, message, duration);
    }

    public static void cancelToast() {
        if (mToast != null) {
            mToast.cancel();
            mToast = null;
        }
    }
}
