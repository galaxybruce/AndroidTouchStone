package com.galaxybruce.component.mvp;

import com.galaxybruce.component.util.exception.AppException;
import com.galaxybruce.component.net.ResponseStatus;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * 解决自动登录和登录成功后自动处理上次逻辑
 * 在发送请求之前把请求封装成Runnable对象，然后调用BBSBaseFragment.doLoginTask或者BBSBaseActivity.doLoginTask
 *
 * 注意：必须结合MVP使用，这里用到Presenter中的自动登录以及登录后重新发送请求
 * <p>
 * Created by bruce.zhang on 2015/10/14.
 *
 * 可以进一步改进，参考这篇文章《Retrofit+RxJava 优雅的处理服务器返回异常、错误》
 * https://blog.csdn.net/jdsjlzx/article/details/51882661
 */
public abstract class MVPRequestObserver<T> implements Observer<T> {

    Disposable disposable;

    @Override
    public void onSubscribe(Disposable d) {
        disposable = d;
        if(showLoadingDialog()) {
            if(getPresenter() != null) {
                getPresenter().showLoadingProgress();
            }
        }
    }

    @Override
    public void onComplete() {
        dispose();
        hideLoadingDialog();
    }

    private void dispose() {
        if(disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }

    @Override
    public final void onNext(T t) {
        if((t instanceof ResponseStatus) && getPresenter() != null) {
            ResponseStatus responseStatus = (ResponseStatus) t;
            MvpView view = getPresenter().getView();
            if (view != null) {
                if (responseStatus.reLogin()) {
                    // todo 这里可能和LoginExpiresEvent事件重复
                    hideLoadingDialog();
                    view.reLogin();
                    onFinalError(new AppException(responseStatus.getMessage(), responseStatus.getCode()));
                    dispose();
                    return;
                }
            }

            getPresenter().clearLoginTask();
            if (responseStatus.success()) {
                onFinalNext(t);
            } else {
                onError(new AppException(responseStatus.getMessage(), responseStatus.getCode()));
            }
        } else {
            onFinalNext(t);
        }
    }

    @Override
    public final void onError(Throwable e) {
        if(getPresenter() != null) {
            getPresenter().clearLoginTask();
        }
        dispose();
        if(e instanceof AppException) {
            onFinalError((AppException)e);
        } else {
            onFinalError(new AppException(e.getMessage()));
        }
        hideLoadingDialog();
    }

    private void hideLoadingDialog() {
        if(showLoadingDialog()) {
            if(getPresenter() != null) {
                getPresenter().hideLoadingProgress();
            }
        }
    }

    public boolean showLoadingDialog() {
        return false;
    }

    public abstract void onFinalNext(T t);

    public abstract void onFinalError(AppException e);

    public abstract MvpBasePresenter getPresenter();

}
