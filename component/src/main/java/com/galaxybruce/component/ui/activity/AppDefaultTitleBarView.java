package com.galaxybruce.component.ui.activity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.ColorUtils;
import com.galaxybruce.component.R;
import com.galaxybruce.component.util.AppConstants;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

/**
 * @author bruce.zhang
 * @date 2022/11/11 22:21
 * @description title bar
 * <p>
 * modification history:
 */
public class AppDefaultTitleBarView implements IAppTitleBarView {

    private final AppCompatActivity mActivity;
    private @AppConstants.TitleMode final int mTitleMode;
    private ViewGroup mRootLayout;
    private Toolbar mToolbar;
    private FrameLayout mContentLayout;

    public AppDefaultTitleBarView(@NonNull AppCompatActivity activity, @AppConstants.TitleMode int titleMode) {
        mActivity = activity;
        mTitleMode = titleMode;
    }

    @Override
    public ViewGroup getContentView() {
        final int titleBarHeight = mActivity.getResources().getDimensionPixelSize(R.dimen.app_title_bar_height);
        View view = LayoutInflater.from(mActivity).inflate(R.layout.activity_title, null);
        mRootLayout = view.findViewById(R.id.root_layout);
        mContentLayout = view.findViewById(R.id.content_layout);
        ((ViewGroup.MarginLayoutParams)mContentLayout.getLayoutParams()).topMargin =
                mTitleMode == AppConstants.TITLE_MODE_LINEAR ? titleBarHeight : 0;
        mToolbar = view.findViewById(R.id.title_bar_layout);
        return mRootLayout;
    }

    /**
     * 获取标题栏以外的实际内容根布局
     * @return
     */
    @Override
    public ViewGroup getContentLayout() {
        return mContentLayout;
    }

    public void initTitle(String title, boolean showHomeAsUp) {
        BarUtils.setStatusBarColor(mActivity, ColorUtils.getColor(R.color.colorPrimaryDark));
        BarUtils.addMarginTopEqualStatusBarHeight(mRootLayout);
        Toolbar toolbar = mToolbar;
        if(toolbar != null) {
            toolbar.setBackgroundResource(R.color.colorPrimaryDark);
            mActivity.setSupportActionBar(toolbar);
            mActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(showHomeAsUp);
            mActivity.getSupportActionBar().setTitle(title);
        }
    }

}
