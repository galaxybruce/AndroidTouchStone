package com.galaxybruce.component.ui.view.recyclerview;

import com.galaxybruce.component.net.model.AppBaseBean;
import com.galaxybruce.component.ui.adapter.AppListAdapterLoadDataState;
import com.galaxybruce.component.ui.adapter.AppRecyclerLoadMoreAdapter;
import com.galaxybruce.component.ui.view.AppEmptyLayout;
import com.galaxybruce.component.util.ToastUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @date 2018/12/20 15:49
 * @author
 * @description 列表请求返回结果处理
 * <p>
 * modification history:
 */
public class AppRecyclerViewExecuteListenerImpl<T> implements AppRecyclerViewExecuteListener<T> {
    // 错误信息
    protected AppBaseBean mResult;
    protected IAppRecyclerView<T> appRecyclerView;

    public AppRecyclerViewExecuteListenerImpl(IAppRecyclerView<T> appRecyclerView) {
        this.appRecyclerView = appRecyclerView;
    }

    @Override
    public void executeOnLoadDataSuccess(List<T> data) {
        if (data == null) {
            data = new ArrayList<>();
        }

        if (mResult != null && !mResult.isSuccessful()) {
            ToastUtils.showToast(appRecyclerView.getAppContext(), mResult.getMessage());
        }

        AppRecyclerLoadMoreAdapter<T> adapter = appRecyclerView.getAppRecyclerLoadMoreAdapter();
        if(adapter == null) {
            return;
        }

        AppEmptyLayout emptyLayout = appRecyclerView.getEmptyLayout();
        if (emptyLayout != null) {
            emptyLayout.setErrorType(AppEmptyLayout.HIDE_LAYOUT);
        }
        if (appRecyclerView.getCurrentPage() == appRecyclerView.getInitPage()
            // 强制加载更多时，currentPage是第一页，不能清理顶部数据
            && adapter.getState() != AppListAdapterLoadDataState.STATE_FORCE_LOAD_MORE) {
            adapter.clear(false);
        }

        int position = adapter.getDataSize() + adapter.getHeaderViewCount();
        int count = data.size();
        adapter.addData(data, false);

        if(data.size() == 0 && appRecyclerView.getCurrentPage() > appRecyclerView.getInitPage()) {
            appRecyclerView.setCurrentPage(appRecyclerView.getCurrentPage() - 1);
        }

        if (appRecyclerView.getLoadMoreParams().getNeedLoadMore()) {
            int adapterState = AppListAdapterLoadDataState.STATE_DEFAULT;
            if (adapter.getDataSize() == 0) {
                adapterState = AppListAdapterLoadDataState.STATE_EMPTY_ITEM;
            } else if (data.size() < appRecyclerView.getPageSize()) {
                adapterState = AppListAdapterLoadDataState.STATE_NO_MORE;
            } else {
                adapterState = AppListAdapterLoadDataState.STATE_LOAD_MORE;
            }
            adapter.setState(adapterState);
        } else {
            // 不分页情况，也展示"没有更多了"提示
            int adapterState = AppListAdapterLoadDataState.STATE_NO_MORE;
            adapter.setState(adapterState);
        }
        adapter.notifyDataSetChanged();

        if ((adapter.getDataSize() + adapter.getHeaderViewCount()) == 0) {
            if (appRecyclerView.needShowEmptyNoData()) {
                if (emptyLayout != null) {
                    emptyLayout.setErrorType(AppEmptyLayout.NO_DATA);
                }
            }
        }
    }

    @Override
    public void executeOnLoadDataError(String error) {
//        if (mCurrentPage == 0 && !CacheManager.isExistDataCache(getActivity(), getCacheKey())) {
        AppEmptyLayout emptyLayout = appRecyclerView.getEmptyLayout();
        if(emptyLayout != null) {
            if (appRecyclerView.getCurrentPage() == appRecyclerView.getInitPage()) {
                if (appRecyclerView.needShowEmptyNoData()) {
                    emptyLayout.setErrorType(AppEmptyLayout.NETWORK_ERROR);
                } else {
                    emptyLayout.setErrorType(AppEmptyLayout.HIDE_LAYOUT);
                }
            } else {
                appRecyclerView.setCurrentPage(appRecyclerView.getCurrentPage() - 1);
                emptyLayout.setErrorType(AppEmptyLayout.HIDE_LAYOUT);
                appRecyclerView.getAppRecyclerLoadMoreAdapter().setState(AppListAdapterLoadDataState.STATE_NETWORK_ERROR);
                appRecyclerView.getAppRecyclerLoadMoreAdapter().notifyDataSetChanged();
            }
        }
    }

    @Override
    public void executeOnLoadFinish() {
        appRecyclerView.setSwipeRefreshLoadedState();
        appRecyclerView.setState(AppLoadDataState.STATE_NONE);
    }

}
