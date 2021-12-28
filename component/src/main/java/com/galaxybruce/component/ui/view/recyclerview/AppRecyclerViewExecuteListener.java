package com.galaxybruce.component.ui.view.recyclerview;

import java.util.List;

public interface AppRecyclerViewExecuteListener {
        /**
         * 请求成功执行
         * */
        void executeOnLoadDataSuccess(List data);

        /**
         * 请求失败执行
         * */
        void executeOnLoadDataError(String error);

        /**
         * 请求完成执行
         * */
        void executeOnLoadFinish();
}