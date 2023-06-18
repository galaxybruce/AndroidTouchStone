package com.galaxybruce.component.ui.jetpack;

import android.view.LayoutInflater;
import android.view.MenuItem;

import com.galaxybruce.component.ui.activity.AppDefaultTitleBarView;
import com.galaxybruce.component.ui.activity.AppTitleInfo;
import com.galaxybruce.component.ui.activity.IAppTitleBarView;
import com.galaxybruce.component.util.AppConstants;

import androidx.annotation.NonNull;
import androidx.databinding.ViewDataBinding;

import static com.galaxybruce.component.ui.jetpack.util.JPViewModeExtKt.getVmClazz;

public abstract class JPBaseActivityV2<VM extends JPBaseViewModel, B extends ViewDataBinding> extends JPBaseActivity<B> {

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
            mPageViewModel = getActivityViewModel(tClass);
        }
        return mPageViewModel;
    }

    @Override
    public void setRootLayout(int layoutId) {
        AppTitleInfo titleInfo = getTitleInfo();
        int titleMode = titleInfo.getTitleMode();
        if(titleMode == AppConstants.TITLE_MODE_LINEAR || titleMode == AppConstants.TITLE_MODE_FLOAT) {
            mAppTitleBarView = createAppTitleBarView(titleMode);
            setContentView(mAppTitleBarView.getContentView());
            // 业务布局
            mDataBinding = mJPPageDelegate.setRootLayout(layoutId, LayoutInflater.from(mActivity),
                    mAppTitleBarView.getContentLayout(), true);
            initTitle(titleInfo);
        } else if(titleMode == AppConstants.TITLE_MODE_CUSTOM){
            super.setRootLayout(layoutId);
            initTitle(titleInfo);
        } else {
            super.setRootLayout(layoutId);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
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
        return new AppDefaultTitleBarView(this, titleMode);
    }

    protected void initTitle(@NonNull AppTitleInfo titleInfo) {
        if(mAppTitleBarView != null && mAppTitleBarView instanceof AppDefaultTitleBarView) {
            mAppTitleBarView.initTitle(titleInfo);
        }
    }

}
