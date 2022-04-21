package com.galaxybruce.splash.ui.mvvm.request

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LifecycleOwner
import com.galaxybruce.component.ui.jetpack.JPBaseRequest
import com.galaxybruce.splash.ui.mvvm.viewmodel.SplashViewModel

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers


class SplashRequest(private val viewModel: SplashViewModel) : JPBaseRequest() {

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
    }

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)

        Log.i("aaaaaaaaaaaa", "request.onCreate")
    }
}