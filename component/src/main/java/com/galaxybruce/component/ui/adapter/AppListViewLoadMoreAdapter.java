package com.galaxybruce.component.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

/**
 * 具备加载更多的adapter
 * created by bruce.zhang
 */
public class AppListViewLoadMoreAdapter<T> extends AppBaseListViewAdapter<T> {

    protected static final int ITEM_VIEW_TYPE_LOAD_MORE = Integer.MAX_VALUE;
    protected static final int DEFAULT_REAL_ITEM_VIEW_TYPE = 0;

    protected int state = AdapterLoadDataState.STATE_DEFAULT;

    protected int mScreenWidth;

    public void setScreenWidth(int width) {
        mScreenWidth = width;
    }

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
            case AdapterLoadDataState.STATE_EMPTY_ITEM:
                return getDataSize();
            case AdapterLoadDataState.STATE_NETWORK_ERROR:
            case AdapterLoadDataState.STATE_NO_MORE:
                return showFooterViewOfHint() ? getDataSizePlus1() : getDataSize();
            case AdapterLoadDataState.STATE_LOAD_MORE:
                return getDataSizePlus1();
            default:
                break;
        }
        return getDataSize();
    }

    public int getDataSizePlus1() {
        if (hasFooterView()) {
            return getDataSize() + 1;
        }
        return getDataSize();
    }

    protected boolean loadMoreHasBg() {
        return false;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getCount() - 1 && getCount() > getDataSize() && hasFooterView()) {// 最后一条
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
                this.mFooterView = new ListFooterView(mContext);
            }
            mFooterView.setState(getState(), loadMoreHasBg());
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

    public boolean hasFooterView() {
        return true;
    }

    public boolean needLoadMore() {
        return true;
    }

    protected boolean showFooterViewOfHint() {//头部有数据，列表无数据时有时候需要显示特殊布局，这时返回false
        return false;
    }

    private ListFooterView mFooterView;

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
