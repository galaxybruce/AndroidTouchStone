package com.galaxybruce.component.mvp;

import android.content.Context;

/**
 * Created by bruce.zhang
 */
public interface MvpView {

    /**
     * 隐藏加载框
     */
    void hideLoadingProgress();

    /**
     * 显示加载狂
     * @param msg
     */
    void showLoadingProgress(String msg);

    /**
     * 重新登录
     */
    void reLogin();

    /**
     * 获取Context
     * @return
     */
    Context getContext();
}
