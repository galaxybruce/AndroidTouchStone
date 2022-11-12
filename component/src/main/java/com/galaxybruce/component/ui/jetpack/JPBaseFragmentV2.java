package com.galaxybruce.component.ui.jetpack;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.galaxybruce.component.ui.activity.AppDefaultTitleBarView;
import com.galaxybruce.component.ui.activity.IAppTitleBarView;
import com.galaxybruce.component.util.AppConstants;

import java.lang.reflect.ParameterizedType;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.ViewDataBinding;

public abstract class JPBaseFragmentV2<B extends ViewDataBinding, VM extends JPBaseViewModel> extends JPBaseFragment<B> {

    protected VM mPageViewModel;
    protected IAppTitleBarView mAppTitleBarView;

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

    @Override
    public View setRootLayout(int layoutId, @NonNull LayoutInflater inflater, ViewGroup container) {
        View view;
        int titleMode = getTitleMode();
        if(titleMode == AppConstants.TITLE_MODE_LINEAR || titleMode == AppConstants.TITLE_MODE_FLOAT) {
            mAppTitleBarView = createAppTitleBarView( titleMode);
            view = mAppTitleBarView.getContentView();
            // 业务布局
            mDataBinding = mJPPageDelegate.setRootLayout(layoutId, inflater,
                    mAppTitleBarView.getContentLayout(), true);
            initTitle();
        } else if(titleMode == AppConstants.TITLE_MODE_CUSTOM){
            view = super.setRootLayout(layoutId, inflater, container);
            initTitle();
        } else {
            view = super.setRootLayout(layoutId, inflater, container);
        }
        return view;
    }

    /**
     * 获取标题栏模式
     * @return
     */
    protected @AppConstants.TitleMode int getTitleMode() {
        return AppConstants.TITLE_MODE_LINEAR;
    }

    /**
     * 给外层app提供自定义标题栏的机会
     * @param titleMode
     * @return
     */
    protected IAppTitleBarView createAppTitleBarView(@AppConstants.TitleMode int titleMode) {
        return new AppDefaultTitleBarView((AppCompatActivity) mActivity, titleMode);
    }

    protected void initTitle() {
        if(mAppTitleBarView != null && mAppTitleBarView instanceof AppDefaultTitleBarView) {
            ((AppDefaultTitleBarView)mAppTitleBarView).initTitle(bindTitle(), false);
        }
    }

    protected String bindTitle() {
        return null;
    }
}
