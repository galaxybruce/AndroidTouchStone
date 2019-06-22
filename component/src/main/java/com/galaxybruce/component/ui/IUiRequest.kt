package com.galaxybruce.component.ui

/**
 * Date: 2017/2/20 17:58
 * Author: bruce.zhang
 * Description: UI界面上的请求接口
 * onRefresh -> requestData(true) -> sendRequestData();
 * requestDataWithLoading -> requestData(false)-> sendRequestData();
 * 子类只要重写sendRequestData就行了
 *
 *
 * Modification  History:
 */
interface IUiRequest : IUiData {

    /**
     * 显示errorlayout中的loading的请求
     */
    fun requestDataWithLoading()

    fun sendRequestData()
}
