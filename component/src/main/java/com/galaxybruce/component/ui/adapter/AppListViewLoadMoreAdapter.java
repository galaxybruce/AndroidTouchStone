package com.galaxybruce.component.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;

/**
 * 具备加载更多的adapter
 * created by bruce.zhang
 */
public class AppListViewLoadMoreAdapter<T> extends AppBaseListViewAdapter<T> {

    protected static final int DEFAULT_REAL_ITEM_VIEW_TYPE = 0;
    protected static final int ITEM_VIEW_TYPE_LOAD_MORE = Integer.MAX_VALUE;

    protected int state = AppListAdapterLoadDataState.STATE_DEFAULT;

    public void setState(int state) {
        this.state = state;
    }

    public int getState() {
        return this.state;
    }

    public AppListViewLoadMoreAdapter(Context context) {
        super(context);
    }

    @Override
    public int getCount() {
        switch (getState()) {
            case AppListAdapterLoadDataState.STATE_EMPTY_ITEM:
                return getDataSize();
            case AppListAdapterLoadDataState.STATE_NETWORK_ERROR:
            case AppListAdapterLoadDataState.STATE_NO_MORE:
                return showNoMoreView() ? getDataSize() + 1 : getDataSize();
            case AppListAdapterLoadDataState.STATE_LOAD_MORE:
                return showLoadMoreView() ? getDataSize() + 1 : getDataSize();
            default:
                break;
        }
        return getDataSize();
    }

    private Drawable loadMoreBg() {
        return null;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getCount() - 1
                && getCount() > getDataSize()
                && (showLoadMoreView() || showNoMoreView())) {// 最后一条
            return ITEM_VIEW_TYPE_LOAD_MORE;
        } else {
            return getRealItemViewType(position);
        }
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @SuppressWarnings("deprecation")
    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int viewType = getItemViewType(position);
        if (viewType == ITEM_VIEW_TYPE_LOAD_MORE) {
            if (convertView == null) {
                this.mFooterView = new AppListFooterView(mContext);
            }
            mFooterView.setState(getState(), loadMoreBg());
            return mFooterView;
        } else {
            return getRealView(position, convertView, parent);
        }

    }

    protected int getRealItemViewType(int position) {
        return DEFAULT_REAL_ITEM_VIEW_TYPE;
    }

    protected View getRealView(int position, View convertView, ViewGroup parent) {
        return null;
    }

    /**
     * 是否需要分页。分页时加载更多数据时，是否显示"加载更多"View通过showLoadMoreView()控制
     *
     * @return
     */
    public boolean needLoadMore() {
        return true;
    }

    /**
     * 分页加载是否显示"加载更多"View。有的分页是滑到快到底部时，自动加载更多，则不需要显示"加载更多"View
     * @return
     */
    public boolean showLoadMoreView() {
        return true;
    }

    /**
     * 是否显示"没有更多数据"View
     *
     * @return
     */
    protected boolean showNoMoreView() {
        return true;
    }

    private AppListFooterView mFooterView;

    public View getFooterView() {
        return this.mFooterView;
    }

    public void setFooterViewLoading(String loadMsg) {
        if (mFooterView == null) return;
        mFooterView.setFooterViewLoading(loadMsg);
    }

    public void setFooterViewLoading() {
        setFooterViewLoading("");
    }

}
