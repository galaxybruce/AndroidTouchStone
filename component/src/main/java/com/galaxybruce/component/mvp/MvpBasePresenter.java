package com.galaxybruce.component.mvp;


import com.trello.rxlifecycle3.LifecycleProvider;

import java.lang.ref.WeakReference;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * created by bruce.zhang
 *
 * @param <V>
 */
public class MvpBasePresenter<V extends MvpView> implements MvpPresenter<V> {

    private WeakReference<V> viewRef;
    // 用来在页面退出时，取消订阅，两种方式：
    // 1.CompositeDisposable，适用于subscribe(Consumer
    // 2.通过.compose(LifecycleProvider.<String>bindToLifecycle())来取消，适用于subscribe(Observer
    // 通过构造函数把LifecycleProvider<ActivityEvent/FragmentEvent>接口传进来
    protected CompositeDisposable compositeDisposable;
    protected LifecycleProvider lifecycleProvider;

    public MvpBasePresenter() {

    }

    public MvpBasePresenter(LifecycleProvider lifecycleProvider) {
        this.lifecycleProvider = lifecycleProvider;
    }

    @Override
    public void attachView(V view) {
        viewRef = new WeakReference<V>(view);
        compositeDisposable = new CompositeDisposable();
        if (view instanceof LifecycleProvider) {
            this.lifecycleProvider = (LifecycleProvider) view;
        }
    }

    public V getView() {
        return viewRef == null ? null : viewRef.get();
    }

    public boolean isViewAttached() {
        return viewRef != null && viewRef.get() != null;
    }

    @Override
    public void detachView() {
        if (viewRef != null) {
            viewRef.clear();
            viewRef = null;
        }
        clearDisposable();
        this.lifecycleProvider = null;
    }

    protected void addDisposable(Disposable disposable) {
        if (disposable != null && compositeDisposable != null) {
            compositeDisposable.add(disposable);
        }
    }

    protected void clearDisposable() {
        if (compositeDisposable != null) {
            compositeDisposable.clear();
        }
    }

    public void hideLoadingProgress() {
        V view = getView();
        if (view != null) {
            view.hideLoadingProgress();
        }
    }

    public void showLoadingProgress() {
        V view = getView();
        if (view != null) {
            view.showLoadingProgress("");
        }
    }


    /*********************** 重新登录后的行为 start ************************/

    private Runnable mLoginTask;

    /**
     * 如果某个动作想在自动登录后，再次自动执行，需要封装成Runnable
     * 并调用该方法
     *
     * @param runnable
     */
    public void doLoginTask(Runnable runnable) {
        if (runnable != null) {
            mLoginTask = runnable;
            runnable.run();
        }
    }

    /**
     * 登录成功后，执行上次的task
     */
    public boolean doLoginTask() {
        if (mLoginTask != null) {
            Runnable runnable = mLoginTask;
            mLoginTask = null;
            runnable.run();
            return true;
        }
        return false;
    }

    public void clearLoginTask() {
        mLoginTask = null;
    }

    /*********************** 重新登录后的行为 end ************************/
}
