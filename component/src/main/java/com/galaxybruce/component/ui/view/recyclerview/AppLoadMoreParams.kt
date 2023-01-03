package com.galaxybruce.component.ui.view.recyclerview

import android.graphics.drawable.Drawable

/**
 * @date 2023/1/3 14:03
 * @author bruce.zhang
 * @description 上拉加载更多参数
 *
 * modification history:
 */
data class AppLoadMoreParams(
    /**
     * 是否需要分页。分页时加载更多数据时，是否显示"加载更多"View通过showLoadMoreView()控制
     */
    var needLoadMore: Boolean = true,
    /**
     * 分页加载是否显示"加载更多"View。有的分页是滑到快到底部时，自动加载更多，则不需要显示"加载更多"View
     */
    var showLoadMoreView: Boolean = true,
    /**
     * 是否显示"没有更多数据"View
     */
    var showNoMoreView: Boolean = true,
    /**
     * 加载更多view背景
     */
    var loadMoreViewBg: Drawable? = null,
)
