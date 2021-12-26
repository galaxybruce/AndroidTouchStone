package com.galaxybruce.component.ui.presentation;

import android.app.Application;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.galaxybruce.component.app.BaseApplication;
import com.galaxybruce.component.ui.jetpack.JPBaseRequest;
import com.galaxybruce.component.ui.jetpack.JPBaseViewModel;
import com.galaxybruce.component.ui.jetpack.JPDataBindingConfig;
import com.galaxybruce.component.ui.jetpack.JPHost;
import com.galaxybruce.component.ui.jetpack.JPPageActionEvent;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

/**
 * @author bruce.zhang
 * @date 2020/11/26 11:29
 * @description mvvm页面部分具体实现，可以供activity、fragment、fragmentDialog使用
 * <p>
 * modification history:
 */
public class JPPresentationDelegate<B extends ViewDataBinding> {

    private JPHost mJPHost;
    private JPPresentation mPresentation;

    private ViewModelProvider mAppViewModelProvider;
    private ViewModelProvider mPresentationModelProvider;
    private ViewModelProvider.Factory mFactory;

    public JPPresentationDelegate(JPHost host) {
        mJPHost = host;
        mPresentation = (JPPresentation)host;
    }

    /**
     * 页面上loading  relogin  toast等事件
     */
    private final Observer<JPPageActionEvent> jpPageActionObserver = jpPageActionEvent -> {
        if(mPresentation == null || !mPresentation.isShowing()) {
            return;
        }
        // todo
        switch (jpPageActionEvent.getAction()) {
            case JPPageActionEvent.SHOW_LOADING_DIALOG:
                mPresentation.showLoading();
                break;
            case JPPageActionEvent.DISMISS_LOADING_DIALOG:
                mPresentation.hideLoading();
                break;
            case JPPageActionEvent.SHOW_TOAST:
                mPresentation.showToast(jpPageActionEvent.getMessage());
                break;
            case JPPageActionEvent.LOGIN:
                // todo 应该没有该场景，副屏不可能让会员登录
                break;
            case JPPageActionEvent.FINISH:
                mPresentation.dismiss();
                break;
            default:
                break;
        }
    };

    public B setRootLayout(int layoutId, LayoutInflater inflater, ViewGroup container) {
        JPBaseViewModel pageViewModel = mJPHost.initViewModel();
        JPBaseViewModel[] viewModels = mJPHost.initViewModels();
        observePageAction(pageViewModel);
        if(viewModels != null && viewModels.length > 0) {
            for (JPBaseViewModel viewModel : viewModels) {
                observePageAction(viewModel);
            }
        }

        if(layoutId > 0) {
            final JPDataBindingConfig dataBindingConfig = mJPHost.initDataBindConfig();
            if(dataBindingConfig != null) {
                ViewDataBinding binding;
                binding = DataBindingUtil.inflate(inflater, dataBindingConfig.getLayout(), container, false);
                binding.setLifecycleOwner(mPresentation);
                SparseArray<Object> bindingParams = dataBindingConfig.getBindingParams();
                for (int i = 0; i < bindingParams.size(); i++) {
                    binding.setVariable(bindingParams.keyAt(i), bindingParams.valueAt(i));
                }
                return (B)binding;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    private void observePageAction(JPBaseViewModel viewModel) {
        if(viewModel != null) {
            List<JPBaseRequest> requestList = viewModel.getRequests();
            if (requestList != null) {
                for (JPBaseRequest jpBaseRequest : requestList) {
                    if (jpBaseRequest != null) {
                        LifecycleOwner owner = getLifecycleOwner();
                        jpBaseRequest.getActionLiveData().observe(owner, jpPageActionObserver);
                    }
                }
            }
        }
    }

    protected <T extends JPBaseViewModel> T getPresentationModel(@NonNull Class<T> modelClass) {
        if (mPresentationModelProvider == null) {
            mPresentationModelProvider = new ViewModelProvider(mPresentation);
        }
        return mPresentationModelProvider.get(modelClass);
    }

    protected <T extends ViewModel> T getAppViewModel(@NonNull Class<T> modelClass) {
        if (mAppViewModelProvider == null) {
            mAppViewModelProvider = getAppViewModelProvider();
        }
        return mAppViewModelProvider.get(modelClass);
    }

    protected ViewModelProvider getAppViewModelProvider(){
        return new ViewModelProvider((BaseApplication)mPresentation.getContext().getApplicationContext()
                , getAppFactory());
    }

    protected ViewModelProvider.Factory getAppFactory(){
        Application application = BaseApplication.Companion.getInstance();
        if (mFactory == null) {
            mFactory = ViewModelProvider.AndroidViewModelFactory.getInstance(application);
        }
        return mFactory;
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
        liveData.observe(getLifecycleOwner(), observer);
    }

    @NotNull
    private LifecycleOwner getLifecycleOwner() {
        return mPresentation;
    }

}
