package com.galaxybruce.main.ui.activity

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.alibaba.android.arouter.facade.annotation.Route
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.ColorUtils
import com.galaxybruce.base.ui.activity.AppBaseActivity
import com.galaxybruce.component.ui.jetpack.JPDataBindingConfig
import com.galaxybruce.main.BR
import com.galaxybruce.main.R
import com.galaxybruce.main.databinding.NetTestLayoutBinding
import com.galaxybruce.main.ui.mvvm.viewmodel.NetTestViewModel


@Route(path = "/test/TestActivity")
class NetTestActivity : AppBaseActivity<NetTestLayoutBinding, NetTestViewModel>() {

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

    override fun initView(view: View?) {
        super.initView(view)
        initTitle()
    }

    open fun initTitle() {
        BarUtils.setStatusBarColor(this, ColorUtils.getColor(com.galaxybruce.component.R.color.colorPrimaryDark))
        BarUtils.addMarginTopEqualStatusBarHeight(binding.toolbar)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            this.setDisplayHomeAsUpEnabled(true)
            this.title = "NetTestActivity"
        }
    }

    override fun bindData(savedInstanceState: Bundle?) {
        super.bindData(savedInstanceState)

        mPageViewModel.title.set("xxx")
        // todo LiveData数据监听例子，实际代码请删除
        setLiveDataObserver(
            mPageViewModel.name,
            Observer { s: String? -> showToast("name has changed") })

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
            performRequest()
        }
    }

}