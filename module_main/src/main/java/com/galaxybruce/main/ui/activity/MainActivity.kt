package com.galaxybruce.main.ui.activity

import android.content.res.Resources
import android.os.Bundle
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.blankj.utilcode.util.AdaptScreenUtils
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.ColorUtils
import com.galaxybruce.base.ui.activity.AppBaseActivity
import com.galaxybruce.component.ui.dialog.AppConfirmDialog
import com.galaxybruce.component.ui.jetpack.JPDataBindingConfig
import com.galaxybruce.main.BR
import com.galaxybruce.main.R
import com.galaxybruce.main.databinding.MainLayoutBinding
import com.galaxybruce.main.ui.mvvm.viewmodel.MainViewModel

@Route(path = "/app/main")
class MainActivity : AppBaseActivity<MainLayoutBinding, MainViewModel>() {

    override fun initDataBindConfig(): JPDataBindingConfig {
        return JPDataBindingConfig(bindLayoutId())
            .addBindingParam(BR.vm, mPageViewModel)
            .addBindingParam(BR.click, ClickProxy())
    }

    override fun bindLayoutId(): Int {
        return R.layout.main_layout
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
            this.title = "首页"
        }
    }


    /**
     * 以pt为单位适配，这里的宽度已设计稿的标准像素尺寸为准
     */
    override fun getResources(): Resources {
        return AdaptScreenUtils.adaptWidth(super.getResources(), 750)
    }

    override fun bindData(savedInstanceState: Bundle?) {
        super.bindData(savedInstanceState)

        performRequest()
    }

    private fun performRequest() {
        mPageViewModel.request.performRequest("xxx")
    }

    /**
     * 页面事件类，可以把所有事件都写在这里
     */
    inner class ClickProxy {

        fun onImageClick() {
            AppConfirmDialog.getInstance("提示",
                "哈哈哈哈",
                false,
                object : AppConfirmDialog.AppConfirmDialogCallback {
                    override fun onCancel() {
                        showToast("cancel== ${mPageViewModel.n.set(mPageViewModel.n.get() ?: 0 + 1)}")
                    }

                    override fun onConfirm() {
                        showToast("ok== ${mPageViewModel.n.set(mPageViewModel.n.get() ?: 0 + 1)}")                    }
                })
                .show(mActivity, "aaa")

        }
    }
}