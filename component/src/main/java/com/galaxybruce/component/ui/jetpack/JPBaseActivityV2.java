package com.galaxybruce.component.ui.jetpack;

import java.lang.reflect.ParameterizedType;

import androidx.databinding.ViewDataBinding;

public abstract class JPBaseActivityV2<B extends ViewDataBinding, VM extends JPBaseViewModel> extends JPBaseActivity<B> {

    protected VM mPageViewModel;

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

}
