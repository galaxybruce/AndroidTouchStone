package com.galaxybruce.touchstone.demo.activity

import android.os.Bundle
import androidx.lifecycle.Observer
import com.alibaba.android.arouter.facade.annotation.Route
import com.galaxybruce.component.ui.jetpack.JPBaseActivity
import com.galaxybruce.component.ui.jetpack.JPBaseViewModel
import com.galaxybruce.component.ui.jetpack.JPDataBindingConfig

import com.galaxybruce.touchstone.R
import com.galaxybruce.touchstone.BR
import com.galaxybruce.touchstone.demo.mvvm.viewmodel.NetTestViewModel
import com.galaxybruce.touchstone.databinding.NetTestLayoutBinding

@Route(path = "/test/TestActivity")
class NetTestActivity : JPBaseActivity<NetTestLayoutBinding>() {

    private lateinit var mPageViewModel: NetTestViewModel

    override fun initViewModel(): JPBaseViewModel {
        mPageViewModel = getActivityViewModel(NetTestViewModel::class.java)
        // todo tip: 这里还可能会初始化全局的ViewModel
        // mSharedViewModel = getAppViewModelProvider().get(SharedViewModel::class.java);
        return mPageViewModel
    }

    override fun initDataBindConfig(): JPDataBindingConfig {
        return JPDataBindingConfig(bindLayoutId())
            .addBindingParam(BR.vm, mPageViewModel)
            .addBindingParam(BR.click, ClickProxy())
    }

    override fun bindLayoutId(): Int {
        return R.layout.net_test_layout
    }

    override fun initData(bundle: Bundle?, savedInstanceState: Bundle?) {
        super.initData(bundle, savedInstanceState)

        // todo init data from Intent
//        var a = intent.getStringExtra("xxx")
    }

    override fun bindData(savedInstanceState: Bundle?) {
        super.bindData(savedInstanceState)

        mPageViewModel.title.set("xxx")
        // todo LiveData数据监听例子，实际代码请删除
        setLiveDataObserver(
            mPageViewModel.name,
            Observer { s: String? -> showToast("name has changed") })

        performRequest()
    }

    private fun performRequest() {
        // todo perform net Requrest
        mPageViewModel.request.performRequest("xxx")
    }

    /**
     * 页面事件类，可以把所有事件都写在这里
     */
    inner class ClickProxy {
        fun handleName() {
            showToast("handleName: " + mPageViewModel.name.value)
        }
    }
}