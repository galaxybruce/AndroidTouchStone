package com.galaxybruce.component.ui.view.recyclerview;

import android.content.Context;

import com.galaxybruce.component.ui.adapter.AppRecyclerLoadMoreAdapter;
import com.galaxybruce.component.ui.view.AppEmptyLayout;

/**
 * author : ZhuJie
 * date   : 2020/6/1217:17
 * desc   :
 * version: 1.0
 */
interface IAppRecyclerView<T> {
    Context getAppContext();

    AppRecyclerLoadMoreAdapter<T> getAppRecyclerLoadMoreAdapter();

    AppEmptyLayout getEmptyLayout();

    int getInitPage();

    int getCurrentPage();

    void setCurrentPage(int currentPage);

    int getPageSize();

    boolean needShowEmptyNoData();

    void setSwipeRefreshLoadedState();

    void setState(int state);

    AppLoadMoreParams getLoadMoreParams();

    AbsAppRecyclerView.AppRequestListener getRequestListener();

    AppRecyclerViewExecuteListener<T> getExecuteListener();

}
