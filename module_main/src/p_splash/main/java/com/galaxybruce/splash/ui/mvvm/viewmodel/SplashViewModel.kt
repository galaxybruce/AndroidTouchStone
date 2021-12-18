package com.galaxybruce.splash.ui.mvvm.viewmodel

import com.galaxybruce.component.ui.jetpack.JPBaseRequest
import com.galaxybruce.component.ui.jetpack.JPBaseViewModel
import com.galaxybruce.splash.ui.mvvm.request.SplashRequest

class SplashViewModel : JPBaseViewModel() {

    val request = SplashRequest(this)

    override fun getRequests(): List<JPBaseRequest> {
        return listOf(request)
    }

    init {
    }
}