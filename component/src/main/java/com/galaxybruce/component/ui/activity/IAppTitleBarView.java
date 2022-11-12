package com.galaxybruce.component.ui.activity;

import android.view.ViewGroup;

/**
 * @author bruce.zhang
 * @date 2022/11/12 09:36
 * @description 标题栏
 * <p>
 * modification history:
 */
public interface IAppTitleBarView {

    /**
     * 获取包括标题栏在内的整个页面根布局
     * @return
     */
    ViewGroup getContentView();

    /**
     * 获取标题栏以外的实际内容根布局，也就是每个页面实际关注的业务部分
     * @return
     */
    ViewGroup getContentLayout();

    /**
     * 获取标题栏
     * @return
     */
    ViewGroup getTitleBarLayout();

}
