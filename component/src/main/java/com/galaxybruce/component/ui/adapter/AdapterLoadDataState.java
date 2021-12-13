package com.galaxybruce.component.ui.adapter;

/**
 * Created by bruce.zhang on 2015/12/29.
 */
public interface AdapterLoadDataState {

    public static final int STATE_DEFAULT = 0;
    public static final int STATE_LOAD_MORE = 1;            //正在加载更多
    public static final int STATE_EMPTY_ITEM = 2;           //没有数据
    public static final int STATE_NO_MORE = 3;              //已加载完毕
    public static final int STATE_NETWORK_ERROR = 4;        //网络错误

}
