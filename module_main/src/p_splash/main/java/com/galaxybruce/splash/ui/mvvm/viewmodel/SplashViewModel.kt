package com.galaxybruce.splash.ui.mvvm.viewmodel

import androidx.lifecycle.MutableLiveData
import com.galaxybruce.component.ui.jetpack.JPBaseRequest
import com.galaxybruce.component.ui.jetpack.JPBaseViewModel
import com.galaxybruce.splash.ui.mvvm.request.SplashRequest

class SplashViewModel : JPBaseViewModel() {

    /**
     * 数据加载完成，比如广告请求完成
     */
    val dataLoaded = MutableLiveData<Boolean>()

    val request = SplashRequest(this)

    override fun getRequests(): List<JPBaseRequest> {
        return listOf(request)
    }

    init {
        dataLoaded.value = false
    }
}