package com.galaxybruce.main.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import com.galaxybruce.base.ui.activity.AppBaseActivity
import com.galaxybruce.component.ui.activity.AppTitleInfo
import com.galaxybruce.component.ui.jetpack.JPDataBindingConfig
import com.galaxybruce.component.util.AppConstants
import com.galaxybruce.main.BR
import com.galaxybruce.main.R
import com.galaxybruce.main.databinding.NetTestLayoutBinding
import com.galaxybruce.main.ui.mvvm.viewmodel.NetTestViewModel


class NetTestActivity : AppBaseActivity<NetTestViewModel, NetTestLayoutBinding>() {

    companion object {
        @JvmStatic
        fun start(context: Context) {
            val starter = Intent(context, NetTestActivity::class.java)
            context.startActivity(starter)
        }
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

    override fun getTitleInfo(): AppTitleInfo {
        return AppTitleInfo.build {
            titleMode = AppConstants.TITLE_MODE_LINEAR
            title = "NetTestActivity"
            showHomeAsUp = true
        }
    }

    override fun bindData(savedInstanceState: Bundle?) {
        super.bindData(savedInstanceState)

        // todo LiveData数据监听例子，实际代码请删除
        setLiveDataObserver(
            mPageViewModel.name,
            Observer { s: String? -> showToast("name has changed") })

    }

    private fun performRequest() {
        // todo perform net Requrest
        mPageViewModel.request.doTask {
            mPageViewModel.name.value = it
        }
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