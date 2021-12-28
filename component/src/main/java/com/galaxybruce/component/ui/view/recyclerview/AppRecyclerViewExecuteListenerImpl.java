package com.galaxybruce.component.ui.view.recyclerview;

import com.galaxybruce.component.net.model.AppBaseBean;
import com.galaxybruce.component.ui.adapter.AdapterLoadDataState;
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
public class AppRecyclerViewExecuteListenerImpl implements AppRecyclerViewExecuteListener {
    // 错误信息
    protected AppBaseBean mResult;
    protected IAppRecyclerView bbsRecyclerView;

    public AppRecyclerViewExecuteListenerImpl(IAppRecyclerView bbsRecyclerView) {
        this.bbsRecyclerView = bbsRecyclerView;
    }

    @Override
    public void executeOnLoadDataSuccess(List data) {
        if (data == null) {
            data = new ArrayList<>();
        }

        if (mResult != null && !mResult.isSuccessful()) {
            ToastUtils.showToast(bbsRecyclerView.getAppContext(), mResult.getMessage());
        }

        AppRecyclerLoadMoreAdapter adapter = bbsRecyclerView.getAppRecyclerLoadMoreAdapter();
        if(adapter == null) {
            return;
        }

        AppEmptyLayout emptyLayout = bbsRecyclerView.getEmptyLayout();
        if (emptyLayout != null) {
            emptyLayout.setErrorType(AppEmptyLayout.HIDE_LAYOUT);
        }
        if (bbsRecyclerView.getCurrentPage() == bbsRecyclerView.getInitPage()) {
            adapter.clear(false);
        }

        int position = adapter.getDataSize() + adapter.getHeaderViewCount();
        int count = data.size();
        adapter.addData(data, false);

        if(data.size() == 0 && bbsRecyclerView.getCurrentPage() > bbsRecyclerView.getInitPage()) {
            bbsRecyclerView.setCurrentPage(bbsRecyclerView.getCurrentPage()-1);
        }

        if (adapter.needLoadMore()) {
            int adapterState = AdapterLoadDataState.STATE_DEFAULT;
            if (adapter.getDataSize() == 0) {
                adapterState = AdapterLoadDataState.STATE_EMPTY_ITEM;
            } else if (data.size() < bbsRecyclerView.getPageSize()) {
                adapterState = AdapterLoadDataState.STATE_NO_MORE;
            } else {
                adapterState = AdapterLoadDataState.STATE_LOAD_MORE;
            }
            adapter.setState(adapterState);
        } else {
            // 不分页情况，也展示"没有更多了"提示
            int adapterState = AdapterLoadDataState.STATE_NO_MORE;
            adapter.setState(adapterState);
        }
        adapter.notifyDataSetChanged();

        if (adapter.getDataSize() == 0) {
            if (bbsRecyclerView.needShowEmptyNoData()) {
                if (emptyLayout != null) {
                    emptyLayout.setErrorType(AppEmptyLayout.NO_DATA);
                }
            }
        }
    }

    @Override
    public void executeOnLoadDataError(String error) {
//        if (mCurrentPage == 0 && !CacheManager.isExistDataCache(getActivity(), getCacheKey())) {
        AppEmptyLayout emptyLayout = bbsRecyclerView.getEmptyLayout();
        if(emptyLayout != null) {
            if (!bbsRecyclerView.needShowEmptyNoData()) {
                emptyLayout.setErrorType(AppEmptyLayout.HIDE_LAYOUT);
                return;
            }

            if (bbsRecyclerView.getCurrentPage() == bbsRecyclerView.getInitPage()) {
                emptyLayout.setErrorType(AppEmptyLayout.NETWORK_ERROR);
            } else {
                bbsRecyclerView.setCurrentPage(bbsRecyclerView.getCurrentPage() - 1);
                emptyLayout.setErrorType(AppEmptyLayout.HIDE_LAYOUT);
                bbsRecyclerView.getAppRecyclerLoadMoreAdapter().setState(AdapterLoadDataState.STATE_NETWORK_ERROR);
                bbsRecyclerView.getAppRecyclerLoadMoreAdapter().notifyDataSetChanged();
            }
        }
    }

    @Override
    public void executeOnLoadFinish() {
        bbsRecyclerView.setSwipeRefreshLoadedState();
        bbsRecyclerView.setState(AppLoadDataState.STATE_NONE);
    }

}
