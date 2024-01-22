package com.galaxybruce.main.ui.mvvm.request


import android.annotation.SuppressLint
import com.alibaba.fastjson.JSON
import com.galaxybruce.component.net.AppServiceGenerator
import com.galaxybruce.component.ui.jetpack.JPBaseRequest
import com.galaxybruce.component.ui.jetpack.JPBaseRequestV2
import com.galaxybruce.main.model.ApkUpdateInfo
import com.galaxybruce.main.model.AppBean4Cms
import com.galaxybruce.main.network.http.DemoApi
import com.galaxybruce.main.network.http.DemoUrl
import com.galaxybruce.main.ui.mvvm.viewmodel.NetTestViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import java.lang.ref.ReferenceQueue

class NetTestRequest(private val viewModel: NetTestViewModel) : JPBaseRequestV2(viewModel) {

    private val mApi: DemoApi = AppServiceGenerator.createService(
        DemoApi::class.java)

    @SuppressLint("CheckResult")
    fun performRequest(activityId: String?) {
        val requestUrl: String = DemoUrl.URL_DEMO
        // RxJava
        mApi.getData(requestUrl)
            .compose(this.handleEverythingResult<AppBean4Cms<ApkUpdateInfo>>(true))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
               this.viewModel.name.value = JSON.toJSONString(it.data)
            }, {
                showToast(it.message)
            })
    }

    fun performRequestSuspend(activityId: String?) {
        request(
            {
                delay(1000)
                val requestUrl: String = DemoUrl.URL_DEMO
                mApi.getDataSuspend(requestUrl)
            }, { response ->
                this.viewModel.name.value = JSON.toJSONString(response.data)
            }, { exception ->
                showToast(exception.message)
            }
        )
    }

    /**
     * 处理耗时任务
     */
    fun doTask() {
        doTask(
            {
                val requestUrl: String = DemoUrl.URL_DEMO
                coroutineScope {
                    val deferredOne = async { mApi.getDataSuspend(requestUrl) }
                    val deferredTwo = async { mApi.getDataSuspend(requestUrl) }
                    val response1 = deferredOne.await()
                    val response2 = deferredTwo.await()
                    JSON.toJSONString(response1.data) + "\n" + JSON.toJSONString(response2.data)
                }
            }, { response ->
                this.viewModel.name.value = response
            }, { exception ->
                showToast(exception.message)
            },
            true)
    }

}