package com.galaxybruce.component.ui.jetpack;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.galaxybruce.component.ui.activity.BaseActivity;
import com.galaxybruce.component.ui.dialog.AppDialogFragment;

import androidx.annotation.NonNull;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

/**
 * @author bruce.zhang
 * @date 2020/11/25 16:50
 * @description mvvm dialog，目前暂未使用
 * <p>
 * modification history:
 */
public abstract class JPBaseDialogFragment<B extends ViewDataBinding> extends AppDialogFragment implements JPHost {

    private B mDataBinding;
    private JPPageDelegate<B> mJPPageDelegate;

    /**
     * 是否支持mvvm方式
     * 以前很多子类不支持mvvm方式，这里采用这个方法兼容
     * @return 默认返回false
     */
    protected boolean supportMVVM() {
        return true;
    }

    @Override
    public LifecycleOwner getLifecycleOwner() {
        return getViewLifecycleOwner();
    }

    /**
     * 每个页面对应的Page ViewModel
     * @return 返回 该页面对应的ViewModel
     */
    @Override
    public JPBaseViewModel initViewModel() {
        return null;
    }

    /**
     * 多个"通用的ViewModel"的初始化
     * @return
     */
    @Override
    public JPBaseViewModel[] initViewModels() {
        return null;
    }

    @Override
    public JPDataBindingConfig initDataBindConfig() {
        return null;
    }

    /**
     * 添加Lifecycle监听
     */
    protected void addLifecycleObserves(){}

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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        addLifecycleObserves();
        return rootView;
    }

    @Override
    protected View setRootLayout(int layoutId, @NonNull LayoutInflater inflater, ViewGroup container) {
        if(supportMVVM()) {
            mJPPageDelegate = new JPPageDelegate<>(this);
            mDataBinding = mJPPageDelegate.setRootLayout(layoutId, inflater, container, false);
            if(mDataBinding != null) {
                return mDataBinding.getRoot();
            }
            return null;
        } else {
            return super.setRootLayout(layoutId, inflater, container);
        }
    }

    protected <T extends JPBaseViewModel> T getFragmentViewModel(@NonNull Class<T> modelClass) {
        return mJPPageDelegate.getFragmentViewModel(modelClass);
    }

    protected <T extends JPBaseViewModel> T getParentFragmentViewModel(@NonNull Fragment parentFragment,
                                                                       @NonNull Class<T> modelClass) {
        return mJPPageDelegate.getParentFragmentViewModel(parentFragment, modelClass);
    }

    protected <T extends JPBaseViewModel> T getActivityViewModel(@NonNull Class<T> modelClass) {
        return mJPPageDelegate.getActivityViewModel(modelClass);
    }

    protected <T extends ViewModel> T getAppViewModel(@NonNull Class<T> modelClass) {
        return mJPPageDelegate.getAppViewModel(modelClass);
    }

    protected ViewModelProvider getAppViewModelProvider(){
        return mJPPageDelegate.getAppViewModelProvider();
    }

    /**
     * 给LiveData设置监听
     * 只所以写这个方法，主要是activity和fragment中写法不一样，原因可以参考Fragment.getViewLifecycleOwner()方法的注释
     * 在activity中，LifecycleOwner owner参数传this
     * 在fragment中，LifecycleOwner owner参数传getViewLifecycleOwner()
     *
     * @param liveData
     * @param observer
     * @param <T>
     */
    protected <T> void setLiveDataObserver(@NonNull LiveData<T> liveData, @NonNull Observer<? super T> observer) {
        mJPPageDelegate.setLiveDataObserver(liveData, observer);
    }

    @Override
    public boolean isHostActive() {
        Activity activity = getActivity();
        return activity != null && !activity.isFinishing() && activity instanceof BaseActivity;
    }

    @Override
    public void closeHost() {
        Activity activity = getActivity();
        if(activity != null) {
            activity.finish();
        }
    }

    @Override
    public Activity getHostActivity() {
        return getActivity();
    }

}
