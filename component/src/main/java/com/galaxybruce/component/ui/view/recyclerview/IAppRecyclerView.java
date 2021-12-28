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
interface IAppRecyclerView {
    Context getAppContext();

    AppRecyclerLoadMoreAdapter getAppRecyclerLoadMoreAdapter();

    AppEmptyLayout getEmptyLayout();

    int getInitPage();

    int getCurrentPage();

    void setCurrentPage(int mCurrentPage);

    int getPageSize();

    boolean needShowEmptyNoData();

    void setSwipeRefreshLoadedState();

    void setState(int mState);

    AbsAppRecyclerView.AppRequestListener getBbsRequestListener();

    AppRecyclerViewExecuteListener getBbsExecuteListener();

}
