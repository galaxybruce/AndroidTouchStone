package com.galaxybruce.main.ui.mvvm.request

import android.annotation.SuppressLint

import com.galaxybruce.component.ui.jetpack.JPBaseRequest

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers

import com.galaxybruce.main.ui.mvvm.viewmodel.MainViewModel

class MainRequest(private val viewModel: MainViewModel) : JPBaseRequest() {

//    private val mApi: LiveApi = KRetrofitFactory.createService(LiveApi::class.java)

    @SuppressLint("CheckResult")
    fun performRequest(activityId: String?) {
//        val requestUrl: String = LiveServer.URL_CREATE_LIVE
//
//        mApi.createLive(requestUrl, liveCreateModel)
//            .compose(this.handleEverythingResult<LiveEntity<Any>>(true))
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe({
//               this.viewModel.title.set("返回数据成功")
//            }, {
//                showToast(it.message)
//                this.viewModel.title.set("返回数据失败")
//            })
        viewModel.name.value = "返回数据成功"
    }
}