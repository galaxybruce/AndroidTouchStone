package com.galaxybruce.main.ui.mvvm.request


import android.annotation.SuppressLint
import com.galaxybruce.component.net.AppServiceGenerator
import com.galaxybruce.component.ui.jetpack.JPBaseRequest
import com.galaxybruce.main.model.AppBean4Cms
import com.galaxybruce.main.model.GroupSocketHost
import com.galaxybruce.main.network.http.DemoApi
import com.galaxybruce.main.network.http.DemoUrl
import com.galaxybruce.main.ui.mvvm.viewmodel.NetTestViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class NetTestRequest(private val viewModel: NetTestViewModel) : JPBaseRequest() {

    private val mApi: DemoApi = AppServiceGenerator.createService(
        DemoApi::class.java)

    @SuppressLint("CheckResult")
    fun performRequest(activityId: String?) {
        val requestUrl: String = DemoUrl.URL_DEMO

        mApi.getData(requestUrl)
            .compose(this.handleEverythingResult<AppBean4Cms<GroupSocketHost>>(true))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
               this.viewModel.name.value = it.data.toString()
            }, {
                showToast(it.message)
                this.viewModel.title.set("返回数据失败")
            })
    }
}