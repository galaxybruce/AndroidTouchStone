package com.galaxybruce.component.ui.view.recyclerview;

import android.content.Context;
import android.util.AttributeSet;

import com.galaxybruce.component.R;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

/**
 * @date 2020/6/19 15:40
 * @author bruce.zhang
 * @description 下拉刷新组件，内部实现是SwipeRefreshLayout
 * <p>
 * modification history:
 */
public class AppRecyclerView<T> extends AbsAppRecyclerView<SwipeRefreshLayout, T>
        implements SwipeRefreshLayout.OnRefreshListener {

    public AppRecyclerView(Context context) {
        this(context,null);
    }

    public AppRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public AppRecyclerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected int defaultLayout() {
        return R.layout.app_recycler_view;
    }

    @Override
    protected void initRefreshView() {
        mSwipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipe_refresh_layout);
        if(mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setOnRefreshListener(this);
//            BBSAttrResolveUtil.resolveAttrSwipeRefreshLayoutRes(getContext(), mSwipeRefreshLayout, R.attr.bbs_load_color);
            mSwipeRefreshLayout.setColorSchemeResources(R.color.theme_refresh_color);
        }
    }

    @Override
    public void onRefresh() {
        onRefreshImpl(mSwipeRefreshLayout);
    }

    /** 设置顶部正在加载的状态 */
    @Override
    public void setSwipeRefreshLoadingState() {
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setRefreshing(true);
            // 防止多次重复刷新
//            mSwipeRefreshLayout.setEnabled(false);
        }
    }

    /** 设置顶部加载完毕的状态 */
    @Override
    public void setSwipeRefreshLoadedState() {
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setRefreshing(false);
//            mSwipeRefreshLayout.setEnabled(true);
        }
    }


}
