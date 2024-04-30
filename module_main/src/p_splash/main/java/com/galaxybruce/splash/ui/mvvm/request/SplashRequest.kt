package com.galaxybruce.splash.ui.mvvm.request

import androidx.lifecycle.LifecycleOwner
import com.galaxybruce.component.ui.jetpack.JPBaseRequest
import com.galaxybruce.component.ui.jetpack.SuccessCallback
import com.galaxybruce.component.util.extensions.runDelayedOnUiThread
import com.galaxybruce.splash.ui.mvvm.viewmodel.SplashViewModel


class SplashRequest(viewModel: SplashViewModel) : JPBaseRequest(viewModel) {

//    private val mApi: LiveApi = KRetrofitFactory.createService(LiveApi::class.java)

    fun requestAdData(successCallback: SuccessCallback<Boolean>) {
        runDelayedOnUiThread(2000) {
            successCallback(true)
        }
    }

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
    }
}