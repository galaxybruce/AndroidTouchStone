package com.galaxybruce.main.ui.activity

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.billy.cc.core.component.CC
import com.galaxybruce.base.ui.activity.AppBaseActivity
import com.galaxybruce.component.router.AppRouterUrlBuilder
import com.galaxybruce.component.ui.activity.AppTitleInfo
import com.galaxybruce.component.ui.dialog.AppConfirmDialog
import com.galaxybruce.component.ui.jetpack.JPDataBindingConfig
import com.galaxybruce.component.util.AppConstants
import com.galaxybruce.main.BR
import com.galaxybruce.main.R
import com.galaxybruce.main.databinding.MainLayoutBinding
import com.galaxybruce.main.ui.mvvm.viewmodel.MainViewModel

@Route(path = "/app/main")
class MainActivity : AppBaseActivity<MainViewModel, MainLayoutBinding>() {

    override fun initDataBindConfig(): JPDataBindingConfig {
        return JPDataBindingConfig(bindLayoutId())
            .addBindingParam(BR.vm, mPageViewModel)
            .addBindingParam(BR.click, ClickProxy())
    }

    override fun bindLayoutId(): Int {
        return R.layout.main_layout
    }

    override fun getTitleInfo(): AppTitleInfo {
        return AppTitleInfo.build {
            titleMode = AppConstants.TITLE_MODE_LINEAR
            title = "首页"
            showHomeAsUp = false
        }
    }

    override fun bindData(savedInstanceState: Bundle?) {
        super.bindData(savedInstanceState)

    }

    private fun performRequest() {
        mPageViewModel.request.performRequest(this)
    }

    /**
     * 页面事件类，可以把所有事件都写在这里
     */
    inner class ClickProxy {

        fun onAlertDialogClick() {
            AppConfirmDialog.create("提示",
                "进入NetTestActivity",
                false,
                object : AppConfirmDialog.AppConfirmDialogCallback {
                    override fun onCancel() {
                        showToast("cancel== ${mPageViewModel.n.set(mPageViewModel.n.get() ?: 0 + 1)}")
                    }

                    override fun onConfirm() {
                        showToast("ok== ${mPageViewModel.n.set(mPageViewModel.n.get() ?: 0 + 1)}")
                        AppRouterUrlBuilder.instance("/test/TestActivity").go(mActivity)
                    }
                })
                .show(mActivity, "AppConfirmDialog_1")
        }

        fun onRequestClick() {
            performRequest()
        }

        fun onComponentClick() {
            CC.obtainBuilder("SampleComponent")
                .setActionName("showToast")
                .build()
                .call()
        }

        fun onUniMPClick() {
            AppRouterUrlBuilder.instance("/unimp/UniMPMainActivity").go(mActivity)
        }
    }

}