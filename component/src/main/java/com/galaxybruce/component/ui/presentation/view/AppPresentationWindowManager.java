package com.galaxybruce.component.ui.presentation.view;

import android.content.Context;
import android.graphics.PixelFormat;
import android.hardware.display.DisplayManager;
import android.os.Build;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

/**
 * @author bruce.zhang
 * @date 2021/6/15 19:28
 * @description Presentation view管理
 *
 * ViewManger只能同时添加一个view
 *
 * <p>
 * modification history:
 */
public class AppPresentationWindowManager {

    protected Display secondDisplay;
    protected Context secondDisplayContext;
    protected WindowManager windowManager;
    protected WindowManager.LayoutParams layoutParams;

    protected View view;

    public AppPresentationWindowManager(Context outerContext) {
        DisplayManager displayManager = (DisplayManager) outerContext.getSystemService(Context.DISPLAY_SERVICE);
        Display[] displays = displayManager.getDisplays(); //得到显示器数组
        Display display = displays.length > 1 ? displays[1] : displays[0];
        this.secondDisplay = display;
        if (display != null) {
            this.secondDisplayContext = outerContext.createDisplayContext(this.secondDisplay);
            this.windowManager = (WindowManager) this.secondDisplayContext.getSystemService(Context.WINDOW_SERVICE);

            final int windowType;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                windowType = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
            } else {
                windowType = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
            }
            this.layoutParams = new WindowManager.LayoutParams(windowType,
                    3,
                    PixelFormat.TRANSLUCENT);
            this.layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT;
            this.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        }
    }

    public View addView(int layoutId) {
        removeView();
        this.view = View.inflate(this.secondDisplayContext, layoutId, null);
        this.resizeView(layoutParams);
        this.windowManager.addView(this.view, this.layoutParams);
        return this.view;
    }

    public void removeView() {
        if(view != null) {
            this.windowManager.removeView(this.view);
        }
    }

    public View getView() {
        return view;
    }

    public void resizeView(WindowManager.LayoutParams layoutParams) {

    }
}
