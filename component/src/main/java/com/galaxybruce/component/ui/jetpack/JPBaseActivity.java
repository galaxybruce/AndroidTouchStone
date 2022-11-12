package com.galaxybruce.component.ui.jetpack;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.galaxybruce.component.ui.activity.BaseActivity;

/**
 * @date 2020/8/11  10:08 AM
 * @author
 * @description jetpack mvvm
 * <p>
 *
 *  add by bruce.zhang start
 *
 *  TODO tip：
 *  链路：UI-ViewModel-Request-Repository/或者其他逻辑类
 *
 *  UI: 负责UI展示和交互
 *  ViewModel: ViewModel 的职责仅限于 状态托管，不建议在此处理 UI 逻辑，每个页面都要单独准备一个 state-ViewModel
 *  Request: 逻辑处理的入口类，同时用MutableLiveData保存处理结果状态
 *  Repository: 具体业务处理，比如网络请求
 *
 *  add by bruce.zhang end
 *
 * modification history:
 */
public abstract class JPBaseActivity<B extends ViewDataBinding> extends BaseActivity implements JPHost {

    protected B mDataBinding;
    protected JPPageDelegate<B> mJPPageDelegate;

    @Override
    public LifecycleOwner getLifecycleOwner() {
        return this;
    }

    /**
     * 每个页面对应的Page ViewModel
     *
     * 全局ViewModel获取
     * mAppViewModel = getAppViewModelProvider().get(AppViewModel.class);
     *
     * @return 返回 该页面对应的ViewModel
     */
    @Override
    public abstract JPBaseViewModel initViewModel();

    /**
     * 多个"通用的ViewModel"的初始化
     * @return
     */
    @Override
    public JPBaseViewModel[] initViewModels() {
        return null;
    }

    @Override
    public abstract JPDataBindingConfig initDataBindConfig();

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
    protected void onCreate(Bundle savedInstanceState) {
        mJPPageDelegate = new JPPageDelegate<>(this);
        super.onCreate(savedInstanceState);
        addLifecycleObserves();
    }

    @Override
    public void setRootLayout(int layoutId) {
        mDataBinding = mJPPageDelegate.setRootLayout(layoutId, null, null, false);
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

}
