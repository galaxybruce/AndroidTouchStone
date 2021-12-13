package com.galaxybruce.component.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.collection.SparseArrayCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

/**
 * 具备加载更多的adapter
 * created by bruce.zhang
 */
public class AppRecyclerLoadMoreAdapter<T> extends AppBaseRecyclerAdapter<T> {

    protected static final int ITEM_VIEW_TYPE_LOAD_MORE = Integer.MAX_VALUE;      //底部加载更多viewType
    protected static final int DEFAULT_REAL_ITEM_VIEW_TYPE = 0;                    //默认的正常的viewType
    private static final int ITEM_VIEW_TYPE_HEADER = 200000;                        //头部viewType从200000开始

    private SparseArrayCompat<View> mHeaderViews = new SparseArrayCompat<>();
    private ListFooterView mFooterView;

    protected int state = AdapterLoadDataState.STATE_DEFAULT;

    public void setState(int state) {
        this.state = state;
    }

    public int getState() {
        return this.state;
    }

    public AppRecyclerLoadMoreAdapter(Context context) {
        super(context);
    }

    @Override
    public int getItemCount() {
        int count = 0;
        switch (getState()) {
            case AdapterLoadDataState.STATE_EMPTY_ITEM:
                count = getDataSize();
                break;
            case AdapterLoadDataState.STATE_NETWORK_ERROR:
            case AdapterLoadDataState.STATE_NO_MORE:
                count = showFooterViewOfHint() ? getDataSizePlus1() : getDataSize();
                break;
            case AdapterLoadDataState.STATE_LOAD_MORE:
                count = getDataSizePlus1();
                break;
            default:
                count = getDataSize();
                break;
        }
        return count + getHeaderViewCount();
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
        if (isHeaderView(position)) {
            return mHeaderViews.keyAt(position);
        } else if (isFooterView(position)) {// 最后一条
            return ITEM_VIEW_TYPE_LOAD_MORE;
        } else {
            return getRealItemViewType(position);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if (mHeaderViews.get(viewType) != null) {
            return RecyclerViewHolder.createViewHolder(viewGroup.getContext(), mHeaderViews.get(viewType));
        } else if (viewType == ITEM_VIEW_TYPE_LOAD_MORE) {
            mFooterView = new ListFooterView(mContext);
            return new FooterHolder(mFooterView);
        } else {
            return onCreateRealViewHolder(viewGroup, viewType);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (isHeaderView(position)) {
            return;
        } else if (holder instanceof FooterHolder) {
            final FooterHolder footerHolder = (FooterHolder) holder;
            footerHolder.footerView.setState(getState(), loadMoreHasBg());
        } else {
            int dataPosition = translateRealDataPosition(position);
            onBindRealViewHolder(holder, dataPosition);
        }
    }

    public View getFooterView() {
        return this.mFooterView;
    }

    public void setFooterViewLoading(String loadMsg) {
        if (mFooterView == null) {
            return;
        }
        mFooterView.setFooterViewLoading(loadMsg);
    }

    public void setFooterViewLoading() {
        setFooterViewLoading("");
    }

    protected boolean isHeaderView(int position) {
        return position < mHeaderViews.size();
    }

    protected boolean isFooterView(int position) {
        return position == getItemCount() - 1 &&
                getItemCount() > (getDataSize() + getHeaderViewCount()) &&
                hasFooterView();
    }

    public void addHeaderView(View view) {
        mHeaderViews.put(mHeaderViews.size() + ITEM_VIEW_TYPE_HEADER, view);
    }

    /**
     * 转化为真正的数据mDatas中的位置，需要去除头部数量
     *
     * @param position adapter中的位置
     * @return
     */
    public int translateRealDataPosition(int position) {
        return position - getHeaderViewCount();
    }

    /**
     * 转化为adapter中的位置，需要加上头部数量
     *
     * @param dataPosition mDatas中的位置
     * @return
     */
    public int translateAdapterDataPosition(int dataPosition) {
        return dataPosition + getHeaderViewCount();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            final GridLayoutManager gridManager = ((GridLayoutManager) manager);
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return (isHeaderView(position) || isFooterView(position))
                            ? gridManager.getSpanCount() : 1;
                }
            });
        }

        if (manager instanceof LinearLayoutManager) {
            ((LinearLayoutManager) manager).setRecycleChildrenOnDetach(true);
        }
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        handleHeaderFooterLayout(holder);
    }

    private void handleHeaderFooterLayout(RecyclerView.ViewHolder holder) {
        if (isStaggeredGridLayout(holder)) {
            if (isHeaderView(holder.getLayoutPosition()) || isFooterView(holder.getLayoutPosition())) {
                StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) holder.itemView.getLayoutParams();
                p.setFullSpan(true);
            }
        }
    }

    protected boolean isStaggeredGridLayout(RecyclerView.ViewHolder holder) {
        ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
        if (layoutParams != null && layoutParams instanceof StaggeredGridLayoutManager.LayoutParams) {
            return true;
        }
        return false;
    }

    protected static class FooterHolder extends RecyclerView.ViewHolder {

        public ListFooterView footerView;

        public FooterHolder(View view) {
            super(view);
            footerView = (ListFooterView) view;
        }
    }


    /***************************************子类可以重写的部分start***************************************/

    /**
     * 除开头和尾部，其他的viewtype；如果头部全部用addHeaderView方法，这个方法不用重写，DEFAULT_REAL_ITEM_VIEW_TYPE就是默认列表的的type
     *
     * @param position
     * @return
     */
    protected int getRealItemViewType(int position) {
        return DEFAULT_REAL_ITEM_VIEW_TYPE;
    }

    public RecyclerView.ViewHolder onCreateRealViewHolder(ViewGroup viewGroup, int viewType) {
        // 返回一个空的RecyclerView.ViewHolder，防止子类直接super.onCreateRealViewHolder，导致RecyclerView内部空指针bug
        return new RecyclerView.ViewHolder(new FrameLayout(mContext)) {
        };
    }

    public void onBindRealViewHolder(RecyclerView.ViewHolder holder, int dataPosition) {

    }

    /**
     * 是否需要分页
     *
     * @return
     */
    public boolean needLoadMore() {
        return true;
    }

    public boolean hasFooterView() {
        return true;
    }

    /**
     * 头部有数据，列表无数据时有时候需要显示特殊布局，这时返回false
     *
     * @return
     */
    protected boolean showFooterViewOfHint() {
        return true;
    }

    /**
     * 如果头部不是全部用addHeaderView方法添加的，子类中应该是mHeaderViews.size() + 另外在onCreateRealViewHolder方法中new的个数
     *
     * @return
     */
    @Override
    public int getHeaderViewCount() {
        return mHeaderViews.size();
    }

    /***************************************子类可以重写的部分end***************************************/
}

