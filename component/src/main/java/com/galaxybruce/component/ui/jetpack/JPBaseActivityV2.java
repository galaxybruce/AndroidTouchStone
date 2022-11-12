package com.galaxybruce.component.ui.jetpack;

import android.view.LayoutInflater;
import android.view.MenuItem;

import com.galaxybruce.component.ui.activity.AppDefaultTitleBarView;
import com.galaxybruce.component.ui.activity.IAppTitleBarView;
import com.galaxybruce.component.util.AppConstants;

import java.lang.reflect.ParameterizedType;

import androidx.annotation.NonNull;
import androidx.databinding.ViewDataBinding;

public abstract class JPBaseActivityV2<B extends ViewDataBinding, VM extends JPBaseViewModel> extends JPBaseActivity<B> {

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
    public void setRootLayout(int layoutId) {
        int titleMode = getTitleMode();
        if(titleMode == AppConstants.TITLE_MODE_LINEAR || titleMode == AppConstants.TITLE_MODE_FLOAT) {
            mAppTitleBarView = createAppTitleBarView(titleMode);
            setContentView(mAppTitleBarView.getContentView());
            // 业务布局
            mDataBinding = mJPPageDelegate.setRootLayout(layoutId, LayoutInflater.from(mActivity),
                    mAppTitleBarView.getContentLayout(), true);
            initTitle();
        } else if(titleMode == AppConstants.TITLE_MODE_CUSTOM){
            super.setRootLayout(layoutId);
            initTitle();
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
        return new AppDefaultTitleBarView(this, titleMode);
    }

    protected void initTitle() {
        if(mAppTitleBarView != null && mAppTitleBarView instanceof AppDefaultTitleBarView) {
            ((AppDefaultTitleBarView)mAppTitleBarView).initTitle(bindTitle(), true);
        }
    }

    protected String bindTitle() {
        return null;
    }
}
