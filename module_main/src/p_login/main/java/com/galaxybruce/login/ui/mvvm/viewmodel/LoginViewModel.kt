package com.galaxybruce.login.ui.mvvm.viewmodel

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.galaxybruce.component.ui.jetpack.JPBaseRequest
import com.galaxybruce.component.ui.jetpack.JPBaseViewModel
import com.galaxybruce.login.ui.mvvm.request.LoginRequest


class LoginViewModel : JPBaseViewModel() {

    // ObservableField 有防抖的特点, 防抖可以避免重复刷新 以减少不必要的性能开销，所以看情况选择 ObservableField 或 LiveData。
    val email = ObservableField<String>()
    val password = ObservableField<String>()

    val loginSuccess = MutableLiveData<Boolean>()

    val loginEnable = object : ObservableBoolean(email, password) {
        override fun get(): Boolean {
            return password.get()?.length ?: 0 >= 6
        }
    }

    val request = LoginRequest(this)

    override fun getRequests(): List<JPBaseRequest> {
        return listOf(request)
    }

    init {
    }
}