package com.galaxybruce.splash.ui.mvvm.request

import androidx.lifecycle.LifecycleOwner
import com.galaxybruce.component.ui.jetpack.JPBaseRequest
import com.galaxybruce.component.util.extensions.runDelayedOnUiThread
import com.galaxybruce.splash.ui.mvvm.viewmodel.SplashViewModel


class SplashRequest(private val viewModel: SplashViewModel) : JPBaseRequest() {

//    private val mApi: LiveApi = KRetrofitFactory.createService(LiveApi::class.java)

    fun requestAdData() {
        runDelayedOnUiThread(500) {
            viewModel.dataLoaded.value = true
        }
    }

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
    }
}