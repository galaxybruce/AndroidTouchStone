package com.galaxybruce.component.ui.jetpack

import androidx.lifecycle.viewModelScope
import com.galaxybruce.component.net.exception.AppLoginExpiresException
import com.galaxybruce.component.net.exception.AppNetException
import com.galaxybruce.component.net.exception.AppNetExceptionHandler
import com.galaxybruce.component.net.model.IAppBean
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * @date 2020/9/16 11:48
 * @author bruce.zhang
 * @description 逻辑处理的入口类，在这里可以调用JPBaseRepository或者其他更深层次的逻辑处理类
 *
 *  注意：[JPBaseRequest] 与 [JPBaseRequestV2]的区别：
 *  JPBaseRequestV2 在 JPBaseRequest基础上支持协程
 *
 */
abstract class JPBaseRequestV2(private val viewModel: JPBaseViewModel)
    : JPBaseRequest() {

    /**
     * 处理耗时任务，使场景：
     * 1. 处理单个的耗时任务，也可以是网络请求
     * 2. 处理多个网络请求
     *
     * 例如：
     * doTask(
     *     {
     *         val requestUrl: String = DemoUrl.URL_DEMO
     *         coroutineScope {
     *             val deferredOne = async { mApi.getDataSuspend(requestUrl) }
     *             val deferredTwo = async { mApi.getDataSuspend(requestUrl) }
     *             val response1 = deferredOne.await()
     *             val response2 = deferredTwo.await()
     *             JSON.toJSONString(response1.data) + "\n" + JSON.toJSONString(response2.data)
     *        }
     *    }, { response ->
     *        this.viewModel.name.value = response
     *    }, { exception ->
     *         showToast(exception.message)
     *     },
     *     true)
     */
    fun <T> doTask(
        block: suspend () -> T,
        successCallback: (T) -> Unit = {},
        errorCallback: (Throwable) -> Unit = {},
        showLoading: Boolean = false,
        loadingMessage: String? = null
    ) {
        viewModel.viewModelScope.launch {
            kotlin.runCatching {
                if (showLoading) {
                    showLoadingProgress(loadingMessage)
                }
                withContext(Dispatchers.IO) {
                    block()
                }
            }.onSuccess {
                if(showLoading) {
                    hideLoadingProgress()
                }
                successCallback(it)
            }.onFailure {
                if(showLoading) {
                    hideLoadingProgress()
                }
                errorCallback(AppNetExceptionHandler.handleException(it))
            }
        }
    }

    /**
     * 处理单个网络请求
     * 例如：
     * request(
     *     {
     *         val requestUrl: String = DemoUrl.URL_DEMO
     *         mApi.getDataSuspend(requestUrl)
     *     }, { response ->
     *         this.viewModel.name.value = JSON.toJSONString(response.data)
     *     }, { exception ->
     *         showToast(exception.message)
     *     }
     * )
     *
     * @param block 请求体方法，必须要用suspend关键字修饰
     * @param success 成功回调
     * @param error 失败回调
     * @param showLoading 是否显示加载框
     * @param loadingMessage 加载框提示内容
     *
     */
    fun <T: IAppBean> request(
        block: suspend () -> T,
        successCallback: (T) -> Unit = {},
        errorCallback: (Throwable) -> Unit = {},
        showLoading: Boolean = true,
        loadingMessage: String? = null
    ): Job {
        return viewModel.viewModelScope.launch {
            kotlin.runCatching {
                if (showLoading) {
                    showLoadingProgress(loadingMessage)
                }
                // 请求体
                withContext(Dispatchers.IO) {
                    block()
                }
            }.onSuccess { response ->
                // 网络请求完成 关闭弹窗
                if(showLoading) {
                    hideLoadingProgress()
                }
                //统一处理登录态失效
                if (response.isExpireLogin) {
                    reLogin()
                }
                //是否处理业务异常
                if (response.isSuccessful) {
                    successCallback(response)
                } else {
                    errorCallback(AppNetException(response.code, response.message))
                }
            }.onFailure { throwable ->
                // 网络请求完成 关闭弹窗
                if(showLoading) {
                    hideLoadingProgress()
                }
                throwable.printStackTrace()
                // 统一处理登录态失效
                if(throwable is AppLoginExpiresException) {
                    reLogin()
                }
                //失败回调
                errorCallback(AppNetExceptionHandler.handleException(throwable))
            }
        }
    }
}