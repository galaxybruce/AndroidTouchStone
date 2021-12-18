package com.galaxybruce.splash.ui.activity

import android.os.Bundle
import android.view.View
import com.galaxybruce.base.manager.AppUserInfoManager
import com.galaxybruce.base.ui.activity.AppBaseActivity
import com.galaxybruce.component.interal.AppInternal
import com.galaxybruce.component.router.RouterUrlBuilder
import com.galaxybruce.component.ui.jetpack.JPBaseViewModel
import com.galaxybruce.component.ui.jetpack.JPDataBindingConfig
import com.galaxybruce.component.util.AppConstants
import com.galaxybruce.main.R
import com.galaxybruce.main.BR
import com.galaxybruce.main.databinding.SplashLayoutBinding
import com.galaxybruce.splash.ui.mvvm.viewmodel.SplashViewModel

/**
 * @date 2021/4/23 22:30
 * @author
 * @description 启动页
 *
 * <p>
 * modification history:
 */
class SplashActivity : AppBaseActivity<SplashLayoutBinding>() {

    private lateinit var mPageViewModel: SplashViewModel

    override fun initViewModel(): JPBaseViewModel {
        mPageViewModel = getActivityViewModel(SplashViewModel::class.java)
        return mPageViewModel
    }

    override fun initDataBindConfig(): JPDataBindingConfig {
        return JPDataBindingConfig(bindLayoutId())
            .addBindingParam(BR.vm, mPageViewModel)
    }

    override fun bindLayoutId(): Int {
        return R.layout.splash_layout
    }

    override fun initView(view: View?) {
        super.initView(view)
    }

    override fun bindData(savedInstanceState: Bundle?) {
        super.bindData(savedInstanceState)

        if(!AppInternal.getInstance().mustLogin() || AppUserInfoManager.getInstance().isLogin) {
            RouterUrlBuilder.instance("main").go(this)
        } else {
            RouterUrlBuilder.instance("login")
                .addParam(AppConstants.Login.KEY_LOGIN_SUCCESS_ROUTER, "main").go(this)

        }
        finish()
    }
}