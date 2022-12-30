package com.galaxybruce.component.ui.view.recyclerview;

import android.content.Context;
import android.util.AttributeSet;

import com.galaxybruce.component.R;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import androidx.annotation.NonNull;


/**
 * @date 2020/6/19 15:40
 * @author bruce.zhang
 * @description 下拉刷新组件，内部实现是SmartRefreshLayout
 * <p>
 * modification history:
 */
public class AppRecyclerView2<T> extends AbsAppRecyclerView<SmartRefreshLayout, T> implements OnRefreshListener {

    public AppRecyclerView2(Context context) {
        this(context,null);
    }

    public AppRecyclerView2(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public AppRecyclerView2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected int defaultLayout() {
        return R.layout.app_recycler_view_2;
    }

    @Override
    protected void initRefreshView() {
        mSwipeRefreshLayout = (SmartRefreshLayout)findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setEnableLoadMore(false);
        if(mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setOnRefreshListener(this);
//            BBSAttrResolveUtil.resolveAttrSwipeRefreshLayoutRes(getContext(), mSwipeRefreshLayout, R.attr.bbs_load_color);
        }
    }

    public void onRefresh(){
        onRefresh(mSwipeRefreshLayout);
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        onRefreshImpl((SmartRefreshLayout)refreshLayout);
    }

    /** 设置顶部正在加载的状态 */
    @Override
    public void setSwipeRefreshLoadingState() {
        if (mSwipeRefreshLayout != null) {
//            mSwipeRefreshLayout.finishRefresh();
            // 防止多次重复刷新
//            mSwipeRefreshLayout.setEnabled(false);
        }
    }

    /** 设置顶部加载完毕的状态 */
    @Override
    public void setSwipeRefreshLoadedState() {
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.finishRefresh();
            mSwipeRefreshLayout.finishLoadMore();
//            mSwipeRefreshLayout.setEnabled(true);
        }
    }

}
