package com.galaxybruce.component.ui.jetpack;

import android.app.Activity;
import android.app.Application;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.galaxybruce.component.app.BaseApplication;
import com.galaxybruce.component.ui.activity.BaseActivity;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
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
public class JPPageDelegate<B extends ViewDataBinding> {

    private JPHost mJPHost;
    private final boolean mHostActivity;

    private ViewModelProvider mAppViewModelProvider;
    private ViewModelProvider mActivityViewModelProvider;
    private ViewModelProvider mFragmentViewModelProvider;
    /**
     * 父fragment的provider，可能有很多层级，根据fragment类名区分
     */
    private Map<String, ViewModelProvider> mParentFragmentViewModelProviderMap;
    private ViewModelProvider.Factory mFactory;

    public JPPageDelegate(JPHost host) {
        mJPHost = host;
        if(host instanceof BaseActivity) {
            mHostActivity = true;
        } else if(host instanceof Fragment) {
            mHostActivity = false;
        } else {
            throw new IllegalArgumentException("init mvvm param host must be activity or fragment !!!");
        }
    }

    /**
     * 页面上loading  relogin  toast等事件
     */
    private final Observer<JPPageActionEvent> jpPageActionObserver = jpPageActionEvent -> {
        BaseActivity activity = null;
        if(mJPHost instanceof BaseActivity) {
            activity = (BaseActivity)mJPHost;
        }
        if(activity == null || activity.isFinishing()) {
            return;
        }
        switch (jpPageActionEvent.getAction()) {
            case JPPageActionEvent.SHOW_LOADING_DIALOG:
                activity.showLoadingProgress(jpPageActionEvent.getMessage());
                break;
            case JPPageActionEvent.DISMISS_LOADING_DIALOG:
                activity.hideLoadingProgress();
                break;
            case JPPageActionEvent.SHOW_TOAST:
                activity.showToast(jpPageActionEvent.getMessage());
                break;
            case JPPageActionEvent.LOGIN:
                activity.login();
                break;
            case JPPageActionEvent.FINISH:
                activity.finish();
                break;
            default:
                break;
        }
    };

    public B setRootLayout(int layoutId, LayoutInflater inflater, ViewGroup container,
                           boolean attachToParent) {
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
                if(mHostActivity && !attachToParent) {
                    binding = DataBindingUtil.setContentView((AppCompatActivity)mJPHost, dataBindingConfig.getLayout());
                } else {
                    binding = DataBindingUtil.inflate(inflater, dataBindingConfig.getLayout(), container, attachToParent);
                }
                binding.setLifecycleOwner(mJPHost);
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
                        // JpBaseRequest实现了DefaultLifecycleObserver
                        owner.getLifecycle().addObserver(jpBaseRequest);
                        jpBaseRequest.getActionLiveData().observe(owner, jpPageActionObserver);
                    }
                }
            }
        }
    }

    protected <T extends JPBaseViewModel> T getFragmentViewModel(@NonNull Class<T> modelClass) {
        if (mFragmentViewModelProvider == null) {
            mFragmentViewModelProvider = new ViewModelProvider(mJPHost);
        }
        return mFragmentViewModelProvider.get(modelClass);
    }

    /**
     * 父fragment的provider，可能有很多层级，根据fragment类名区分
     * 使用场景就是一个复杂的页面，activity只是一个壳子，里面有很多层fragment，
     * 业务都在fragment中，如果把ViewModel强行放在activity中会导致数据隔离，不好处理
     *
     * 使用方式：mParentViewModel = getParentFragmentViewModel(getParentFragment(), xxx.class);
     *
     * @param parentFragment
     * @param modelClass
     * @param <T>
     * @return
     */
    protected <T extends JPBaseViewModel> T getParentFragmentViewModel(@NonNull Fragment parentFragment,
                                                                       @NonNull Class<T> modelClass) {
        if(mParentFragmentViewModelProviderMap == null) {
            mParentFragmentViewModelProviderMap = new HashMap<>(3);
        }
        ViewModelProvider provider = mParentFragmentViewModelProviderMap.get(parentFragment.getClass().getName());
        if (provider == null) {
            provider = new ViewModelProvider(parentFragment);
            mParentFragmentViewModelProviderMap.put(parentFragment.getClass().getName(), provider);
        }
        return provider.get(modelClass);
    }

    protected <T extends JPBaseViewModel> T getActivityViewModel(@NonNull Class<T> modelClass) {
        if (mActivityViewModelProvider == null) {
            mActivityViewModelProvider = new ViewModelProvider(mJPHost);
        }
        return mActivityViewModelProvider.get(modelClass);
    }

    protected <T extends ViewModel> T getAppViewModel(@NonNull Class<T> modelClass) {
        if (mAppViewModelProvider == null) {
            mAppViewModelProvider = getAppViewModelProvider();
        }
        return mAppViewModelProvider.get(modelClass);
    }

    protected ViewModelProvider getAppViewModelProvider(){
        Activity activity;
        if(mHostActivity) {
            activity = (Activity) mJPHost;
        } else {
            activity = ((Fragment)mJPHost).getActivity();
            if (activity == null) {
                throw new IllegalStateException("Can't create ViewModelProvider for detached fragment");
            }
        }
        return new ViewModelProvider((BaseApplication)activity.getApplicationContext()
                , getAppFactory(activity));
    }

    private ViewModelProvider.Factory getAppFactory(Activity activity){
        Application application = checkApplication(activity);
        if (mFactory == null) {
            mFactory = ViewModelProvider.AndroidViewModelFactory.getInstance(application);
        }
        return mFactory;
    }

    private Application checkApplication(Activity activity) {
        Application application = activity.getApplication();
        if (application == null) {
            throw new IllegalStateException("Your activity/fragment is not yet attached to "
                    + "Application. You can't request ViewModel before onCreate call.");
        }
        return application;
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
        return mJPHost.getLifecycleOwner();
    }

}
