package com.galaxybruce.component.ui.jetpack;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.galaxybruce.component.ui.activity.AppDefaultTitleBarView;
import com.galaxybruce.component.ui.activity.AppTitleInfo;
import com.galaxybruce.component.ui.activity.IAppTitleBarView;
import com.galaxybruce.component.util.AppConstants;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.ViewDataBinding;

import static com.galaxybruce.component.ui.jetpack.util.JPViewModeExtKt.getVmClazz;

public abstract class JPBaseFragmentV2<VM extends JPBaseViewModel, B extends ViewDataBinding> extends JPBaseFragment<B> {

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
        Class<VM> tClass = getVmClazz(this);
        if(!tClass.equals(JPBaseViewModel.class)) {
            mPageViewModel = getFragmentViewModel(tClass);
        }
        return mPageViewModel;
    }

    @Override
    public View setRootLayout(int layoutId, @NonNull LayoutInflater inflater, ViewGroup container) {
        View view;
        AppTitleInfo titleInfo = getTitleInfo();
        int titleMode = titleInfo.getTitleMode();
        if(titleMode == AppConstants.TITLE_MODE_LINEAR || titleMode == AppConstants.TITLE_MODE_FLOAT) {
            mAppTitleBarView = createAppTitleBarView(titleMode);
            view = mAppTitleBarView.getContentView();
            // 业务布局
            mDataBinding = mJPPageDelegate.setRootLayout(layoutId, inflater,
                    mAppTitleBarView.getContentLayout(), true);
        } else if(titleMode == AppConstants.TITLE_MODE_CUSTOM){
            view = super.setRootLayout(layoutId, inflater, container);
        } else {
            view = super.setRootLayout(layoutId, inflater, container);
        }
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        AppTitleInfo titleInfo = getTitleInfo();
        int titleMode = titleInfo.getTitleMode();
        if(titleMode != AppConstants.TITLE_MODE_NONE) {
            initTitle(titleInfo);
        }
    }

    /**
     * 获取标题栏信息
     * @return
     */
    protected @NonNull AppTitleInfo getTitleInfo() {
        return new AppTitleInfo(AppConstants.TITLE_MODE_LINEAR, null, true);
    }

    /**
     * 给外层app提供自定义标题栏的机会
     * @param titleMode
     * @return
     */
    protected IAppTitleBarView createAppTitleBarView(@AppConstants.TitleMode int titleMode) {
        return new AppDefaultTitleBarView((AppCompatActivity) mActivity, titleMode);
    }

    protected void initTitle(@NonNull AppTitleInfo titleInfo) {
        if(mAppTitleBarView != null && mAppTitleBarView instanceof AppDefaultTitleBarView) {
            mAppTitleBarView.initTitle(titleInfo);
        }
    }

}
