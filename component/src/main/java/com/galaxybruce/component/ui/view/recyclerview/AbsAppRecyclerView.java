package com.galaxybruce.component.ui.view.recyclerview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.galaxybruce.component.R;
import com.galaxybruce.component.ui.adapter.AppListAdapterLoadDataState;
import com.galaxybruce.component.ui.adapter.AppRecyclerLoadMoreAdapter;
import com.galaxybruce.component.ui.view.AppEmptyLayout;
import com.galaxybruce.component.ui.view.AppBackToTopView;
import com.galaxybruce.component.util.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


/**
 * @date 2018/12/20 15:50
 * @author
 * @description 列表组件
 * <p>
 *
 *  使用方式
 *    appRecyclerView.setAdapter(new BBSCoursePlayRecordAdapter(mContext))
 *         .showBack2TopView(5)
 *         .setRequestDataIfViewCreated(false)
 *         .setNeedShowEmptyNoData(true)
 *         .setRequestListener(new AppRecyclerView.AppRequestListener() {
 *             @Override
 *             public void sendRequestData(boolean refresh) {
 *                 BBSCoursePlayRecordActivity.this.sendRequestData();
 *             }
 *             @Override
 *             public void sendRequestLoadMoreData() {
 *             }
 *         })
 *         .create();
 *
 * 注意：使用该组件后，因为涉及到组件中的各种状态，所以发送请求不能直接调用发送接口，需要通过下面方式触发。
 * 1. 下拉或者上拉触发接口
 * 2. 调用方法：requestDataWithLoading()
 * 3. 调用方法：requestDataWithoutLoading()
 *
 * 对于一个列表有多个接口的情况，可以强制加载更多。参考 {@link AbsAppRecyclerView#forceRequestMore()}
 *
 * modification history:
 */
public abstract class AbsAppRecyclerView<V extends ViewGroup, T> extends RelativeLayout
        implements IAppRecyclerView<T> {

    public int mState = AppLoadDataState.STATE_NONE;
    protected int mCurrentPage = 0;
    protected int mPageSize = 10;

    protected V mSwipeRefreshLayout;

    protected RecyclerView mRecyclerView;
    protected RecyclerView.LayoutManager layoutManager;
    protected AppRecyclerLoadMoreAdapter<T> mAdapter;
    protected AppEmptyLayout mErrorLayout;

    boolean requestDataIfViewCreated = true;
    boolean isTimeRefresh = false;
    boolean needShowEmptyNoData = true;
    /**
     * 是否显示loadingView，默认显示；有可能外面想自己显示loading dialog，这里可以设置成false
     */
    boolean needShowLoadingView = true;
    boolean pullRefreshEnable = true;
    /**
     * 无数据时，是否可以点击中间的按钮或者图标发送请求
     */
    boolean clickRefreshEnable = true;
    int back2TopViewVisiblePosition;

    int mDefaultPage;
    Rect mRecyclerViewPaddingRect;
    List<RecyclerView.ItemDecoration> mItemDecorationList = new ArrayList<>();

    AppLoadMoreParams loadMoreParams;
    AppRequestListener requestListener;
    AppRecyclerViewExecuteListener<T> executeListener;


    public AbsAppRecyclerView(Context context) {
        this(context,null);
    }

    public AbsAppRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public AbsAppRecyclerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    private void initView(Context context,AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.app_recycler_view);
        int layoutResId = a.getResourceId(R.styleable.app_recycler_view_layout_res_id, defaultLayout());
        a.recycle();

        LayoutInflater.from(getContext()).inflate(layoutResId, this, true);

        initRefreshView();

        mErrorLayout = (AppEmptyLayout)findViewById(R.id.error_layout);
        mRecyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        mRecyclerView.addOnScrollListener(mOnScrollListener);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator() {
            @Override
            public boolean canReuseUpdatedViewHolder(@NonNull RecyclerView.ViewHolder viewHolder) {
                return true;
            }
        });

        // 外面设置的padding传递到RecyclerView
        mRecyclerView.setPadding(getPaddingLeft(), getPaddingTop(), getPaddingRight(), getPaddingBottom());
        setPadding(0, 0, 0, 0);

        if (mErrorLayout != null) {
            mErrorLayout.setOnLayoutClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(clickRefreshEnable) {
                        requestData(needShowLoadingView);
                    }
                }
            });
        }
    }

    /**
     * 注意：这个方法必须在BBSActivity.bindData()或者BBSFragment.bindData()中调用
     * 这里处理了未登录的情况
     */
    public void create() {
        if(mAdapter == null){
            ToastUtils.showToast(getContext().getApplicationContext(),"必须实现Adapter");
            return;
        }
        if(loadMoreParams == null) {
            loadMoreParams = new AppLoadMoreParams() {
                @Override
                public boolean needLoadMore() {
                    return AppLoadMoreParams.super.needLoadMore();
                }

                @Override
                public boolean showLoadMoreView() {
                    return AppLoadMoreParams.super.showLoadMoreView();
                }

                @Override
                public boolean showNoMoreView() {
                    return AppLoadMoreParams.super.showNoMoreView();
                }
            };
        }
        if(requestListener == null){
            requestListener = new AppRequestListener() {
                @Override
                public void sendRequestData(boolean refresh, int page) {
                }

                @Override
                public void sendRequestLoadMoreData(int page) {
                }
            };
        }
        if(executeListener == null){
            executeListener = new AppRecyclerViewExecuteListenerImpl<>(this);
        }
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setEnabled(pullRefreshEnable);
        }

        if (mRecyclerViewPaddingRect != null) {
            mRecyclerView.setPadding(mRecyclerViewPaddingRect.left, mRecyclerViewPaddingRect.top,
                    mRecyclerViewPaddingRect.right, mRecyclerViewPaddingRect.bottom);
        }

        if (!mItemDecorationList.isEmpty()) {
            for (RecyclerView.ItemDecoration itemDecoration : mItemDecorationList) {
                mRecyclerView.addItemDecoration(itemDecoration);
            }
        }

        mAdapter.setLoadMoreParams(loadMoreParams);
        mRecyclerView.setLayoutManager(layoutManager == null ? new LinearLayoutManager(getContext()) : layoutManager);
        mRecyclerView.setAdapter(mAdapter);

        if(back2TopViewVisiblePosition > 0) {
            AppBackToTopView backToTop = (AppBackToTopView) findViewById(R.id.back_to_top);
            if (backToTop != null) {
                backToTop.setRecyclerView(mRecyclerView, back2TopViewVisiblePosition);
            }
        }

        if (requestDataIfViewCreated) {
            requestData(needShowLoadingView);
        } else {
            hideLoading();
        }
    }

    private void setErrorLayoutType(@AppEmptyLayout.AppEmptyLayoutState int state){
        if (mErrorLayout != null) {
            mErrorLayout.setErrorType(state);
        }
    }

    private void requestData(boolean showLoading) {
        if(mErrorLayout != null) {
            if (mState == AppLoadDataState.STATE_REFRESH) {
                return;
            }

            mCurrentPage = mDefaultPage;
            mState = AppLoadDataState.STATE_REFRESH;
            if(showLoading) {
                showLoading();
            } else {
                hideLoading();
            }
            requestListener.sendRequestData(false, getCurrentPage());
        }
    }

    /**
     * 不用显示下拉刷新状态，中间转圈
     */
    public void requestDataWithLoading() {
        requestData(true);
    }

    /**
     * 不用显示下拉刷新状态，也不显示中间转圈，只触发加载回调
     */
    public void requestDataWithoutLoading() {
        requestData(false);
    }

    /**
     * 强制加载更多
     * 注意：接口失败后一定要执行 mPageViewModel.listData.value = JPListData(null, true)
     *
     * 例如：有的页面不是标准的一个接口，可能是几个接口合成的列表，先请求上面部分接口，然后再请求最下面的接口。
     * 这种场景就可以在上面的接口请求回来，再调用该方法强制接在更多，这样页面会立马在最下面展示"正在加载中"样式。
     * .setRequestListener(new AppRecyclerView.AppRequestListener() {
     *     @Override
     *     public void sendRequestData(boolean refresh) {
     *          // 这里是上面的接口请求
     *     }
     *     @Override
     *     public void sendRequestLoadMoreData() {
     *          // 这里是下面的接口请求
     *     }
     * })
     *
     * setLiveDataObserver(mPageViewModel.listDataTop) {
     *     mPageViewModel.listData.value = it
     *     binding.appRecyclerView.forceRequestMore()
     * }
     */
    public void forceRequestMore() {
        // 这里必须是post，因为mPageViewModel.listData.value = it内部是通过队列机制执行的，
        // 必须按照顺序设置adapter的状态
        postDelayed(new Runnable() {
            @Override
            public void run() {
                // 强制加载跟过时，时当前页是第一页
                mCurrentPage = mDefaultPage;
                mState = AppLoadDataState.STATE_LOAD_MORE;
                requestListener.sendRequestLoadMoreData(getCurrentPage());
                mAdapter.setState(AppListAdapterLoadDataState.STATE_FORCE_LOAD_MORE);
                if(loadMoreParams.showLoadMoreView() || loadMoreParams.showNoMoreView()) {
                    if(mAdapter.isFooterView(mAdapter.getItemCount() - 1)) {
                        mAdapter.notifyItemChanged(mAdapter.getItemCount() - 1);
                    } else {
                        mAdapter.notifyDataSetChanged();
                    }
                    mAdapter.setFooterViewLoading();
                }
            }
        }, 10);
    }

    public RecyclerView.OnScrollListener mOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (mAdapter == null || mAdapter.getItemCount() == 0 || !loadMoreParams.needLoadMore()) {
                return;
            }
            // 数据已经全部加载，或数据为空时，或正在加载，不处理滚动事件
            if (mState == AppLoadDataState.STATE_LOAD_MORE || mState == AppLoadDataState.STATE_REFRESH) {
                return;
            }
            // 判断是否滚动到底部
            boolean scrollEnd = false;
            try {
                int lastVisibleItem = ItemsPositionHelper.getLastVisiblePosition(recyclerView);
                if(loadMoreParams.showLoadMoreView()) {
                    if (recyclerView.getChildAdapterPosition(mAdapter.getFooterView()) == lastVisibleItem) {
                        scrollEnd = true;
                    }
                } else {
                    if (mAdapter.getItemCount() - 3 >= 0 && lastVisibleItem > mAdapter.getItemCount() - 3) {
                        scrollEnd = true;
                    }
                }
            } catch (Exception e) {
                scrollEnd = false;
            }

            if (mState == AppLoadDataState.STATE_NONE && scrollEnd) {
                if (mAdapter.getState() == AppListAdapterLoadDataState.STATE_LOAD_MORE
                        || mAdapter.getState() == AppListAdapterLoadDataState.STATE_NETWORK_ERROR) {
                    mCurrentPage++;
                    mState = AppLoadDataState.STATE_LOAD_MORE;
                    requestListener.sendRequestLoadMoreData(getCurrentPage());
                    mAdapter.setFooterViewLoading();
                }
            }
        }
    };

    public void onRefreshImpl(V refreshView) {
        if (mState == AppLoadDataState.STATE_REFRESH) {
            return;
        }
        // 设置顶部正在刷新
        mRecyclerView.scrollToPosition(0);
        setSwipeRefreshLoadingState();
        mCurrentPage = mDefaultPage;
        mState = AppLoadDataState.STATE_REFRESH;

        if(requestListener != null) {
            requestListener.sendRequestData(true, getCurrentPage());
        }
    }

    public void notifyAdapterDataSetChanged() {
        if(mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
    }

    public V getSwipeRefreshLayout() {
        return mSwipeRefreshLayout;
    }

    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    @Override
    public AppEmptyLayout getEmptyLayout() {
        return mErrorLayout;
    }

    public void showLoading(){
        setErrorLayoutType(AppEmptyLayout.NETWORK_LOADING);
    }

    public void hideLoading(){
        setErrorLayoutType(AppEmptyLayout.HIDE_LAYOUT);
    }

    @Override
    public Context getAppContext() {
        return getContext().getApplicationContext();
    }

    @Override
    public int getCurrentPage() {
        return mCurrentPage;
    }

    @Override
    public int getInitPage(){
        return mDefaultPage;
    }

    @Override
    public void setCurrentPage(int currentPage) {
        if (currentPage >= mDefaultPage) {
            this.mCurrentPage = currentPage;
        }
    }

    @Override
    public int getPageSize() {
        return mPageSize;
    }

    @Override
    public AppRecyclerLoadMoreAdapter<T> getAppRecyclerLoadMoreAdapter() {
        return mAdapter;
    }

    public int getState() {
        return mState;
    }

    @Override
    public void setState(int mState) {
        this.mState = mState;
    }

    public AbsAppRecyclerView<V, T> setPageSize(int mPageSize) {
        this.mPageSize = mPageSize;
        return this;
    }

    @Override
    public boolean needShowEmptyNoData() {
        return needShowEmptyNoData;
    }

    public AbsAppRecyclerView<V, T> showBack2TopView(int visiblePosition) {
        this.back2TopViewVisiblePosition = visiblePosition;
        return this;
    }

    public AbsAppRecyclerView<V, T> setAdapter(AppRecyclerLoadMoreAdapter<T> mAdapter) {
        this.mAdapter = mAdapter;
        return this;
    }

    public AbsAppRecyclerView<V, T> setLayoutManager(RecyclerView.LayoutManager layoutManager) {
        this.layoutManager = layoutManager;
        return this;
    }

    public AbsAppRecyclerView<V, T> setRequestDataIfViewCreated(boolean requestDataIfViewCreated) {
        this.requestDataIfViewCreated = requestDataIfViewCreated;
        return this;
    }

    public AbsAppRecyclerView<V, T> setNeedShowEmptyNoData(boolean needShowEmptyNoData) {
        this.needShowEmptyNoData = needShowEmptyNoData;
        return this;
    }

    public AbsAppRecyclerView<V, T> setNeedShowLoadingView(boolean needShowLoadingView) {
        this.needShowLoadingView = needShowLoadingView;
        return this;
    }

    public AbsAppRecyclerView<V, T> setInitPage(int initPage) {
        this.mDefaultPage = initPage;
        this.mCurrentPage = initPage;
        return this;
    }

    public AbsAppRecyclerView<V, T> setRecyclerViewPadding(Rect rect){
        this.mRecyclerViewPaddingRect = rect;
        return this;
    }

    public AbsAppRecyclerView<V, T> addItemDecoration(@NonNull RecyclerView.ItemDecoration itemDecoration){
        this.mItemDecorationList.add(itemDecoration);
        return this;
    }

    public AbsAppRecyclerView<V, T> setPullRefreshEnable(boolean pullRefreshEnable) {
        this.pullRefreshEnable = pullRefreshEnable;
        return this;
    }

    public AbsAppRecyclerView<V, T> setClickRefreshEnable(boolean clickRefreshEnable) {
        this.clickRefreshEnable = clickRefreshEnable;
        return this;
    }

    public AbsAppRecyclerView<V, T> setTimeRefresh(boolean timeRefresh) {
        isTimeRefresh = timeRefresh;
        return this;
    }

    public AbsAppRecyclerView<V, T> setNoDataContent(String noDataContent) {
        if(mErrorLayout != null) {
            mErrorLayout.setNoDataContent(noDataContent);
        }
        return this;
    }

    public AbsAppRecyclerView<V, T> setNoDataTextSize(float sp) {
        if (mErrorLayout != null) {
            mErrorLayout.setNoDataTextSize(sp);
        }
        return this;
    }

    public AbsAppRecyclerView<V, T> setEmptyLayoutGravity(int gravity, int topMargin) {
        if(mErrorLayout != null) {
            mErrorLayout.setContentGravity(gravity, topMargin);
        }
        return this;
    }

    public AbsAppRecyclerView<V, T> setNoDataImage(int image) {
        if(mErrorLayout != null) {
            mErrorLayout.setNoDataImage(image);
        }
        return this;
    }

    /**
     * 设置无数据时的背景
     * @param bgRes
     * @return
     */
    public AbsAppRecyclerView<V, T> setEmptyLayoutBg(int bgRes) {
        if(mErrorLayout != null) {
            mErrorLayout.setBackgroundResource(Math.max(bgRes, 0));
        }
        return this;
    }

    public AbsAppRecyclerView<V, T> setRequestListener(AppRequestListener requestListener) {
        this.requestListener = requestListener;
        return this;
    }

    public AbsAppRecyclerView<V, T> setAppLoadMoreParams(AppLoadMoreParams loadMoreParams) {
        this.loadMoreParams = loadMoreParams;
        return this;
    }

    public AbsAppRecyclerView<V, T> setExecuteListener(AppRecyclerViewExecuteListener<T> executeListener) {
        this.executeListener = executeListener;
        return this;
    }

    @Override
    public AppRequestListener getRequestListener() {
        return requestListener;
    }

    @Override
    public AppLoadMoreParams getLoadMoreParams() {
        return loadMoreParams;
    }

    @Override
    public AppRecyclerViewExecuteListener<T> getExecuteListener() {
        return executeListener;
    }

    /**
     * 状态重置
     */
    public void reset() {
        mCurrentPage = mDefaultPage;
        mState = AppLoadDataState.STATE_NONE;
        mAdapter.setState(AppListAdapterLoadDataState.STATE_DEFAULT);
        mAdapter.clear();
        hideLoading();
    }

    public interface AppRequestListener {
        /**
         * 下拉请求
         * */
        void sendRequestData(boolean refresh, int page);

        /**
         * 加载更多请求
         * */
        default void sendRequestLoadMoreData(int page) {

        }
    }

    public interface AppLoadMoreParams {
        /**
         * 是否需要分页。分页时加载更多数据时，是否显示"加载更多"View通过showLoadMoreView()控制
         *
         * @return
         */
        default boolean needLoadMore() {
            return true;
        }

        /**
         * 分页加载是否显示"加载更多"View。有的分页是滑到快到底部时，自动加载更多，则不需要显示"加载更多"View
         * @return
         */
        default boolean showLoadMoreView() {
            return true;
        }

        /**
         * 是否显示"没有更多数据"View
         *
         * @return
         */
        default boolean showNoMoreView() {
            return true;
        }

        default Drawable loadMoreBg() {
            return null;
        }
    }

    protected abstract int defaultLayout();

    protected abstract void initRefreshView();

    /** 设置顶部正在加载的状态 */
    public abstract void setSwipeRefreshLoadingState();

    /** 设置顶部加载完毕的状态 */
    @Override
    public abstract void setSwipeRefreshLoadedState();

}
