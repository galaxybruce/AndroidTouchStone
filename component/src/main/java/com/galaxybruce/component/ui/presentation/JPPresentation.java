package com.galaxybruce.component.ui.presentation;

import android.app.Presentation;
import android.content.Context;
import android.hardware.display.DisplayManager;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.WindowManager;

import com.galaxybruce.component.ui.IUiInit;
import com.galaxybruce.component.ui.jetpack.JPBaseViewModel;
import com.galaxybruce.component.ui.jetpack.JPHost;
import com.galaxybruce.component.ui.presentation.view.AppPresentationLoadingView;
import com.galaxybruce.component.ui.presentation.view.AppPresentationToastView;
import com.galaxybruce.component.util.log.AppLogUtils;

import androidx.annotation.NonNull;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;
import androidx.lifecycle.ViewModelStore;
import androidx.lifecycle.ViewModelStoreOwner;

/**
 * @author bruce.zhang
 * @date 2021/6/15 14:24
 * @description 全局Presentation
 *
 * 要点：
 * 1. 添加权限
 * <uses-permission android:name="android.permission.SYSTEM_ALERT_WINDOW"/>
 * <uses-permission android:name= "android.permission.SYSTEM_OVERLAY_WINDOW"/>
 * 2. 设置浮窗模式
 *  getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
 *
 * 参考文章：
 * Android双屏机，副屏内容的显示和关闭 https://blog.csdn.net/xuanshao_/article/details/109742983
 * Android 双屏异显实现的三种方式 https://bbs.huaweicloud.com/blogs/226759
 * 副屏不随主屏幕退出 https://www.jianshu.com/p/d42f3ec6e451
 * android 双屏异显 副屏幕不随主屏幕退出 https://blog.csdn.net/wangsheng5454/article/details/80227786
 * RK平台双屏异显方案 https://files.imin.sg/img/iminDoc_bak/zh/Presentation.html
 *
 * <p>
 * modification history:
 */
public abstract class JPPresentation<B extends ViewDataBinding>
        extends Presentation
        implements JPHost, IUiInit, LifecycleOwner, ViewModelStoreOwner {

    protected Context mOuterContext;

    private B mDataBinding;
    private JPPresentationDelegate<B> mJPPresentationDelegate;

    private LifecycleRegistry lifecycle = new LifecycleRegistry(this);
    private ViewModelStore mViewModelStore = new ViewModelStore();

    private AppPresentationLoadingView mLoadingView;
    private AppPresentationToastView mToastView;

    public JPPresentation(Context outerContext) {
        this(outerContext, 0);
    }

    public JPPresentation(Context outerContext, int theme) {
        super(outerContext.getApplicationContext(), gainDisplay(outerContext), theme);
        init(outerContext.getApplicationContext());
    }

    private static Display gainDisplay(Context outerContext) {
        DisplayManager displayManager = (DisplayManager)outerContext.getSystemService(Context.DISPLAY_SERVICE);
        Display[] displays = displayManager.getDisplays(); //得到显示器数组

        for (Display display1 : displays) {
            DisplayMetrics metric = new DisplayMetrics();
            display1.getMetrics(metric);
            AppLogUtils.i("screen info====== w: " + metric.widthPixels + "; h: " + metric.heightPixels + "; dpi: " + metric.density);
        }

        return displays.length > 1 ? displays[1] : displays[0];
    }

    private void init(Context outerContext) {
        mOuterContext = outerContext;
        // 浮窗模式
        //检查版本，注意当type为TYPE_APPLICATION_OVERLAY时，铺满活动窗口，但在关键的系统窗口下面，如状态栏或IME
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY);
        } else {
            getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        }
    }

    @Override
    public void show() {
        DisplayManager displayManager = (DisplayManager)mOuterContext.getSystemService(Context.DISPLAY_SERVICE);
        Display[] displays = displayManager.getDisplays(); //得到显示器数组
        if(displays.length > 1) {
            super.show();
        }
    }

    @Override
    public LifecycleOwner getLifecycleOwner() {
        return this;
    }

    /**
     * 多个"通用的ViewModel"的初始化
     * @return
     */
    @Override
    public JPBaseViewModel[] initViewModels() {
        return null;
    }

    /**
     * TODO tip: 警惕使用。非必要情况下，尽可能不在子类中拿到 binding 实例乃至获取 view 实例。使用即埋下隐患。
     * 目前的方案是在 debug 模式下，对获取实例的情况给予提示。
     * <p>
     * 如果这样说还不理解的话，详见 https://xiaozhuanlan.com/topic/9816742350 和 https://xiaozhuanlan.com/topic/2356748910
     *
     * @return binding
     */
    protected B getBinding() {
        return mDataBinding;
    }

    @Override
    @NonNull
    public Lifecycle getLifecycle() {
        return lifecycle;
    }

    @NonNull
    @Override
    public ViewModelStore getViewModelStore() {
        return mViewModelStore;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_CREATE);

        mJPPresentationDelegate = new JPPresentationDelegate<>(this);

        setRootLayout(bindLayoutId());
        initData(null, savedInstanceState);
        initView(mDataBinding != null ? mDataBinding.getRoot() : null);
        bindData(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_START);
    }

    @Override
    protected void onStop() {
        super.onStop();
        lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_STOP);
    }

    @Override
    public void dismiss() {
        super.dismiss();
        lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY);
        hideLoading();
        hideToast();
    }

    protected void setRootLayout(int layoutId) {
        mDataBinding = mJPPresentationDelegate.setRootLayout(layoutId, LayoutInflater.from(getContext()), null);
        if(mDataBinding != null) {
            setContentView(mDataBinding.getRoot());
        }
    }

    protected <T extends JPBaseViewModel> T getPresentationModel(@NonNull Class<T> modelClass) {
        return mJPPresentationDelegate.getPresentationModel(modelClass);
    }

    public void showLoading() {
        // window manager不能同时add两个view
        hideToast();
        if(mLoadingView == null) {
            mLoadingView = new AppPresentationLoadingView(mOuterContext);
        }
        mLoadingView.show(null);
    }

    public void hideLoading() {
        if(mLoadingView != null) {
            mLoadingView.hide();
            mLoadingView = null;
        }
    }

    public void showToast(final String message) {
        // window manager不能同时add两个view
        hideLoading();
        if(mToastView == null) {
            mToastView = new AppPresentationToastView(mOuterContext);
        }
        mToastView.show(message);
    }

    public void hideToast() {
        if(mToastView != null) {
            mToastView.hide();
            mToastView = null;
        }
    }
    
}
