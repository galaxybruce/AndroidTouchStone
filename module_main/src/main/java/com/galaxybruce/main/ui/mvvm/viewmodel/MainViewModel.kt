package com.galaxybruce.main.ui.mvvm.viewmodel

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData

import com.galaxybruce.component.ui.jetpack.JPBaseRequest
import com.galaxybruce.component.ui.jetpack.JPBaseViewModel

import com.galaxybruce.main.ui.mvvm.request.MainRequest

class MainViewModel : JPBaseViewModel() {

    // ObservableField 有防抖的特点, 防抖可以避免重复刷新 以减少不必要的性能开销，所以看情况选择 ObservableField 或 LiveData。
    val n = ObservableField<Int>()
    val name = MutableLiveData<String?>()

    val request = MainRequest(this)

    override fun getRequests(): List<JPBaseRequest> {
        return listOf(request)
    }

    init {
        name.value = "初始化数据"
    }
}