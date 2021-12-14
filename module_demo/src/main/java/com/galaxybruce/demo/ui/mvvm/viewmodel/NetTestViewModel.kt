package com.galaxybruce.demo.ui.mvvm.viewmodel

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.galaxybruce.component.ui.jetpack.JPBaseRequest
import com.galaxybruce.component.ui.jetpack.JPBaseViewModel
import com.galaxybruce.demo.ui.mvvm.request.NetTestRequest


class NetTestViewModel : JPBaseViewModel() {

    // ObservableField 有防抖的特点, 防抖可以避免重复刷新 以减少不必要的性能开销，所以看情况选择 ObservableField 或 LiveData。
    val title = ObservableField<String>()
    val name = MutableLiveData<String?>()

    val request = NetTestRequest(this)

    override fun getRequests(): List<JPBaseRequest> {
        return listOf(request)
    }

    init {
        name.value = "初始化数据"
    }
}