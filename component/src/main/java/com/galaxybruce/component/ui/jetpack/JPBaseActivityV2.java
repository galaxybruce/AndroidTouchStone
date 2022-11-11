package com.galaxybruce.component.ui.jetpack;

import android.view.LayoutInflater;
import android.view.View;

import com.galaxybruce.component.ui.activity.AppTitleBarView;
import com.galaxybruce.component.util.AppConstants;

import java.lang.reflect.ParameterizedType;

import androidx.annotation.Nullable;
import androidx.databinding.ViewDataBinding;

public abstract class JPBaseActivityV2<B extends ViewDataBinding, VM extends JPBaseViewModel> extends JPBaseActivity<B> {

    protected VM mPageViewModel;
    protected AppTitleBarView mAppTitleBarView;

    /**
     * 子类可以重写该方法
     * mPageViewModel = getActivityViewModel(MainViewModel::class.java)
     *
     * @return
     */
    @Override
    public JPBaseViewModel initViewModel() {
        Class<VM> tClass = (Class<VM>)((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[1];
        mPageViewModel = getActivityViewModel(tClass);
        return mPageViewModel;
    }

    /**
     * 获取标题栏模式
     * @return
     */
    protected @AppConstants.TitleMode int getTitleMode() {
        return AppConstants.TITLE_MODE_LINEAR;
    }

    @Override
    public void setRootLayout(int layoutId) {
        int titleMode = getTitleMode();
        if(titleMode == AppConstants.TITLE_MODE_LINEAR) {
            mAppTitleBarView = new AppTitleBarView(this, titleMode);
            setContentView(mAppTitleBarView.getContentView());
            // 业务布局
            mDataBinding = mJPPageDelegate.setRootLayout(layoutId, LayoutInflater.from(mActivity),
                    mAppTitleBarView.getContentLayout(), true);
        } else {
            super.setRootLayout(layoutId);
        }
    }

    @Override
    public void initView(@Nullable View view) {
        super.initView(view);
        initTitle();
    }

    protected void initTitle() {
        if(mAppTitleBarView != null) {
            mAppTitleBarView.initTitle(bindTitle());
        }
    }

    protected String bindTitle() {
        return null;
    }
}
