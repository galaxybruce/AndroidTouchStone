package com.galaxybruce.component.ui.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * @author bruce.zhang
 * @date 2022/3/9 23:11
 * @description 透传事件给activity
 *
 * [android对话框透传Touch事件](https://segmentfault.com/a/1190000040418062)
 *
 * <p>
 * modification history:
 */
public class AppTouchPassThroughDialog extends Dialog {

    public AppTouchPassThroughDialog(@NonNull Context context) {
        super(context);
        init();
    }

    public AppTouchPassThroughDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        init();
    }

    protected AppTouchPassThroughDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init();
    }

    private void init() {
        Window window = getWindow();
        if(window != null) {
            window.addFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
        }
        setCanceledOnTouchOutside(false);
        setCancelable(false);
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        return super.onTouchEvent(event) || passThrough(event);
    }

    private boolean passThrough(MotionEvent event) {
        if(getContext() instanceof Activity) {
            return ((Activity)getContext()).dispatchTouchEvent(event);
        }
        return false;
    }
}
