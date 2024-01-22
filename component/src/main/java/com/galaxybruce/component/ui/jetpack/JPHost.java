package com.galaxybruce.component.ui.jetpack;

import android.app.Activity;

import com.galaxybruce.component.ui.ILogin;
import com.galaxybruce.component.ui.IUiView;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelStoreOwner;

/**
 * @author bruce.zhang
 * @date 2020/11/26 11:43
 * @description mvvm页面部分接口：activity、fragment、fragmentDialog来实现
 * <p>
 * modification history:
 */
public interface JPHost extends LifecycleOwner, ViewModelStoreOwner, IUiView, ILogin {

    LifecycleOwner getLifecycleOwner();

    /**
     * 每个页面对应的Page ViewModel
     * @return 返回 该页面对应的ViewModel
     */
    JPBaseViewModel initViewModel();

    /**
     * 多个"通用的ViewModel"的初始化
     * @return
     */
    JPBaseViewModel[] initViewModels();

    /**
     * databind 配置
     * @return
     */
    JPDataBindingConfig initDataBindConfig();

    /**
     * 是否出入活跃状态
     * @return
     */
    boolean isHostActive();

    /**
     * 关闭容器
     */
    void closeHost();

    Activity getHostActivity();

    @Override
    default void finishAllActivity() {

    }
}
